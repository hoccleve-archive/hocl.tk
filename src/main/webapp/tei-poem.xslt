<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:tei="http://www.tei-c.org/ns/1.0"  
    >
    <xsl:template match="//tei:text">
        <html>
            <xsl:for-each select="tei:body">
                <h1><xsl:value-of select="tei:head/text()" /></h1>
                <table>
                    <xsl:for-each select="tei:lg">
                        <xsl:for-each select="tei:l">
                            <tr>
                                <td>
                                    <xsl:if test="(@n mod 5) = 0">
                                        <xsl:value-of select="@n" />
                                    </xsl:if>
                                </td>
                                <td>
                                    <xsl:value-of select="text()" />
                                </td>
                            </tr>
                        </xsl:for-each>
                    </xsl:for-each>
                </table>
            </xsl:for-each>
        </html>
    </xsl:template>

</xsl:stylesheet>
