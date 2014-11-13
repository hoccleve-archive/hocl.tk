package util;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.custommonkey.xmlunit.*;

import java.net.URL;
import controllers.Transform;
import static util.Util.*;

public class TestUtil
{
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
            e.exc.printStackTrace();
            return false;
        }

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
}

