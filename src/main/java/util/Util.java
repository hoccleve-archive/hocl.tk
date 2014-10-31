package util;

import java.io.InputStream;
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

}
