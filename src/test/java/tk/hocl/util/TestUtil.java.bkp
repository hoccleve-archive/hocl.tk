package com.mycompany.app.util;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.custommonkey.xmlunit.*;

import java.net.URL;
import com.mycompany.app.controllers.Transform;
import static com.mycompany.app.util.Util.*;

public class TestUtil
{
    static {
        /* Set features for XMLUnit document parsing */
        DocumentBuilderFactory[] facs = new DocumentBuilderFactory[] {XMLUnit.getControlDocumentBuilderFactory(),
            XMLUnit.getTestDocumentBuilderFactory()};
        XMLUnit.setIgnoreWhitespace(true);
        /* Turn off validation */
        for (DocumentBuilderFactory fac: facs)
        {
            try
            {
                fac.setValidating(false);
                fac.setFeature("http://xml.org/sax/features/validation", false);
                fac.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
                fac.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            }
            catch (ParserConfigurationException e)
            {
                System.err.println("Couldn't disable validation:");
                e.printStackTrace();
            }
        }
    };

    public static boolean test_transformation (String stylesheetFile, String sourceFile)
    {
        return test_transformation0(stylesheetFile, sourceFile, sourceFile +".expected");
    }

    public static boolean test_transformation0 (String stylesheetFile, String sourceFile, String expectedFile)
    {
        return test_transformation0(new String[] {stylesheetFile}, sourceFile, expectedFile);
    }

    public static boolean test_transformation0 (String[] stylesheetFiles, String sourceFile, String expectedFile)
    {
        /** Tests if a transformation succeeds or not
        */
        InputStream source = getResourceStream(sourceFile);
        InputStream expected = getResourceStream(expectedFile);

        ByteArrayOutputStream transformResult = new ByteArrayOutputStream();
        for (int i = 0; i < stylesheetFiles.length; i++)
        {
            stylesheetFiles[i] = getResource(stylesheetFiles[i]).toString();
        }

        try
        {
            Transform.doTransformation(stylesheetFiles, source, transformResult);
        }
        catch (Transform.MyTransformerException e)
        {
            System.err.println("Got a transformation error: ");
            e.exc.printStackTrace();
            return false;
        }

        InputStream result = copyOutputByteStreamToInput(transformResult);
        boolean res = simpleDiff(expected, result);
        if ( res )
        {
            return true;
        }
        else
        {
            System.out.println("RESULT OF TRANSFORMATION:\n"+ transformResult);

            try
            {
                InputStream expected2 = getResourceStream(expectedFile);
                System.out.println("EXPECTED:\n");
                int r = 0;
                while ((r = expected2.read()) != -1)
                {
                    System.out.write(r);
                }
            }
            catch (IOException e)
            {
                System.out.println("ERROR in PRINTING");
            }
            return false;
        }
    }

    public static boolean simpleDiff(InputStream a, InputStream b)
    {
    /* Returns whether the XML from a and b are similar.
     *
     * Prints the diff as a side effect */
        boolean diffStatus = false;
        try
        {
            Diff myDiff = new Diff(new InputSource(a), new InputSource(b));
            diffStatus = myDiff.similar();
            if ( !diffStatus )
            {
                System.err.println(myDiff.toString());
            }
        }
        catch (SAXException|IOException e)
        {
            e.printStackTrace();
            diffStatus = false;
        }
        return diffStatus;
    }
}

