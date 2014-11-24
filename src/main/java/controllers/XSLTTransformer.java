package controllers;

import java.util.*;
import java.io.*;
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

    /* MUST be a local resource starting with "/"
     * rather than a full URI */
    public String xsltResourceName = null;
    /* TODO: Accept a sequence of resource names to
     * perform multiple transformations */
    //public String[] xsltResourceNames = null;
    /* The content type of the response */
    public String contentType = "text/xml";
    /* These are parameters that get passed to the transformer
     * (e.g., using Transform::setParameter). They can be set
     * by passing an identically named parameter in the request
     * so don't try to pass any private parameters that way.
     */
    public Map<String,Object> params = new HashMap<String,Object>();
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setCharacterEncoding("UTF-8");
        response.setBufferSize(8192);
        try (PrintWriter out = response.getWriter())
        {
            String xml = request.getParameter("q");
            String xsl = null;
            if (xsltResourceName == null)
            {
                xsl = request.getParameter("xsl");
            }
            else
            {
                xsl = getResource(xsltResourceName).toString();
            }

            if (xml == null || xsl == null)
            {
                response.setContentType("text/plain");
                out.printf("You must provide an XSL and XML to transform. Got %s and %s.\n", xsl, xml);
            }
            else
            {
                try
                {
                    Transform.doTransformation(xsl, xml, out);
                }
                catch (Transform.MyTransformerException e)
                {
                    response.setContentType("text/plain");
                    out.println(e.exc.getMessageAndLocation());
                }
            }
        }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            /* Unlike the GET, this is the actual XML text */
            String xml = request.getParameter("text");
            String xsl = null;
            if (xsltResourceName != null)
            {
                try
                {
                    xsl = getResource(xsltResourceName).toString();
                }
                catch (Exception e)
                {
                    System.out.println("This shouldn't be possible");
                    out.println(e);
                }
            }
            else
            {
                xsl = request.getParameter("xsl");
            }
            extractTransformParameters(request);

            if (xml == null || xsl == null)
            {
                response.setContentType("text/plain");
                out.printf("You must provide an XSL and XML to transform. Got %s and %s.\n", xsl, xml);
            }
            else
            {
                try
                {
                    InputStream xml_stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                    Transform.doTransformation(xsl, xml_stream, out, params);
                }
                catch (Transform.MyTransformerException e)
                {
                    response.setContentType("text/plain");
                    out.println("ERROR:");
                    out.println(e.exc.getMessageAndLocation());
                }
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
