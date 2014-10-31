package controllers;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import util.Util;

public class Transform
{
    static TransformerFactory tf = TransformerFactory.newInstance();
    public String transform()
    {
        return "";
    }

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

    public static Source getInputFromURL(String url)
    {
        return getInput(Util.openURL(url));
    }

    public static Source getInput(InputStream is)
    {
        return new StreamSource(is);
    }

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

    public static Result getOutput(OutputStream os)
    {
        return new StreamResult(os);
    }

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
            e.printStackTrace();
        }
        return res;
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
            e.printStackTrace();
        }
        return res;
    }

    public static void doTransformation(Transformer t, Source in, Result out)
    {
        try
        {
            t.transform(in, out);
        }
        catch (TransformerException e)
        {
            System.out.println("Transformation failed");
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

    public static void doTransformation(String t, InputStream in, OutputStream out)
    {
        Transformer tf = getTransformerFromURL(t);
        doTransformation(tf, getInput(in), getOutput(out));
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
