<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:tei="http://www.tei-c.org/ns/1.0"  
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:xi="http://www.w3.org/2001/XInclude"
    version="1.0"
    >
    <!--
    Includes a doucument at the end of the text body. It can actually be 
    any kind of document.
    `ana` is the analyitcal markup to include
    -->
    <xsl:param name="ana"></xsl:param>
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>  
    </xsl:template>
    <xsl:template match="/tei:TEI/tei:text/tei:body">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
            <xsl:copy-of select="document($ana)//tei:spanGrp" />
        </xsl:copy>  
    </xsl:template>
</xsl:stylesheet>

