<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ct="http://hocl.tk/schema/">
    <xsl:template match="table">
        <html>
            <head>
                <meta name="viewport" content="width=device-width" />
                <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
                <title>Time referents concordance table demo</title>

                <script src="jquery-1.11.1.min.js"></script>
                <script src="jquery-csv.min.js"></script>
                <script src="sorttable.js"></script>
                <style>
                    table {
                    border-spacing: 0.5rem;
                    }
                </style>
            </head>

            <body>
                <table border="1" width="50%" align="center">
                    <caption>Concordance</caption>
                    <tr><th>Lines</th><th>Note</th></tr>
                    <xsl:for-each select="spanGrp/span">
                        <tr><td><xsl:value-of select="@from"/>
                                <xsl:if test="@to">-<xsl:value-of select="@to"/></xsl:if>
                            </td>
                            <td><xsl:value-of select="text()" />
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
