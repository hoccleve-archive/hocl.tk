package controllers;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import controllers.*;

/* TODO: Make automatic mappings like this based on
 * the names of xslt files
 */
@WebServlet("/tei-line-numbers")
public class TEI extends XSLTTransformer
{
    static {
        xsltResourceName = "/tei-numbers.xslt";
    }
}
