<%
    response.setContentType("application/xml");
    String xml = request.getParameter("q");
    String xsl = request.getParameter("xsl");
    String type = "xml";
    if (xml != null || xsl != null)
    {
        try
        {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Transform.doTransformation(xsl, xml, os);
            byte[] result = os.toByteArray();
            for (byte x : result)
            {
                out.write(x);
            }
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }
    /*
    PipedInputStream is = new PipedInputStream();
    PipedOutputStream os = new PipedOutputStream();
    setPipe(is, os);
    InputStream xml_is = Util.openURL(xml);
    */
%>
<%--Note: This has to come at the end or firefox at least complains about the whitespace before the xml declaration--%>
<%@ page import="util.Util" %>
<%@ page import="controllers.Transform" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
