package tk.hocl.util;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/** A simple configuration class.
 *
 * Reads configuration from a "properties" file and exposes it with some transformations to Java data types
 */
public class Configuration
{
    /** The underlying Properties */
    private Properties _props = new Properties();

    /** The single configuration instance */
    private static Configuration theConf;

    static
    {
        theConf = new Configuration("conf/app.conf");
    }

    /** Get a configuration value */
    public static String C(String key)
    {
        return theConf.getValue(key);
    }
    /** Get a configuration value as an integer */
    public static Integer C_Integer(String key)
    {
        return new Integer(C(key));
    }

    /** Create the Configuration instance from a file name */
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

    /** Actually return a configuration value from the backing Properties */
    private String getValue(String key)
    {
        return _props.getProperty(key);
    }
}

