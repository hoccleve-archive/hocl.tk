<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:tei="http://www.tei-c.org/ns/1.0"  
    >
<!--This guy is for extracting a table from an annotated poem-->
    <xsl:param name="subject"></xsl:param>
    <xsl:template match="/">
        <xsl:for-each select="tei:spanGrp">
            <ct:table xmlns:ct="http://hocl.tk/schema/">
                <xsl:attribute name="type">
                    <xsl:value-of select="@type" />
                </xsl:attribute>
                <xsl:attribute name="subject">
                    <xsl:value-of select="$subject" />
                </xsl:attribute>
                <xsl:for-each select="tei:span">
                    <ct:entry>
                        <xsl:if test="@type">
                            <ct:type>
                                <xsl:attribute name="name">
                                    <xsl:value-of select="@type" />
                                </xsl:attribute>
                                <xsl:if test="tei:note">
                                    <ct:note>
                                        <xsl:value-of select="tei:note/text()" />
                                    </ct:note>
                                </xsl:if>
                            </ct:type>
                        </xsl:if>
                        <ct:reference>
                            <!--Taken from tei:span including the att.pointing attribute set (contains target)-->
                            <xsl:copy-of select="@*[name() != 'type']" />
                        </ct:reference>
                        <ct:function>
                            <xsl:for-each select="text()" >
                                <xsl:value-of select="." />
                            </xsl:for-each>
                        </ct:function>
                    </ct:entry>
                </xsl:for-each>
            </ct:table>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
