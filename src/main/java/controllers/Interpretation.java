package controllers;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import controllers.*;

/** This annotates a text with an interpretation.
 * This transformation augments a TEI text with an associated TEI interpretation.
 * The interpretive markup supported includes span elements with notes.
 * Please see the XSLT documents for more precise information.
 *
 */
@WebServlet("/annotate")
public class Interpretation extends XSLTTransformer
{
    @Override
    public void init ()
    {
        xsltResourceName = "/annotate.xslt";
    }
}
