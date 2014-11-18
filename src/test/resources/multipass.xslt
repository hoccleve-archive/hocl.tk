<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    >
    <!--
       - Learning test. Can get an sibling element and modify it?
       -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>  
    </xsl:template>
    <xsl:template match="/zum/as">
        <!--Delete it-->
    </xsl:template>
    <xsl:template match="/zum/zucks">
        <!--Delete it-->
    </xsl:template>

    <xsl:template match="/zum">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
            <xsl:for-each select="zucks">
                <xsl:copy>
                    <xsl:for-each select="zuck">
                        <xsl:copy>
                            <xsl:apply-templates select="@*|node()"/>
                            <poptart>
                                <xsl:value-of select="/zum/as/a[@ref=concat('#',current()/@xml:id)]" />
                            </poptart>
                        </xsl:copy>
                    </xsl:for-each>
                </xsl:copy>
            </xsl:for-each>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>

