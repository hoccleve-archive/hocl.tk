<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="2.0"
    >
    <!--
       - There *is* a TEI->HTML XSLT-based converter provided by TEI-C, but it
       - basically just wraps everything in divs. It doesn't support line numbers
       - out of the box. It isn't any prettier...
       -->
    <xsl:template match="/a">
        <a>
            <xsl:for-each select="b">
                <b><xsl:value-of select="text()" />n</b>
            </xsl:for-each>
        </a>
    </xsl:template>
</xsl:stylesheet>
