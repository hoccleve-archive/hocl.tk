package com.mycompany.app.util;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;

public class Util {

    public static InputStream openURL(String url) {
        URL urlObject;
        InputStream res = null;
        try {
            urlObject = new URL(url);
            URLConnection conn = urlObject.openConnection();
            res = conn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static InputStream getResourceStream (String name)
    {
        /** Resources are those stored in the class path (e.g., src/main/resources). */
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream file = loader.getResourceAsStream(name);
        return file;
    }

    public static URL getResource (String name)
    {
        /** Resources are those stored in the class path (e.g., src/main/resources). */
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResource(name);
    }

    public static ByteArrayInputStream copyOutputByteStreamToInput(ByteArrayOutputStream os)
    {
        return new ByteArrayInputStream(os.toByteArray());
    }


}
