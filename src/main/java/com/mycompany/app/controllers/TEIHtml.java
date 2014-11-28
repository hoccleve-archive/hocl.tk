package com.mycompany.app.controllers;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.mycompany.app.controllers.XSLTTransformer;

public class TEIHtml extends XSLTTransformer
{
    @Override
    public void init ()
    {
        xsltResourceName = "tei-html.xslt";
        contentType = "text/html";
    }
}

