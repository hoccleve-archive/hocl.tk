package com.mycompany.app.controllers;

import com.mycompany.app.controllers.XSLTTransformer;

/** Transforms a CTable into HTML.
 */
public class CTableHTML extends XSLTTransformer
{

    {
        xsltResourceName = "ctable-html.xslt";
        contentType = "text/html";
    }

}

