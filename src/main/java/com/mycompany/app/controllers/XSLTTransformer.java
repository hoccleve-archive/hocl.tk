package com.mycompany.app.controllers;

import java.util.*;
import java.security.MessageDigest;
import java.security.DigestInputStream;
import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.mycompany.app.controllers.Transform;
import static com.mycompany.app.util.Util.*;

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

    /**
     * Executes a GET request.
     *
     * A `q' parameter provided in the request will be interpreted as the URL of an XML resource on which the transformation will
     * be performed. URLs relative to the WebServlet's context or to the host name will not be resolved, but must be fully specified.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
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
            if (xml_date.compareTo(lastModified) > 0)
            {
                lastModified = xml_date;
            }
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
        String xml = request.getParameter("text");

        if (xml == null)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a parameter `text` with the text of your XML document.");
            return;
        }
        long xml_date_seconds = request.getDateHeader("Last-Modified");
        if (xml_date_seconds != 0)
        {
            Date xml_date = new Date(xml_date_seconds);
            if (xml_date.compareTo(lastModified) > 0)
            {
                lastModified = xml_date;
            }
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

        try (PrintWriter out = response.getWriter())
        {
            String xsl = null;
            if (xsltResourceName == null)
            {
                xsl = request.getParameter("xsl");
                if (xsl == null)
                {
                    String error_message =  "You must provide a parameter `xsl' in order to perform a transformation";
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST , error_message);
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
                    if (xslt_date.compareTo(lastModified) > 0)
                    {
                        lastModified = xslt_date;
                    }
                    xsl = xslt_url.toString();
                }
                else
                {
                    String error_message =  "Couldn't find the XSLT file to perform this operation. "+
                        "Please file a bug report about this error here: https://github.com/hoccleve-archive/hocl.tk/issues";
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR , error_message);
                    return;
                }
            }

            extractTransformParameters(request);

            if (lastModified.compareTo(ifModifiedSince) > 0)
            {
                response.setDateHeader("Last-Modified", lastModified.getTime() + 1);
                try
                {
                    Transform.doTransformation(xsl, input_xml, out, params);
                }
                catch (Transform.MyTransformerException e)
                {
                    String error_message = "Transformer error:\n"+e.exc.getMessageAndLocation();
                    ByteArrayOutputStream backtrace_stream = new ByteArrayOutputStream();
                    PrintStream backtrace_print_stream = new PrintStream(backtrace_stream);
                    e.printStackTrace(backtrace_print_stream);
                    error_message += backtrace_stream.toString();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR , error_message);
                }
            }
            else
            {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            }
        }
    }

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

    private void extractTransformParameters(HttpServletRequest request)
    {
        for (String p : params.keySet())
        {
            if ((ParamValue.class).isInstance(params.get(p)))
            {
                ParamValue pv = (ParamValue)(params.get(p));
                params.put(p, request.getParameter(pv.getName()));
            }
            String requestValue = request.getParameter(p);
            if (requestValue != null)
            {
                params.put(p, requestValue);
            }
        }
    }
}
