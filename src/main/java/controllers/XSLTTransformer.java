package controllers;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import controllers.Transform;

public class XSLTTransformer extends HttpServlet
{
    /* MUST be a local resource starting with "/"
     * rather than a full URI
     */
    public static String xsltResourceName;
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        response.setContentType("text/xml");
        response.setCharacterEncoding("UTF-8");
        response.setBufferSize(8192);
        try (PrintWriter out = response.getWriter())
        {
            String xml = request.getParameter("q");
            /* Maybe there's a better way to get this path... */
            ServletContext ctx = request.getServletContext();
            String xsl = ctx.getResource(xsltResourceName).toString();
            Transform.doTransformation(xsl, xml, out);
        }
    }
}
