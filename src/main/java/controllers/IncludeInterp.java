package controllers;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import controllers.*;

@WebServlet("/include-interp")
public class IncludeInterp extends XSLTTransformer
{
    @Override
    public void init ()
    {
        xsltResourceName = "/include-interp.xslt";
        contentType = "text/xml";
        params.put("ana", "");
    }
}

