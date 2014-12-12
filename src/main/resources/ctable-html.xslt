<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:tei="http://www.tei-c.org/ns/1.0"  
    xmlns:ct="http://hocl.tk/schema/"
    version="1.0"
    xmlns:exsl="http://exslt.org/common" 
    extension-element-prefixes="exsl"
    >
<!--
   -    This stylsheet should be able to present any *lined* text. If the referenced text isn't
   -    linked to a line, then the entry in the lines column defaults to the xml-id reference
   -    for whatever the element is.
   -
   -    TODO:
   -    [ ] Display multiple the lines of multiple ct:references in an entry
   -    [ ] Change the 'lines' column to a reference column, identify the element and label the,
   -        reference text with an appropriate indicator. Like if an identifier is on a <p> element,
   -        then you might have "paragraph: It was the best of times...". If there's a "@n" 
   -        attribute or similar label, then that could take the place of the text snippet.
   -    [X] Add an <html:a> hyperlink to the subject document.
   -        [ ] Link to the referenced lines in the subject document
   -        [ ] Make the link resolve to a TEI-HTML page
   -    
   -->
    <xsl:import href="resources/url-encode.xslt" />
    <xsl:output method="html"
                indent="yes"
                omit-xml-declaration="yes"
                />
    <xsl:template match="/">
        <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
        <html>
            <head>
                <meta name="viewport" content="width=device-width"></meta>
                <meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
                <title>Time referents concordance table demo</title>

                <script src="jquery-2.1.1.js"></script>
                <script src="sorttable.js"></script>
                <style>
                    table {
                        border-spacing: 0.5rem;
                    }
                </style>
            </head>

            <body>
                <xsl:for-each select="ct:table" >
                    <xsl:variable name="subject">
                        <xsl:choose>
                            <xsl:when test="@subject">
                                <xsl:copy-of select="document(@subject)" />
                            </xsl:when>
                            <xsl:when test="ct:subjectDocument">
                                <xsl:copy-of select="ct:subjectDocument" />
                            </xsl:when>
                        </xsl:choose>
                    </xsl:variable>
                    <xsl:variable name="doc-title">
                        <xsl:call-template name="tei-title">
                            <xsl:with-param name="tei-doc" select="$subject" />
                        </xsl:call-template>
                    </xsl:variable>
                    <!--<xsl:copy-of select="$subject" />-->
                    <table border="1" width="50%" align="center" class="sortable">
                        <caption>Concordance for <xsl:choose>
                                <xsl:when test="@subject">
                                    <a>
                                        <xsl:attribute name="href">
                                            <xsl:text>tei-html?q=</xsl:text><xsl:call-template name="url-encode">
                                                <xsl:with-param name="str" select="@subject"/>
                                            </xsl:call-template>
                                        </xsl:attribute>
                                        <xsl:value-of select="$doc-title"/>
                                    </a>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="$doc-title"/>
                                </xsl:otherwise>
                            </xsl:choose>
                            <br/>
                            <xsl:choose>
                                <xsl:when test="@label">
                                    <xsl:value-of select="@label"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="@type"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </caption>
                        <tr><th>#</th><th>Lines</th><th>Type</th><th>Note</th></tr>
                        <xsl:for-each select="ct:entry">
                            <tr>
                                <td>
                                    <xsl:number/>
                                </td>
                                <td>
                                    <xsl:choose>
                                        <xsl:when test="ct:reference/@from">
                                            <xsl:call-template name="tei-line-number" >
                                                <xsl:with-param select="ct:reference/@from" name="reference" />
                                                <xsl:with-param name="tei-doc" select="$subject"/>
                                            </xsl:call-template> - <xsl:call-template name="tei-line-number" >
                                                <xsl:with-param select="ct:reference/@to" name="reference" />
                                                <xsl:with-param name="tei-doc" select="$subject"/>
                                            </xsl:call-template>
                                        </xsl:when>
                                        <xsl:when test="ct:reference/@target">
                                            <xsl:call-template name="tei-line-number">
                                                <xsl:with-param name="reference" select="ct:reference/@target"/>
                                                <xsl:with-param name="tei-doc" select="$subject"/>
                                            </xsl:call-template>
                                        </xsl:when>
                                    </xsl:choose>
                                </td>
                                <td>
                                    <xsl:choose>
                                        <xsl:when test="ct:type/@label">
                                            <xsl:value-of select="ct:type/@label"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of select="ct:type/@name"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <xsl:if test="ct:type/ct:note">
                                        (<xsl:value-of select="ct:type/ct:note" />)
                                    </xsl:if>
                                </td>
                                <td><xsl:apply-templates select="ct:function/text()" /></td>
                            </tr>
                        </xsl:for-each>
                    </table>
                    <!--<xsl:if test="@subject">-->
                        <!--This document was generated from: -->
                        <!--<a>-->
                            <!--<xsl:attribute name="href">-->
                                <!--<xsl:value-of select="@subject"/>-->
                            <!--</xsl:attribute>-->
                            <!--<xsl:value-of select="@subject"/>-->
                        <!--</a>-->
                    <!--</xsl:if>-->
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>
    <xsl:template name="tei-title">
        <xsl:param name="tei-doc" />
        <xsl:apply-templates select="exsl:node-set($tei-doc)//tei:teiHeader/tei:fileDesc/tei:titleStmt/tei:title/text()" />
    </xsl:template>
    <xsl:template name="tei-line-number">
        <xsl:param name="tei-doc" />
        <xsl:param name="reference" />
        <xsl:variable name="number-string">
            <xsl:value-of select="exsl:node-set($tei-doc)//tei:l[@xml:id=substring-after($reference, '#')]/@n" />
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$number-string">
                <xsl:value-of select="$number-string" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$reference" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
