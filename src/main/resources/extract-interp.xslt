<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:tei="http://www.tei-c.org/ns/1.0"  
    xmlns:xi="http://www.w3.org/2001/XInclude"
    version="1.0"
    >
    <!--
    TODO: Takes a document with analysis and extracts the analysis into its own document
    The result can be either just the analysis text or a teiCorpus containing both the
    analysed document and the analysis.
    -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>  
    </xsl:template>

    <xsl:template match="/">
        <tei:teiCorpus>
            <tei:TEI>
                        <xsl:copy-of select="tei:TEI/tei:teiHeader"/>
                <tei:text>
                    <tei:body>
                        <xsl:copy-of select="tei:TEI/tei:text/tei:body/*[not (self::tei:spanGrp)]" />
                    </tei:body>
                </tei:text>
            </tei:TEI>
            <tei:TEI>
                        <xsl:copy-of select="tei:TEI/tei:teiHeader"/>
                <tei:text>
                    <tei:body>
                        <xsl:copy-of select="//tei:text/tei:body/tei:spanGrp">
                        </xsl:copy-of>
                    </tei:body>
                </tei:text>
            </tei:TEI>
        </tei:teiCorpus>
    </xsl:template>
</xsl:stylesheet>
