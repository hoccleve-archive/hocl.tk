package com.mycompany.app.tests;
import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import org.junit.*; // Test, Before, After, etc.
import static org.junit.Assert.*; // test assertions
import static com.mycompany.app.util.Util.*;
import static com.mycompany.app.util.TestUtil.*;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.exceptions.XpathException;
import com.mycompany.app.testcase.MyXMLTestCase;

public class XSLTest extends MyXMLTestCase
{
    /** This test suite is for testing stylesheets (XSLT files).
     *
     * This test suite is NOT for testing the functionality of the Transform module
     */

    @Test
    public void testLineNumbers1 ()
    {
        assertTrue("line numbers correct", test_transformation0("tei-numbers.xslt", "reg+interp.xml", "reg+interp-numbers.xml"));
    }

    @Test
    public void testLineNumbers2 ()
    {
        assertTrue("line numbers correct on a TEI fragment (only lg and l elements)",
                test_transformation0("tei-numbers.xslt", "short-poem.xml", "short-poem-numbers.xml"));
    }

    @Test
    public void testIncludeDocument ()
    {
        String doc = "include-doc.xml";
        String parent = "parent-doc.xml";
        Map<String, Object> params = new HashMap<String, Object> ();
        params.put("ana", getResource(doc));
        //assertTransformXPath(parent, "include-interp.xslt", "Something", "/TEI/text/body/p/ab", params);
        assertTransformXPath(parent, "include-interp.xslt", "Test parent", "/TEI/teiHeader/fileDesc/titleStmt/title", params);
    }

    @Test
    public void testReferenceSibling ()
    {
        /** Learning test.
         * See if we can join elements by an id value
         */
        assertTrue("ID reference", test_transformation0("multipass.xslt", "multipass.xml", "multipass-out.xml"));
    }

    private boolean test_transformation (String stylesheetFile, String sourceFile)
    {
        return test_transformation0(stylesheetFile, sourceFile, sourceFile +".expected");
    }
}
