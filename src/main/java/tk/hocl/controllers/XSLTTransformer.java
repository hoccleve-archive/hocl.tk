package tk.hocl.controllers;

import java.util.*;
import java.security.MessageDigest;
import java.security.DigestInputStream;
import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import tk.hocl.controllers.Transform;
import static tk.hocl.util.Util.*;
import tk.hocl.util.XSLTXTInputStream;
import com.zanthan.xsltxt.exception.SyntaxException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.Source;

public class XSLTTransformer extends HttpServlet
{
    /** This is the base for most of the XSLT transformations.
     *
     * To implement a new transformation, the simplest way is to override
     * HttpServlet::init, initializing xsltResourceName with the name of
     * the XSLT file to use. The file must be relative to the application
     * root (the root contains the WEB-INF directory)
     *
     * Each transformation should have at least one XSLTTransformer-derived
     * WebServlet that routes to it. Documentation for the transformation
     * should be provided as a Javadoc comment on the WebServlet.
     */

    /* Required for serializable objects */
    private static final long serialVersionUID = 1L;

    /** MUST be a local resource starting with "/"
     * rather than a full URI */
    public String xsltResourceName = null;
    /* TODO: Accept a sequence of resource names to
     * perform multiple transformations */
    //public String[] xsltResourceNames = null;
    /** The content type of the response */
    public String contentType = "text/xml";
    /** These are parameters that get passed to the transformer
     * (e.g., using Transform::setParameter). They can be set
     * by passing an identically named parameter in the request
     * so don't try to pass any private parameters that way.
     */
    public Map<String,Object> params = new HashMap<String,Object>();

    /** This is used for getting the last modified date of the resources involved in the
     * request. This will be the last-modified date of the newest resource.
     *
     * If a resource is provided as a parameter to the transformation in a derived class,
     * it's last-modified date will not be figured in
     */
    protected Date lastModified = new Date(0);

    /* This is the if-modified-since date provided in the request */
    private Date ifModifiedSince = new Date(0);
    private MyURIResolver uriResolver = new MyURIResolver();

    {
        TransformerFactory tf = Transform.getTransformerFactory();
        tf.setURIResolver(uriResolver);
        uriResolver.setServlet(this);
    }
    /** The URI resolver to use with the transformer.
     *
     * For URIs with a scheme (http:, file:, etc.) the URI is resolved
     * using java.net.URL. Otherwise, if the href starts with a '/',
     * then the resource is resolved against the request URI's server
     * location (e.g., localhost:8080), but if the href starts with some
     * other character, the href resolves against the context path
     * (e.g., localhost:8080/my-app/).
     */
    public static class MyURIResolver implements URIResolver
    {
        HttpServletRequest request = null;
        HttpServlet servlet = null;
        public void setRequest (HttpServletRequest req)
        {
            request = req;
        }

        public void setServlet (HttpServlet serv)
        {
            servlet = serv;
        }

        public Source resolve (String href, String base_IGNORED) throws TransformerException
        {
            System.out.println("Resolving URI "+href+"...");
            try
            {
                URI uri = new URI(href);
                Source res = null;
                if (uri.isAbsolute())
                {
                    res = Transform.getInputFromURL(uri.toString());
                }
                else
                {
                    // XXX: As a web resource, it might also make sense to resolve
                    // relative URIs against the servlet context.
                    // Note that this allows a user to access *anything* that lies
                    // in the classpath
                    String scheme = request.getScheme();
                    String host = request.getServerName();
                    int port = request.getServerPort();
                    String path = servlet.getServletContext().getContextPath();
                    if (href.charAt(0) == '/')
                    {
                        path = href;
                    }
                    else
                    {
                        path = path + "/" + href;
                    }
                    System.out.printf("scheme %s, host %s, port %d, path %s\n", scheme, host, port, path);
                    URL url = new URL(scheme,host,port, path);
                    System.out.println("Resolved URI "+url.toString());
                    res = Transform.getInput(url);
                }
                res.setSystemId(href);
                return res;
            }
            catch (URISyntaxException e)
            {
                throw new TransformerException("Incorrect URI", e);
            }
            catch (Exception e)
            {
                throw new TransformerException("Error resolving document URI", e);
            }
        }
    }

