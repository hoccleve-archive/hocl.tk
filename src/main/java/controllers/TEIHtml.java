package controllers;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import controllers.*;

@WebServlet("/tei-html")
public class TEIHtml extends XSLTTransformer
{
    @Override
    public void init ()
    {
        xsltResourceName = "/tei-html.xslt";
        contentType = "application/xhtml+xml";
    }
}

