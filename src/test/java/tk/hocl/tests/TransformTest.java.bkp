package com.mycompany.app.tests;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.*; // Test, Before, After, etc.
import static org.junit.Assert.*; // test assertions
import static com.mycompany.app.util.Util.*;
import static com.mycompany.app.util.TestUtil.*;
import org.xml.sax.SAXException;
import com.mycompany.app.controllers.Transform;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.exceptions.XpathException;
import com.mycompany.app.testcase.MyXMLTestCase;

public class TransformTest extends MyXMLTestCase
{
    /** This test suite is for testing the Transform module.
     *
     * This test suite is NOT for testing stylesheets that Transform operates on.
     */
    @Test
    public void testMultipleTransformations ()
    {
        /* performs the transformation, which adds an n to /a/b, twice */
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String stylesheet_file = getResource("simple1.xslt").toString();
        Transform.doTransformation(new String[] {stylesheet_file, stylesheet_file, stylesheet_file}, getResourceStream("simple1.xml"), os);
        assertXPath("nnn", "/a/b", os.toString());
    }


}
