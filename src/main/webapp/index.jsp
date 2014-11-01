<html>
    <head>
        <link href="main.css" rel=stylesheet type="text/css">
        <title>Demo page</title>
    </head>
    <body>
        <form action="transform.jsp" method="get" id="main-xsl-input" class="input-box">
            <label for="xsl-url">xslt</label> <input id="xsl-url" value="http://localhost/ctable.xslt" type="text" name="xsl"><br/>
            <label for="xml-url">xml</label> <input id="xml-url" value="http://localhost/reg.xml" type="text" name="q"><br/>
            <input type="submit" value="Send">
        </form>
        <ul>
            <% 
            final File folder = new File("/var/lib/tomcat7/webapps/ROOT");
            for (final File fileEntry : folder.listFiles()) {

                if (!fileEntry.isDirectory())
                {
                    String s = fileEntry.getName();
                    if (s.matches(".*\\.xslt|.*\\.xml"))
                    {
                        %>
                        <li>
                            <%= s %>
                        </li>
                        <%
                    }
                }
            }
            %>
        </ul>
    </body>
</html>
<%@ page import="java.io.*" %>
