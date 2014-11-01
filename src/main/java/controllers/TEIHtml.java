package controllers;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import controllers.Transform;

@WebServlet("/tei-html")
public class TEIHtml extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setBufferSize(8192);
        try (PrintWriter out = response.getWriter())
        {
            String xml = request.getParameter("q");
            //String xsl = "/tei-numbers.xslt";
            /* Maybe there's a better way to get this path... */
            ServletContext ctx = request.getServletContext();
            String xsl = ctx.getResource("/tei-poem.xslt").toString();
            Transform.doTransformation(xsl, xml, out);
        }
    }
}

