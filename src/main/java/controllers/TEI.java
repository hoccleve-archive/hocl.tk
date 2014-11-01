package controllers;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import controllers.Transform;

/* TODO: Make automatic mappings like this based on
 * the names of xslt files
 */
@WebServlet("/tei-line-numbers")
public class TEI extends HttpServlet
{
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
            //String xsl = "/tei-numbers.xslt";
            /* Maybe there's a better way to get this path... */
            ServletContext ctx = request.getServletContext();
            String xsl = ctx.getResource("/tei-numbers.xslt").toString();
            Transform.doTransformation(xsl, xml, out);
        }
    }
}
