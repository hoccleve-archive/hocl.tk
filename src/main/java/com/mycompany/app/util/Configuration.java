package com.mycompany.app.util;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Configuration
{
    private Properties _props = new Properties();
    private static Configuration theConf;

    static
    {
        theConf = new Configuration("conf/app.conf");
    }

    public static String C(String key)
    {
        return theConf.getValue(key);
    }
    public static Integer C_Integer(String key)
    {
        return new Integer(C(key));
    }

    private Configuration (String filename)
    {
        try
        {
            InputStream fs = Util.getResourceStream(filename);
            _props.load(fs);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Couldn't open the file: " + filename);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't load properties from the file: " + filename);
        }
    }

    private String getValue(String key)
    {
        return _props.getProperty(key);
    }
}

