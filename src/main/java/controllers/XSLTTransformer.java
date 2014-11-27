package controllers;

import java.util.*;
import java.security.MessageDigest;
import java.security.DigestInputStream;
import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import controllers.Transform;
import static util.Util.*;

@WebServlet("/transform")
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

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String xml = request.getParameter("q");

        if (xml == null)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a parameter `q' with the URL of your XML document.");
            return;
        }

        URL xml_resource = new URL(xml);
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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String xml = request.getParameter("text");
        InputStream xml_stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        doTransformation(request, response, xml_stream);
    }

    private void doTransformation (HttpServletRequest request, HttpServletResponse response, InputStream input_xml) throws IOException
    {
        response.setCharacterEncoding("UTF-8");
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
                response.setDateHeader("Last-Modified", lastModified.getTime());
                try
                {
                    Transform.doTransformation(xsl, input_xml, out, params);
                }
                catch (Transform.MyTransformerException e)
                {
                    String error_message = "Transformer error\n"+e.exc.getMessageAndLocation();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR , error_message);
                }
            }
            else
            {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            }
        }
    }

    private void extractTransformParameters(HttpServletRequest request)
    {
        for (String p : params.keySet())
        {
            String requestValue = request.getParameter(p);
            if (requestValue != null)
            {
                params.put(p, requestValue);
            }
        }
    }
}
