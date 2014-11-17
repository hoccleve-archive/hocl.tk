<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="2.0"
    >
    <!--
       - There *is* a TEI->HTML XSLT-based converter provided by TEI-C, but it
       - basically just wraps everything in divs. It doesn't support line numbers
       - out of the box. It isn't any prettier...
       -->
    <xsl:template match="/records">
        <resources>
            <xsl:for-each select="record">
                <resource>
                    <email><xsl:value-of select="Contact/text()" /></email>
                    <ccData><xsl:value-of select="Grammars/text()" /></ccData>
                </resource>
            </xsl:for-each>
        </resources>
    </xsl:template>
</xsl:stylesheet>
