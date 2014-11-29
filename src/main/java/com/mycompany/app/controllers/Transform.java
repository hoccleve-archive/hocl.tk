package com.mycompany.app.controllers;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Writer;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.ErrorListener;

import com.mycompany.app.util.Util;

/** A utility class for performing XSL transformations */
public class Transform
{
    static SAXTransformerFactory tf;
    static ErrorListener errors = new MyErrorListener();

    static {
        TransformerFactory tf_instance = TransformerFactory.newInstance();
        if (tf_instance.getFeature(SAXTransformerFactory.FEATURE))
        {
            tf = (SAXTransformerFactory)tf_instance;
        }

        tf.setErrorListener(errors);
    };

    /** A wrapper around a checked exception, TransformerException,
     * so it can pass through the to top level methods.
     */
    public static class MyTransformerException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        public TransformerException exc;
        public MyTransformerException (TransformerException tf)
        {
            super();
            exc = tf;
        }
    }

    public static class MyErrorListener implements ErrorListener
    {
        public void fatalError(TransformerException exception) throws TransformerException
        {
            throw exception;
        }

        public void warning(TransformerException exception) throws TransformerException
        {
            System.err.println("transformation warning: ");
            exception.printStackTrace();
        }

        public void error(TransformerException exception)
        {
            System.err.println("transformation error: ");
            exception.printStackTrace();
        }
    }

    /** Creates a Source from the named file.
     * @param fileName The absolute name of the file containing the resource
     * @see Transform#getInput(InputStream)
     */
    public static Source getInput(String fileName)
    {
        Source res = null;
        try
        {
            res = getInput(new FileInputStream(fileName));
        }
        catch (FileNotFoundException e)
        {/* fall through */}
        return res;
    }

    /** Creates a Source by opening a connection to the provided url
     * @param url The URL to the intended resource. Must be not be a relative path.
     * @see Transform#getInput(InputStream)
     */
    public static Source getInputFromURL(String url)
    {
        return getInput(Util.openURL(url));
    }

    /** Returns a Source suitable for passing to {@link Transform#doTransformation(Transformer,Source,Result)}. */
    public static Source getInput(InputStream is)
    {
        return new StreamSource(is);
    }

    /** Creates a Result from the named file
     * @param fileName The absolute name of the file containing the resource
     * @see Transform#getOutput(OutputStream)
     */
    public static Result getOutput(String fileName)
    {
        Result res = null;
        try
        {
            res = getOutput(new FileOutputStream(fileName));
        }
        catch (FileNotFoundException e)
        {/* fall through */}
        return res;
    }

    /** Returns a Result suitable for passing to {@link Transform#doTransformation(Transformer,Source,Result)}. */
    public static Result getOutput(OutputStream os)
    {
        return new StreamResult(os);
    }

    /**
     * @see Transform#getOutput(OutputStream)
     */
    public static Result getOutput(Writer w)
    {
        return new StreamResult(w);
    }

    /** Creates a Transformer from the XSLT at the provided URL
     * @param url The URL pointing to an XSLT resource
     * @see Transform#doTransformation(Transformer,Source,Result)
     */
    public static Transformer getTransformerFromURL(String url)
    {
        Source xslt_in = getInput(Util.openURL(url));
        Transformer res = null;
        try
        {
            res = tf.newTransformer(xslt_in);
        }
        catch (TransformerConfigurationException e)
        {
            throw new MyTransformerException(e);
        }
        return res;
    }

    public static Templates getTemplatesFromURL(String url)
    {
        Source xslt_in = getInput(Util.openURL(url));
        Templates res = null;
        try
        {
            res = tf.newTemplates(xslt_in);
        }
        catch (TransformerConfigurationException e)
        {
            throw new MyTransformerException(e);
        }
        return res;
    }


    public static Transformer getTransformer(InputStream in)
    {
        Source xslt_in = getInput(in);
        return getTransformer(xslt_in);
    }

    public static Transformer getTransformer(String fileName)
    {
        Source xslt_in = getInput(fileName);
        return getTransformer(xslt_in);
    }

    public static Transformer getTransformer(Source in)
    {
        Transformer res = null;
        try
        {
            res = tf.newTransformer(in);
        }
        catch (TransformerConfigurationException e)
        {
            throw new MyTransformerException(e);
        }
        return res;
    }

    /** Performs an XSL transformation.
     *
     * The transformation is described by t and takes in to out. The params provided are passed on
     * to the transformation.
     */
    public static void doTransformation(Transformer t, Source in, Result out, Map<String,Object> params)
    {
        for (String p : params.keySet())
        {
            t.setParameter(p, params.get(p));
        }
        doTransformation(t, in, out);
    }

    /** Performs an XSL transformation.
     *
     * The transformation is described by t and takes in to out.
     * @see Transform#doTransformation(Transformer,Source,Result,Map)
     */
    public static void doTransformation(Transformer t, Source in, Result out)
    {
        try
        {
            t.transform(in, out);
        }
        catch (TransformerException e)
        {
            throw new MyTransformerException(e);
        }
    }

    public static void doTransformation(Transformer t, InputStream in, Result out)
    {
        doTransformation(t, getInput(in), out);
    }

    public static void doTransformation(Transformer t, InputStream in, OutputStream out)
    {
        doTransformation(t, getInput(in), getOutput(out));
    }

    /** Performs a transformation using the stylesheet provided.
     * @param t The URL of the stylesheet
     * @see Transform#doTransformation(Transformer,Source,Result,Map)
     */
    public static void doTransformation(String t, InputStream in, OutputStream out, Map<String, Object> params)
    {
        Transformer tf = getTransformerFromURL(t);
        doTransformation(tf, getInput(in), getOutput(out), params);
    }

    /** Performs a transformation using the stylesheet provided.
     * @param t The URL of the stylesheet
     * @see Transform#doTransformation(Transformer,Source,Result)
     */
    public static void doTransformation(String t, InputStream in, OutputStream out)
    {
        Transformer tf = getTransformerFromURL(t);
        doTransformation(tf, getInput(in), getOutput(out));
    }

    /** Performs a transformation using the stylesheet provided.
     * @param xslt The URL of the stylesheet
     * @param in The URL of the source document
     * @param out A writer for the result
     * @see Transform#doTransformation(Transformer,Source,Result)
     */
    public static void doTransformation(String xslt, String in, Writer out)
    {
        Source xml_in = getInput(Util.openURL(in));
        Result xml_out = getOutput(out);
        Transformer tf = getTransformerFromURL(xslt);
        doTransformation(tf, xml_in, xml_out);
    }

    /** Performs a transformation using the stylesheet provided.
     * @param xslt The URL of the stylesheet
     * @param in A stream for the source document
     * @param out A writer for the result
     * @see Transform#doTransformation(Transformer,Source,Result,Map)
     */
    public static void doTransformation(String xslt, InputStream in, Writer out, Map<String,Object> params)
    {
        Source xml_in = getInput(in);
        Result xml_out = getOutput(out);
        Transformer tf = getTransformerFromURL(xslt);
        doTransformation(tf, xml_in, xml_out, params);
    }

    public static void doTransformation(String xslt, InputStream in, Writer out)
    {
        Source xml_in = getInput(in);
        Result xml_out = getOutput(out);
        Transformer tf = getTransformerFromURL(xslt);
        doTransformation(tf, xml_in, xml_out);
    }

    /** Perform a series of transformations.
     * @param xslts The URLs of the transformations to perform
     */
    public static void doTransformation(String[] xslts, InputStream in, OutputStream out)
    {
        Templates[] ts = new Templates[xslts.length];
        for (int i = 0; i < xslts.length; i++)
        {
            ts[i] = getTemplatesFromURL(xslts[i]);
        }
        doTransformation(ts, getInput(in), getOutput(out));
    }


    /** Perform a series of transformations.
     * @param ts The templates of the transformations to perform
     */
    public static void doTransformation(Templates[] ts, Source in, Result out)
    {
        /* This loop performs 1 or more transformations given in the array `tfs` */
        if (ts.length == 1)
        {
            try
            {
                doTransformation(ts[0].newTransformer(), in, out);
            }
            catch (TransformerConfigurationException e)
            {
                throw new MyTransformerException(e);
            }
        }
        else
        {
            TransformerHandler[] handlers = new TransformerHandler[ts.length];

            try
            {

                for (int i = 0; i < ts.length; i++)
                {
                    handlers[i] = tf.newTransformerHandler(ts[i]);
                }

                for (int i = 0; i < ts.length - 1; i++)
                {
                    handlers[i].setResult(new SAXResult(handlers[i+1]));
                }
                handlers[ts.length - 1].setResult(out);

                Transformer t = tf.newTransformer();
                t.transform(in, new SAXResult(handlers[0]));
            }
            catch (TransformerConfigurationException e)
            {
                System.err.println("Error creating templates");
                e.printStackTrace();
            }
            catch (TransformerException e)
            {
                System.err.println("Error transforming");
                e.printStackTrace();
            }

        }
    }

    /** Perform a transformation
     * @param xslt A stream of the XSLT for the transformation
     */
    public static void doTransformation(InputStream xslt, InputStream in, OutputStream out)
    {
        Source xml_in = getInput(in);
        Result xml_out = getOutput(out);
        Transformer tf = getTransformer(xslt);
        doTransformation(tf, xml_in, xml_out);
    }

    /** Takes the URI of the xslt and input xml and writes to the OutputStream.
    */
    public static void doTransformation(String xslt, String in, OutputStream out)
    {
        Source xml_in = getInput(Util.openURL(in));
        Result xml_out = getOutput(out);
        Transformer tf = getTransformerFromURL(xslt);
        doTransformation(tf, xml_in, xml_out);
    }

    public static void main (String [] args)
    {
        String xslt = args[0];
        String fname = args[1];
        Source xml_in = getInput(fname);
        Result xml_out = getOutput(fname + ".out");
        Transformer tf = getTransformer(xslt);
        doTransformation(tf, xml_in, xml_out);
    }
}
