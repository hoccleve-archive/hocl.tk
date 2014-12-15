package tk.hocl.tests;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import org.junit.*; // Test, Before, After, etc.
import static org.junit.Assert.*; // test assertions
import static tk.hocl.util.Util.*;
import tk.hocl.util.XSLTXTInputStream;
import static tk.hocl.util.TestUtil.*;
import tk.hocl.controllers.Transform;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.custommonkey.xmlunit.jaxp13.Validator;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;
import javax.xml.XMLConstants;
import tk.hocl.testcase.MyXMLTestCase;

import com.zanthan.xsltxt.exception.SyntaxException;

public class XSLTXTTest extends MyXMLTestCase
{
    /** This test suite is for testing stylesheets (XSLT files).
     *
     * This test suite is NOT for testing the functionality of the Transform module
     */


    /** Test that we can do a transformation with a xsltxt file */
    @Test
    public void testBasicConvert ()
    {
        int ch;
        try
        {
            XSLTXTInputStream is = new XSLTXTInputStream(getResourceStream("simple1.xsltxt"));
            InputStream xml_is = getResourceStream("simple1.xslt");
            assertTrue(simpleDiff(xml_is, is));
        }
        catch (IOException e)
        {
            System.out.println("Couldn't open the xsltxt file:");
            e.printStackTrace();
        }
        catch (SyntaxException e)
        {
            System.out.println("Syntax error in xsltxt file:");
            e.printStackTrace();
        }
    }

    private boolean test_transformation (String stylesheetFile, String sourceFile)
    {
        return test_transformation0(stylesheetFile, sourceFile, sourceFile +".expected");
    }
}
