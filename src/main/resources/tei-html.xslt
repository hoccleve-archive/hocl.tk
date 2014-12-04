<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:tei="http://www.tei-c.org/ns/1.0"  
    xmlns:extra="http://hocl.tk/extra/"
    xmlns:xi="http://www.w3.org/2001/XInclude"
    >
    <!--
       - There *is* a TEI->HTML XSLT-based converter provided by TEI-C, but it
       - basically just wraps everything in divs. It doesn't support line numbers
       - out of the box. It isn't any prettier...
       -->

    <!--The interval between numbered lines-->
   <xsl:output method="html" />
    <xsl:param name="line_number_interval">1</xsl:param>

    <xsl:template match="/">
        <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html></xsl:text>
        <xsl:apply-templates select="node()"/>
    </xsl:template>

    <xsl:template match="/tei:TEI">
        <html>
            <head>
                <title>
                    <xsl:apply-templates select="tei:teiHeader/tei:fileDesc/tei:titleStmt/tei:title/text()" />
                </title>
                <script src="jquery-2.1.1.js"></script>
                <script src="resources/tei-html.js"></script>
                <link href="resources/tei-html.css" rel="stylesheet" type="text/css" />
                <!-- TODO: Put this in another transform and find out how to do XInclude processing in Java
                   -<xi:include href="html-head-includes"/>
                   -->
            </head>
            <body>
                <xsl:for-each select="tei:text/tei:body">
                    <h1><xsl:value-of select="tei:head/text()" /></h1>
                    <xsl:for-each select="tei:div[@type='poem']">
                        <xsl:call-template name="poem" />
                    </xsl:for-each>
                </xsl:for-each>
                <xsl:for-each select="//tei:spanGrp">
                    <div>
                        <xsl:attribute name="class" >sidebar <xsl:value-of select="@type" /></xsl:attribute>
                    </div>
                </xsl:for-each>
                <script src="resources/tei-html.js"></script>
                <script>

                    $(document).ready(function ()
                    {
                        make_sidebar_notes();
                    });
        
                </script>
            </body>
        </html>
    </xsl:template>

    <xsl:template name="poem">
        <table class="poem">
            <!--This thead prevents the first heading in the poem from jumping up here-->
            <thead><tr><th></th><th></th>
                    <xsl:for-each select="//tei:spanGrp">
                        <th class="note">
                            <xsl:value-of select="@type" />
                        </th>
                    </xsl:for-each>
            </tr></thead>
            <xsl:for-each select="*">
                <xsl:choose>
                    <xsl:when test="self::tei:lg">
                        <xsl:call-template name="poem_lines" />
                    </xsl:when>
                    <xsl:when test="self::tei:label">
                        <xsl:call-template name="poem_label" />
                    </xsl:when>
                </xsl:choose>
            </xsl:for-each>
        </table>
    </xsl:template>
    <xsl:template match="tei:note">
        <div class="note"><xsl:value-of select="text()"/></div>
    </xsl:template>

    <xsl:template name="line_note">
        <xsl:param name="span">1</xsl:param>
        <xsl:param name="interp_class"></xsl:param>
        <td>
            <xsl:if test="$span > 1">
                <xsl:attribute name="rowspan">
                    <xsl:value-of select="$span" />
                </xsl:attribute>
            </xsl:if>
            <xsl:attribute name="class">
                <xsl:if test="$interp_class" ><xsl:value-of select="$interp_class" />.</xsl:if>
                <xsl:value-of select="@type" /> note</xsl:attribute>
            <xsl:apply-templates select="tei:note|text()" />
        </td>
    </xsl:template>
    <xsl:template name="poem_lines" >
        <tbody>
            <xsl:for-each select="tei:l">
                <xsl:variable name="line_id"
                    select="@xml:id"/>
                <!--TODO: Use XSLT keys for these IDs-->
                <xsl:variable name="line_id_ref"
                    select="concat('#', @xml:id)"/>
                <xsl:variable name="line_number" select="@n"/>
                <tr>
                    <xsl:if test="@xml:id">
                        <xsl:attribute name="id">
                            <xsl:value-of select="$line_id" />
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:attribute name="class">line</xsl:attribute>
                    <td>
                        <xsl:if test="(@n mod $line_number_interval) = 0">
                            <xsl:value-of select="@n" />]
                        </xsl:if>
                    </td>
                    <td>
                        <xsl:value-of select="text()" />
                        <xsl:if test="//tei:spanGrp/tei:span[@target=$line_id_ref or @from=$line_id_ref]">
                            <span style="color:red"> <b>*</b></span>
                        </xsl:if>
                </td>
                    <xsl:for-each select="//tei:spanGrp">
                        <xsl:variable name="interp_class" select="@type"/> 
                        <xsl:for-each select="tei:span[(@target=$line_id_ref) or (@from=$line_id_ref)]">
                            <xsl:choose>
                                <!--XXX:The id function doesn't work in Java, so you the line-* format is required.-->
                                <xsl:when test="@from=$line_id_ref">
                                    <!--<xsl:variable name="start_" select="substring-after(@from,'#')" />-->
                                    <!--<xsl:variable name="start_node" select="id($start_)" />-->
                                    <!--<xsl:variable name="start" select="$start_node/@n" />-->
                                    <xsl:variable name="start" select="substring-after(@from,'#line-')" />
                                    <xsl:variable name="end" select="substring-after(@to,'#line-')" />
                                    <!--<xsl:variable name="end_" select="substring-after(@to,'#')" />-->
                                    <!--<xsl:variable name="end_node" select="id($end_)" />-->
                                    <!--<xsl:variable name="end" select="$end_node/@n" />-->
                                    <xsl:call-template name="line_note">
                                        <xsl:with-param name="interp_class" select="$interp_class" />
                                        
                                        <xsl:with-param name="span"><xsl:value-of select="$end - $start + 1" /></xsl:with-param>
                                    </xsl:call-template>
                                    <!--From: <xsl:value-of select="@from" />-->
                                    <!--To: <xsl:value-of select="@to" />-->
                                    <!--Start: <xsl:value-of select="$start" />-->
                                    <!--Start_: <xsl:value-of select="$start_" />-->
                                    <!--Start_Node: <xsl:value-of select="$start_node" />-->
                                    <!--End: <xsl:value-of select="$end" />-->
                                    <!--End_: <xsl:value-of select="$end_" />-->
                                    <!--End_Node: <xsl:value-of select="$end_node" />-->
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:call-template name="line_note">
                                        <xsl:with-param name="interp_class" select="$interp_class" />
                                    </xsl:call-template>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:for-each>
                </tr>
            </xsl:for-each>
        </tbody>
    </xsl:template>
    <xsl:template name="poem_label">
        <thead><tr><td/><th style="text-align: left;"><xsl:value-of select="text()" /></th></tr></thead>
    </xsl:template>
</xsl:stylesheet>
