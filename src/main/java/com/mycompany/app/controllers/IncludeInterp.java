package com.mycompany.app.controllers;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.mycompany.app.controllers.XSLTTransformer;

/** Adds analysis to a document.
 *
 * The analysis text to add is provided by the 'ana' parameter and must be a URL.
 */
public class IncludeInterp extends XSLTTransformer
{
    @Override
    public void init ()
    {
        xsltResourceName = "include-interp.xslt";
        contentType = "text/xml";
        params.put("ana", "");
    }
}

