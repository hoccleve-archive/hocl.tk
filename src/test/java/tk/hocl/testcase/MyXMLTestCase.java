package tk.hocl.testcase;
import static tk.hocl.util.Util.*;
import static tk.hocl.util.TestUtil.*;
import org.xml.sax.SAXException;
import tk.hocl.controllers.Transform;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.exceptions.XpathException;
import java.util.Map;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import org.junit.Ignore;

@Ignore
public class MyXMLTestCase extends XMLTestCase
{
    public void assertXPath(String expected, String path, String xml)
    {
        try
        {
            assertXpathEvaluatesTo(expected, path, xml);
        }
        catch (XpathException|SAXException|IOException e)
        {
            System.err.println("The test failed with an exception: ");
            e.printStackTrace();
            fail();
        }
    }

    public void assertXPath(String expected, String path, ByteArrayOutputStream xml)
    {
        assertXPath(expected, path, xml.toString());
    }

    public void assertTransformXPath(String source, String xslt, String expected, String path)
    {
        assertTransformXPath(source, xslt, expected, null);
    }

    public void assertTransformXPath(String source, String xslt, String expected, String path, Map<String, Object> params)
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String stylesheet_file = getResource(xslt).toString();
        if (params != null)
        {
            Transform.doTransformation(stylesheet_file, getResourceStream(source), os, params);
        }
        else
        {
            Transform.doTransformation(stylesheet_file, getResourceStream(source), os);
        }

        assertXPath(expected, path, os.toString());
    }
}

