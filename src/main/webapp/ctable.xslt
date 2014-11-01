<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:tei="http://www.tei-c.org/ns/1.0"  
    >
<!--This guy is for extracting a table from an annotated poem-->
<xsl:template match="tei:text">
    <ct:table xmlns:ct="http://hocl.tk/schema/">
        <xsl:for-each select="tei:spanGrp/tei:span">
            <ct:entry>
                <xsl:if test="@type">
                    <ct:type>
                        <xsl:attribute name="class">
                            <xsl:value-of select="@type" />
                        </xsl:attribute>
                    </ct:type>
                </xsl:if>
                <ct:reference>
                    <!--Taken from tei:span including the att.pointing attribute set (contains target)-->
                    <xsl:copy-of select="@*[name() != 'type']" />
                </ct:reference>
                <ct:function>
                    <xsl:value-of select="text()" />
                </ct:function>
            </ct:entry>
        </xsl:for-each>
    </ct:table>
</xsl:template>

</xsl:stylesheet>