    /** Sets the HttpServletRequest for the URI resolver.
     *
     * The resolver uses the request in order to get the necessary to resolve
     * URIs relative to the servlet context
     */
    public void setResolverRequest (HttpServletRequest req)
    {
        uriResolver.setRequest(req);
    }
    /**
     * Executes a GET request.
     *
     * A `q' parameter provided in the request will be interpreted as the URL of an XML resource on which the transformation will
     * be performed. URLs relative to the WebServlet's context or to the host name will not be resolved, but must be fully specified.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        setResolverRequest(request);
        String xml = request.getParameter("q");
        URL xml_resource = null;

        if (xml == null)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a parameter `q' with the URL of your XML document.");
            return;
        }

        try
        {
            xml_resource = new URL(xml);
        }
        catch (MalformedURLException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The URL that you provided ("+xml+") is invalid. Reason: "+e.toString());
            return;
        }

        URLConnection conn = xml_resource.openConnection();
        if (conn.getLastModified() != 0)
        {
            Date xml_date = new Date(conn.getLastModified());
            updateLastModified(xml_date);
        }

        try (InputStream is = xml_resource.openStream())
        {
            doTransformation(request, response, is);
        }
    }

    /**
     * Executes a POST request.
     *
     * A `text` parameter provided in the request will be interpreted as the text of an XML resource on which the transformation will
     * be performed.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        setResolverRequest(request);
        String xml = request.getParameter("q");

        if (xml == null)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a parameter `q' with the text of your XML document.");
            return;
        }
        long xml_date_seconds = request.getDateHeader("Last-Modified");
        if (xml_date_seconds != 0)
        {
            Date xml_date = new Date(xml_date_seconds);
            updateLastModified(xml_date);
        }


        InputStream xml_stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        doTransformation(request, response, xml_stream);
    }

    /**
     * Performs the transformation for a GET or a POST.
     *
     * Performs the transformation specified by xsltResourceName variable or the xsl request parameter
     * on the input_xml stream which is typically provided by doGet or doPost. The content type will
     * be that specified by the contentType variable.
     *
     * Stylesheet parameters can be set through the request as well as parameters or set using the params
     * member variable.
     *
     * See the documentation for a subclass of XSLTTransformer and the stylsheet associated with that
     * subclass for a full description of the transformation performed.
     */
    private void doTransformation (HttpServletRequest request, HttpServletResponse response, InputStream input_xml) throws IOException
    {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(contentType);
        response.setBufferSize(8192);

        long ifmod = request.getDateHeader("If-Modified-Since");
        ifModifiedSince = new Date(ifmod);

        String xsl = null;
        if (xsltResourceName == null)
        {
            xsl = request.getParameter("xsl");
            if (xsl == null)
            {
                sendUserError(response, "You must provide a parameter `xsl' in order to perform a transformation");
                return;
            }
        }
        else
        {
            URL xslt_url = getResource(xsltResourceName);
            if (xslt_url != null)
            {
                URLConnection conn = xslt_url.openConnection();
                Date xslt_date = new Date(conn.getLastModified());
                updateLastModified(xslt_date);
                xsl = xslt_url.toString();
            }
            else
            {
                String error_message =  "Couldn't find the XSLT file to perform this operation. "+
                    "Please file a bug report about this error here: https://github.com/hoccleve-archive/hocl.tk/issues";
                sendServerError(response, error_message);
                return;
            }
        }

        extractTransformParameters(request);
        doTransformation0(response,xsl,input_xml);
    }

