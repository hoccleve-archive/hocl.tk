<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:tei="http://www.tei-c.org/ns/1.0"  
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:xi="http://www.w3.org/2001/XInclude"
    version="1.0"
    >
    <!--
       - There *is* a TEI->HTML XSLT-based converter provided by TEI-C, but it
       - basically just wraps everything in divs. It doesn't support line numbers
       - out of the box. It isn't any prettier...
       -->
    <xsl:template match="/tei:TEI">
        <html>
            <head>
                <title>
                    <xsl:apply-templates select="tei:teiHeader/tei:fileDesc/tei:titleStmt/tei:title/text()" />
                </title>
                <script src="resources/tei-html.js">;</script>
                <link href="resources/tei-html.css" rel="stylesheet" type="text/css" />
                <!-- TODO: Put this in another transform and find out how to do XInclude processing in Java
                   -<xi:include href="html-head-includes"/>
                   -->
            </head>
            <body>
                <xsl:for-each select="tei:text/tei:body">
                    <h1><xsl:value-of select="tei:head/text()" /></h1>
                    <table>
                        <!--This thead prevents the first heading in the poem from jumping up here-->
                        <thead><tr><th></th></tr></thead>
                        <xsl:for-each select="tei:div[@type='poem']">
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
                        </xsl:for-each>
                    </table>
                </xsl:for-each>
                <div  id="sidebar" />
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
                <xsl:value-of select="@type" /> note
            </xsl:attribute>
            <xsl:apply-templates select="tei:note" />
            <xsl:value-of select="text()" />
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
                <xsl:variable name="line_number"
                    select="@n"/>
                <tr>
                    <xsl:if test="@xml:id">
                        <xsl:attribute name="id">
                            <xsl:value-of select="$line_id" />
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:attribute name="class">line</xsl:attribute>
                    <td>
                        <xsl:if test="@n">
                            <xsl:value-of select="@n" />
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
                        <xsl:for-each select="tei:span">
                            <xsl:if test="(@target=$line_id_ref) or (@from=$line_id_ref)">
                                <xsl:call-template name="line_note">
                                    <xsl:with-param name="interp_class" select="$interp_class" />
                                </xsl:call-template>
                                <xsl:if test="@from=$line_id_ref">
                                    <xsl:call-template name="line_note">
                                        <xsl:with-param name="interp_class" select="$interp_class" />
                                        <!--TODO: calculate the actual span-->
                                        <xsl:with-param name="span">2</xsl:with-param>
                                    </xsl:call-template>
                                </xsl:if>
                            </xsl:if>
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
