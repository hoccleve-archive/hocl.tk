package controllers;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import controllers.Transform;

@WebServlet("/transform")
public class XSLTTransformer extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    /* MUST be a local resource starting with "/"
     * rather than a full URI */
    public String xsltResourceName = null;
    public String contentType = "text/xml";
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setCharacterEncoding("UTF-8");
        response.setBufferSize(8192);/* I'm not even sure what this buffer size does... */
        try (PrintWriter out = response.getWriter())
        {
            String xml = request.getParameter("q");
            String xsl = null;
            ServletContext ctx = request.getServletContext();
            if (xsltResourceName == null)
            {
                xsl = request.getParameter("xsl");
            }
            else
            {
                xsl = ctx.getResource(xsltResourceName).toString();
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
}