    private void sendUserError(HttpServletResponse response, String error_message)
    {
        try
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, error_message);
        }
        catch (IOException e)
        {/* If we can't send an error back to the user, there's nothing else to do */}
    }

    private void sendServerError(HttpServletResponse response, String error_message)
    {
        try
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, error_message);
        }
        catch (IOException e)
        {
            System.out.println("Got an error while trying to send a server error. Error message:\n"+error_message);
        }
    }

    /** Performs the actual transformation, if necessary.
     */
    private void doTransformation0 (HttpServletResponse response, String xsl, InputStream input_xml)
    {
        if (lastModified.compareTo(ifModifiedSince) > 0)
        {
            response.setDateHeader("Last-Modified", lastModified.getTime() + 1);
            try
            {
                try (PrintWriter out = response.getWriter())
                {
                    if (xsl.endsWith(".xsltxt"))
                    {
                        URL u = new URL(xsl);
                        try
                        {
                            InputStream is = u.openStream();
                            XSLTXTInputStream xslt_in = new XSLTXTInputStream(is);
                            Transform.doTransformation(xslt_in, input_xml, out, params);
                        }
                        catch (SyntaxException e)
                        {
                            sendUserError(response, "Syntax error in XSLTXT: " +e.toString());
                            e.printStackTrace();
                        }
                        catch (IOException e)
                        {
                            sendUserError(response, "Couldn't open XSLTXT stream from URL: " +e.toString());
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Transform.doTransformation(xsl, input_xml, out, params);
                    }
                }
            }
            catch (Transform.MyTransformerException e)
            {
                sendTransformerError(response, e);
            }
            catch (IOException e)
            {
                sendServerError(response, "Couldn't open the page writer."+
                        " Please file a bug report with this message at: https://github.com/hoccleve-archive/hocl.tk/issues");
            }
        }
        else
        {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        }
    }

    private void sendTransformerError(HttpServletResponse response, Transform.MyTransformerException e)
    {
        ByteArrayOutputStream backtrace_stream;
        PrintStream backtrace_print_stream;

        String error_message = "Transformer error:\n"+e.exc.getMessageAndLocation();

        backtrace_stream = new ByteArrayOutputStream();
        backtrace_print_stream = new PrintStream(backtrace_stream);

        e.exc.printStackTrace(backtrace_print_stream);
        error_message += "\n"+backtrace_stream.toString();

        sendServerError(response, error_message);
    }

    /** Updates the lastModified if the given date is newer
     */
    private void updateLastModified(Date dt)
    {
        if (dt.compareTo(lastModified) > 0)
        {
            lastModified = dt;
        }
    }

    /** A deferred request parameter used by subclasses of XSLTTransformer.
     *
     * In setting the {@code params} variable, a ParamValue indicates that the HTTP request parameter
     * given as paramName holds the value for the XSLT parameter
     */
    public static class ParamValue
    {
        private String _n = "";
        ParamValue(String paramName)
        {
            _n = paramName;
        }
        String getName()
        {
            return _n;
        }
    }

    /** A callback for a subclass to provide any value it wants for a parameter.
     */
    public abstract static class RequestValue
    {
        public abstract Object get(HttpServletRequest req);
    }

    /** Copies string parameters from the HTTP request for the Transformation.
     *
     * Also allows for subclasses to do special processing on the parameters.
     */
    private void extractTransformParameters(HttpServletRequest request)
    {
        for (String p : params.keySet())
        {
            System.out.println("Parameter = "+p);
            if ((ParamValue.class).isInstance(params.get(p)))
            {
                ParamValue pv = (ParamValue)(params.get(p));
                params.put(p, request.getParameter(pv.getName()));
            }
            else if ((RequestValue.class).isInstance(params.get(p)))
            {
                params.put(p, ((RequestValue)params.get(p)).get(request));
            }
            else
            {
                String requestValue = request.getParameter(p);
                if (requestValue != null)
                {
                    params.put(p, requestValue);
                }
            }
        }
    }
}
