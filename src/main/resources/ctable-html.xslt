<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:tei="http://www.tei-c.org/ns/1.0"  
    xmlns:ct="http://hocl.tk/schema/"
    version="1.0"
    >
<!--
   -    This stylsheet should be able to present any *lined* text. If the referenced text isn't
   -    linked to a line, then the entry in the lines column defaults to the xml-id reference
   -    for whatever the element is.
   -
   -    TODO: Change the 'lines' column to a reference column, identify the element and label the
   -    reference text with an appropriate indicator.
   -->
    <xsl:template match="/">
        <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html></xsl:text>
        <html>
            <head>
                <meta name="viewport" content="width=device-width"></meta>
                <meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
                <title>Time referents concordance table demo</title>

                <script src="jquery-2.1.1.js"></script>
                <script src="sorttable.js"></script>
                <style>
                    table {
                        border-spacing: 0.5rem;
                    }
                </style>
            </head>

            <body>
                <xsl:for-each select="ct:table" >
                    <xsl:variable select="document(@subject)" name="subject" />
                    <table border="1" width="50%" align="center" class="sortable">
                        <caption>Concordance for <xsl:call-template name="tei-title">
                                <xsl:with-param name="tei-doc" select="$subject" />
                            </xsl:call-template>
                        </caption>
                        <tr><th>Lines</th><th>Note</th></tr>
                        <xsl:for-each select="ct:entry">
                            <tr>
                                <td>
                                    <xsl:choose>
                                        <xsl:when test="ct:reference/@from">
                                            <xsl:call-template name="tei-line-number" >
                                                <xsl:with-param select="ct:reference/@from" name="reference" />
                                                <xsl:with-param name="tei-doc" select="$subject"/>
                                            </xsl:call-template>-<xsl:call-template name="tei-line-number" >
                                                <xsl:with-param select="ct:reference/@to" name="reference" />
                                                <xsl:with-param name="tei-doc" select="$subject"/>
                                            </xsl:call-template>
                                        </xsl:when>
                                        <xsl:when test="ct:reference/@target">
                                            <xsl:call-template name="tei-line-number">
                                                <xsl:with-param name="reference" select="ct:reference/@target"/>
                                                <xsl:with-param name="tei-doc" select="$subject"/>
                                            </xsl:call-template>
                                        </xsl:when>
                                    </xsl:choose>
                                </td>
                                <td><xsl:apply-templates select="ct:function/text()" /></td>
                            </tr>
                        </xsl:for-each>
                    </table>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>
    <xsl:template name="tei-title">
        <xsl:param name="tei-doc" />
        <xsl:apply-templates select="$tei-doc//tei:teiHeader/tei:fileDesc/tei:titleStmt/tei:title/text()" />
    </xsl:template>
    <xsl:template name="tei-line-number">
        <xsl:param name="tei-doc" />
        <xsl:param name="reference" />
        <xsl:value-of select="$tei-doc//tei:l[@xml:id=substring-after($reference, '#')]/@n" />
    </xsl:template>

</xsl:stylesheet>
