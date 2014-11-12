package tests;
import org.junit.*; // Test, Before, After, etc.
import static org.junit.Assert.*; // test assertions
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.net.URL;
import controllers.Transform;
//import net.sf.xmlunit.diff.*;
import org.custommonkey.xmlunit.*;

public class TransformTest
{
    /* Things to test:
     *   Transform functionality
     *   - Test XInclude processing
     *   Stylesheets
     *   - Test addition of line numbers to a TEI document
     *   - Test translation of internalized analytical markup
     *     into HTML
     */

    @Test
    public void testLineNumbers1 ()
    {
        assertTrue("line numbers correct", test_transformation("tei-numbers.xslt", "reg+interp.xml"));
    }

    private boolean test_transformation (String stylesheetFile, String sourceFile)
    {
        return test_transformation0(stylesheetFile, sourceFile, sourceFile +".expected");
    }

    private boolean test_transformation0 (String stylesheetFile, String sourceFile, String expectedFile)
    {
        /** Tests if a transformation succeeds or not
        */
        InputStream source = getResourceStream(sourceFile);
        InputStream expected = getResourceStream(expectedFile);
        InputStream stylesheet = getResourceStream(stylesheetFile);

        ByteArrayOutputStream transformResult = new ByteArrayOutputStream();
        Transform.doTransformation(stylesheet, source, transformResult);

        InputStream result = copyOutputByteStreamToInput(transformResult);

        InputSource is = new InputSource(expected);
        InputSource resultSource = new InputSource(result);

        boolean diffStatus = false;
        try
        {
            Diff myDiff = new Diff(is, resultSource);
            diffStatus = myDiff.similar();
        }
        catch (SAXException|IOException e)
        {
            e.printStackTrace();
            diffStatus = false;
        }
        return diffStatus;
    }

    private InputStream getResourceStream (String name)
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream file = loader.getResourceAsStream(name);
        return file;
    }

    private URL getResource (String name)
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResource(name);
    }

    private ByteArrayInputStream copyOutputByteStreamToInput(ByteArrayOutputStream os)
    {
        return new ByteArrayInputStream(os.toByteArray());
    }

}
