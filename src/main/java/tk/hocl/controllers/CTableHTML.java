package tk.hocl.controllers;

import tk.hocl.controllers.XSLTTransformer;

/** Transforms a CTable into HTML.
 */
public class CTableHTML extends XSLTTransformer
{
    {
        xsltResourceName = "ctable-html.xslt";
        contentType = "text/html";
    }
}

