<%
    response.setContentType("application/xml");
    String xml = request.getParameter("q");
    String xsl = request.getParameter("xsl");
    String type = "xml";
    Transform.doTransformation(xsl, xml, out);
%>
<%--NOTE: This has to come at the end or firefox at least complains about the whitespace before the xml declaration--%>
<%@ page import="util.Util" %>
<%@ page import="controllers.Transform" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
