package tests;
import org.junit.*; // Test, Before, After, etc.
import static org.junit.Assert.*; // test assertions
import static util.TestUtil.*;

public class XSLTest
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

    //@Test
    public void testAnalysisToHTML ()

    {
        String[] xslts = new String[] { "tei-numbers.xslt", "tei-html.xslt"};
        assertTrue("Simple", test_transformation0(xslts, "reg+interp.xml", "reg+interp-html.xml"));
    }

    private boolean test_transformation (String stylesheetFile, String sourceFile)
    {
        return test_transformation0(stylesheetFile, sourceFile, sourceFile +".expected");
    }
}
