<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:tei="http://www.tei-c.org/ns/1.0"  
    >
<!--This takes a tei document and adds line numbers (@n attribute) and
     xml:ids (as line-#) consecutively. Any existing numbers are ignored
     -->

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>  
    </xsl:template>
    <xsl:template match="//tei:lg/tei:l">
        <xsl:copy>
            <xsl:attribute name="n">
                <xsl:number level="any" count="//tei:l"/>
            </xsl:attribute>
            <xsl:attribute name="xml:id">line-<xsl:number level="any" count="//tei:l"/>
            </xsl:attribute>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>  
    </xsl:template>
</xsl:stylesheet>
