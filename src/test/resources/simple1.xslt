<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    >
    <xsl:template match="/a">
        <a>
            <xsl:for-each select="b">
                <b><xsl:value-of select="text()" />
                    <xsl:text>n</xsl:text>
                </b>
            </xsl:for-each>
        </a>
    </xsl:template>
</xsl:stylesheet>
