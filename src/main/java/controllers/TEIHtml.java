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
    static {
        xsltResourceName = "/tei-poem.xslt";
    }
}

