<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="2.0"
    >
    <xsl:template match="/resources">
        <emails>
            <xsl:for-each select="resource">
                <email><xsl:value-of select="email/text()" /></email>
            </xsl:for-each>
        </emails>
    </xsl:template>
</xsl:stylesheet>
