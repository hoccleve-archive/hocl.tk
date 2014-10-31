<%@ page import="util.Util" %>
<%@ page import="controllers.Transform" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<html>
    <body>
        <h2>Hello World!</h2>
        <%
            String xml = request.getParameter("q");
            String xsl = request.getParameter("xsl");
            String type = "xml";
            try
            {
                out.println(xml);
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
            /*
            PipedInputStream is = new PipedInputStream();
            PipedOutputStream os = new PipedOutputStream();
            setPipe(is, os);
            InputStream xml_is = Util.openURL(xml);
            response().setContentType("text/xml");
            */
        %>
    </body>
</html>
