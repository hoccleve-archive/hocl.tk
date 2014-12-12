package com.mycompany.app.controllers;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/** Prints out some information about the server and servlet context */
public class ServerInfo extends HttpServlet
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
            ServletContext ctx = this.getServletContext();
            out.println("server name: " + request.getServerName());
            out.println("server port: " + request.getServerPort());

            out.println("server info: " + ctx.getServerInfo());
            out.println("context path: " + ctx.getContextPath());
            out.println("real path of tei-numbers.xslt: " + ctx.getRealPath("tei-numbers.xslt"));
            out.println("resource paths: ");
            for (Object s : ctx.getResourcePaths("/"))
            {
                out.println("  "+s.toString());
            }
            out.println("----");
        }
    }
}

