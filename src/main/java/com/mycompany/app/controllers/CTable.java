package com.mycompany.app.controllers;

import com.mycompany.app.controllers.XSLTTransformer;
import javax.servlet.http.HttpServletRequest;

/** Transforms a TEI document into an HTML page.
 *
 * If there is analysis included in the document text, then it may
 * be included in the HTML page. See the stylesheet for a description
 * of how the kinds of analysis text that will be interpreted.
 */
public class CTable extends XSLTTransformer
{

    {
        xsltResourceName = "ctable.xslt";
        contentType = "text/xml";
        params.put("subject", "");
        params.put("subjectDocument", "");
    }
}

