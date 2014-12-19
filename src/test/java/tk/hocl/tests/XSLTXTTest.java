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

    /** Test that XSLTXT -> XSLT conversions aren't too slow.
     * "Too slow" is more than one hundred times what it takes to just read the XML file in.
     * If this test fails, just run it again
     */
    @Test
    public void testConvertTime ()
    {
        int number_of_iterations = 20;
        long t0, t1;
        long conversion_times_sum = 0;
        long xml_read_times_sum = 0;
        byte[] buffer = new byte[2048];
        try
        {
            for (int i = 0; i < number_of_iterations; i++)
            {
                /* Time the xsltxt convert+read */
                {
                    t0 = System.currentTimeMillis();
                    XSLTXTInputStream txt_is = new XSLTXTInputStream(getResourceStream("tei-html.xsltxt"));
                    while (txt_is.read(buffer) != -1) {};
                    t1 = System.currentTimeMillis();
                    conversion_times_sum += (t1-t0);
                }

                /* Time the xml read */
                {
                    t0 = System.currentTimeMillis();
                    InputStream xml_is = getResourceStream("tei-html.xslt");
                    while (xml_is.read(buffer) != -1) {};
                    t1 = System.currentTimeMillis();
                    xml_read_times_sum += (t1-t0);
                }
            }
            System.out.println("xml_read_times_sum = "+xml_read_times_sum);
            System.out.println("conversion_times_sum = "+conversion_times_sum);
            assertTrue(xml_read_times_sum * 100 > conversion_times_sum);
        }
        catch (SyntaxException e)
        {
            System.out.println("Couldn't complete the test (bad syntax in xsltxt):");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            System.out.println("Couldn't complete the test:");
            e.printStackTrace();
        }

    }

    private boolean test_transformation (String stylesheetFile, String sourceFile)
    {
        return test_transformation0(stylesheetFile, sourceFile, sourceFile +".expected");
    }
}
