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
    private PipedReader _realIn = new PipedReader();

    public XSLTXTInputStream (InputStream is) throws IOException, SyntaxException
    {
        super();
        _out.connect(_realIn);

        InputStreamReader r = new InputStreamReader(is);
        Outputter out = new Outputter(new PrintWriter(_out));
        out.output("<?xml version=\"1.0\"?>");
        // Create a Lexer to read the input file via the reader. The
        // name of the file is used in error messages so that you know
        // where the problem is.
        Lexer lex = new Lexer(r, "MEMORY");

        // Calling getStatement on a StatementFactory instance reads
        // the first statement from the lexer and all of the
        // statements it contains. So, it reads the whole file for a
        // well constructed file.

        Statement stat = StatementFactory.getInstance().getStatement(lex);
        out.newline();
        stat.outputXML(out);
        // Flush to make sure all is written.
        out.flush();
        _out.close();
    }

    @Override
    public int read() throws IOException
    {
        return _realIn.read();
    }
}
