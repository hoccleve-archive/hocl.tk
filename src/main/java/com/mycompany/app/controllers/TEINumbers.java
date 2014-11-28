package com.mycompany.app.controllers;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.mycompany.app.controllers.XSLTTransformer;

/* TODO: Make automatic mappings like this based on
 * the names of xslt files
 */
public class TEINumbers extends XSLTTransformer
{
    @Override
    public void init ()
    {
        xsltResourceName = "tei-numbers.xslt";
    }
}
