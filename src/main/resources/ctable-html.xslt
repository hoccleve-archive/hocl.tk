<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ct="http://hocl.tk/schema/">
    <xsl:template match="table">
        <html>
            <head>
                <meta name="viewport" content="width=device-width" />
                <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
                <title>Time referents concordance table demo</title>

                <script src="jquery-1.11.1.min.js"></script>
                <script src="jquery-csv.min.js"></script>
                <script src="sorttable.js"></script>
                <style>
                    table {
                    border-spacing: 0.5rem;
                    }
                </style>
                <script>
                    function generateTable(data) {
                    var html = '';

                    if(typeof(data[0]) === 'undefined') {
                    return null;
                    }

                    if(data[0].constructor === String) {
                    html += '<tr>\r\n';
                        for(var item in data) {
                        html += '<td>' + data[item] + '</td>\r\n';
                        }
                        html += '</tr>\r\n';
                    }

                    if(data[0].constructor === Array) {
                    for(var row in data) {
                    html += '<tr>\r\n';
                        for(var item in data[row]) {
                        html += '<td>' + data[row][item] + '</td>\r\n';
                        }
                        html += '</tr>\r\n';
                    }
                    }

                    if(data[0].constructor === Object) {
                    html += '<thead>\r\n';
                        for(var item in data[0]) {
                        if (data[0].hasOwnProperty(item))
                        {
                        html += '<td>' + item + '</td>\r\n';
                        }
                        }
                        html += '</thead>\r\n';
                    /* XXX: I would think that this index should start at 1 to skip the header row.
                    *      I have no idea why it doesn't.
                    */
                    for(var i = 0; i < data.length; i++){
                    var row = data[i];
                    html += '<tr>\r\n';
                        for(var item in row) {
                        if (row.hasOwnProperty(item))
                        {
                        html += '<td>';
                            if (item == "LINE NUMBER")
                            {
                            var content = row[item];
                            var matches = content.match(/L[.] (((,\s*)?\d+(\s*[–-]\s*\d+)?)+)/);
                            var url = "/html/regiment_of_princes.html";
                            if (matches != null)
                            {
                            url += "#"+matches[0];
                            }
                            html += "<a href=\""+url+"\">"+matches[1]+"</a>";
                            }
                            else
                            {
                            html += row[item]; 
                            }
                            html += '</td>\r\n';
                        }
                        }
                        html += '</tr>\r\n';
                    }
                    }

                    return html;
                    }
                </script>
            </head>

            <body>
                <table border="1" width="50%" align="center">
                    <caption>Concordance</caption>
                    <tr><th>Lines</th><th>Note</th></tr>
                    <xsl:for-each select="spanGrp/span">
                        <tr><td><xsl:value-of select="@from"/>
                                <xsl:if test="@to">-<xsl:value-of select="@to"/></xsl:if>
                            </td>
                            <td><xsl:value-of select="text()" />
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
