<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:tei="http://www.tei-c.org/ns/1.0"  
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0"
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
            </head>
            <body>
                <xsl:for-each select="tei:text/tei:body">
                    <h1><xsl:value-of select="tei:head/text()" /></h1>
                    <table>
                        <xsl:for-each select="tei:div[@type='poem']">
                            <xsl:for-each select="tei:lg">
                                <xsl:for-each select="tei:l">
                                <tr>
                                    <xsl:if test="@n">
                                        <td>
                                            <xsl:value-of select="@n" />
                                        </td>
                                    </xsl:if>
                                    <td>
                                        <xsl:value-of select="text()" />
                                    </td>
                                </tr>
                                </xsl:for-each>
                            </xsl:for-each>
                        </xsl:for-each>
                    </table>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
