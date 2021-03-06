package tk.hocl.util;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;

public class Util {

    /** This is for opening URLs only provided by the programmer.
     * URLs provided by the user should be checked for errors and the
     * user notified. Only use this if you have a single response to
     * any non-content situation on opening a URL
     */
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

    /** Resources are those stored in the class path (e.g., src/main/resources). */
    public static InputStream getResourceStream (String name)
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResourceAsStream(name);
    }

    /** Resources are those stored in the class path (e.g., src/main/resources). */
    public static URL getResource (String name)
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResource(name);
    }

    public static ByteArrayInputStream copyOutputByteStreamToInput(ByteArrayOutputStream os)
    {
        return new ByteArrayInputStream(os.toByteArray());
    }


}
