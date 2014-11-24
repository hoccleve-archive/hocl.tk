package tests;

import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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

import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.xml.sax.SAXException;

import org.junit.*; // Test, Before, After, etc.
import static org.junit.Assert.*; // test assertions
import static util.Util.*;
import static util.TestUtil.*;

public class SAXTransformerFactoryTest extends XMLTestCase
{
    @Test
    public void test_basic ()
    {
        SAXTransformerFactory stf = (SAXTransformerFactory) TransformerFactory.newInstance();
        try
        {
            Templates templates1 = stf.newTemplates(new StreamSource(getResourceStream("simple2.xslt")));
            Templates templates2 = stf.newTemplates(new StreamSource(getResourceStream("simple2.xslt")));

            TransformerHandler th1 = stf.newTransformerHandler(templates1);
            TransformerHandler th2 = stf.newTransformerHandler(templates2);
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            th1.setResult(new SAXResult(th2));
            th2.setResult(new StreamResult(os));

            Transformer t = stf.newTransformer();
            t.transform(new StreamSource(getResourceStream("simple1.xml")), new SAXResult(th1));
            try
            {
                assertXpathEvaluatesTo("nn", "/a/b", os.toString());
            }
            catch (XpathException|SAXException|IOException e)
            {
                System.err.println("The test failed with an exception: ");
                e.printStackTrace();
                fail();
            }
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

    @Test
    public void test_large ()
    {
        SAXTransformerFactory stf = (SAXTransformerFactory) TransformerFactory.newInstance();
        try
        {
            Templates templates1 = stf.newTemplates(new StreamSource(getResourceStream("large1.1.xslt")));
            Templates templates2 = stf.newTemplates(new StreamSource(getResourceStream("large1.2.xslt")));

            TransformerHandler th1 = stf.newTransformerHandler(templates1);
            TransformerHandler th2 = stf.newTransformerHandler(templates2);
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            th1.setResult(new SAXResult(th2));
            th2.setResult(new StreamResult(os));

            Transformer t = stf.newTransformer();
            t.transform(new StreamSource(getResourceStream("large1.1.xml")), new SAXResult(th1));


            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            assertTrue("The things are the same", simpleDiff(getResourceStream("large1.2.xml"), is));

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

