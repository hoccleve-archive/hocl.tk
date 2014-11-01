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
@WebServlet("/info")
public class MyServlet extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        response.setContentType("text/plain");
        response.setBufferSize(8192);
        try (PrintWriter out = response.getWriter())
        {
            String xml = request.getParameter("q");
            Enumeration<String> en = request.getAttributeNames();
            out.println("attributes: ");
            while (en.hasMoreElements())
            {
                String x = en.nextElement();
                out.println(x +": " +request.getAttribute(x));
            }
            out.println("----");
            ServletContext ctx = request.getServletContext();
            out.println("server name: " + request.getServerName());
            out.println("server port: " + request.getServerPort());

            out.println("server info: " + ctx.getServerInfo());
            out.println("context path: " + ctx.getContextPath());
            out.println("real path of tei-numbers.xslt: " + ctx.getRealPath("tei-numbers.xslt"));
            out.println("resource paths: ");
            for (String s : ctx.getResourcePaths("/"))
            {
                out.println("  "+s);
            }
            out.println("----");
        }
    }
}

