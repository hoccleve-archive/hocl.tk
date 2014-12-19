package tk.hocl.util;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.io.PipedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.zanthan.xsltxt.Outputter;
import com.zanthan.xsltxt.Statement;
import com.zanthan.xsltxt.StatementFactory;
import com.zanthan.xsltxt.Lexer;
import com.zanthan.xsltxt.exception.SyntaxException;

/** A simple configuration class.
 *
 * Reads configuration from a "properties" file and exposes it with some transformations to Java data types
 */
public class XSLTXTInputStream extends InputStream
{
    /** Where XSLTXT code writes to */
    private PipedWriter _out = new PipedWriter();
    /** For the implementation of read() */
    private Producer<PipedReader> _realIn;

    public XSLTXTInputStream (InputStream is) throws IOException, SyntaxException
    {
        super();

        InputStreamReader r = new InputStreamReader(is);
        Lexer lex = new Lexer(r, "MEMORY");
        final Statement stat = StatementFactory.getInstance().getStatement(lex);

        PipedReader reader = new PipedReader();
        _out.connect(reader);
        _realIn = new Producer<PipedReader> (reader)
        {
            public void real_run () throws SyntaxException, IOException
            {
                Outputter out = new Outputter(new PrintWriter(_out));
                out.output("<?xml version=\"1.0\"?>");
                out.newline();
                stat.outputXML(out);
                // Flush to make sure all is written.
                out.flush();
                _out.close();
            }
        };
        _realIn.start();
    }

    @Override
    public int read() throws IOException
    {
        try
        {
            return _realIn.get().read();
        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
    }

    public static abstract class Producer<T> extends Thread
    {
        T _item;
        Exception _exc = null;

        public Producer(T i)
        {
            _item = i;
        }

        public abstract void real_run () throws Exception;

        public void run ()
        {
            try
            {
                real_run();
            }
            catch (Exception e)
            {
                _exc = e;
            }
        }

        public T get () throws Exception
        {
            if (_exc != null)
            {
                throw _exc;
            }
            else
            {
                return _item;
            }
        }
    }
}
