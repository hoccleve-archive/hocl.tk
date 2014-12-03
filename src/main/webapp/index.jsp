<html>
    <head>
        <%@include file="resources/html-head-includes" %>
        <title>Demo page</title>
    </head>
    <body>
        <div style="float: left;" >
            <textarea id="input-xml" name="q" cols=60 rows=20><jsp:include page="resources/reg+interp.xml" /></textarea>
            <div id="buttons">
                <button id="add-line-numbers-button" type="submit">Add line numbers</button>
                <button id="render-html-button" type="submit">Render HTML</button>
                <button id="reload-document-button" type="submit">Reload original document</button>
                <br/>
                <button id="load-document-from-web" type="submit">Load Document from URL</button><input id="document-url" type="text" />
                <br/>
                <button id="add-analysis-button" type="submit">Include analysis document</button><input id="analysis-url" type="text" />
                <br/>
                <button id="render-ctable-button" type="submit">Render concordance table</button>
            </div>
        </div>
        <note>Virtually no error handling is done on this page other than to make it basically function. YMMV.</note>
        <div style="float: right;" class="result" id="poem">
        </div>
        <div style="float: right;" class="result" id="ctable">
        </div>
        
    </body>
    <script>
        // The value of the textarea
        var oldVal = "";
        // The time that the textarea last changed.
        // By setting Date(0), it always updates after the page reloads
        var changedTime = new Date(0);
        // The If-Modified-Since dates for each URL.
        var inputIfMod = {};

        // Gets the last-modified from a jQuery XMLHttpResponse or if there
        // isn't one set, then defaults to epoch
        function response_last_modified (jqXHR)
        {
            var server_date = jqXHR.getResponseHeader("Last-Modified");
            if (server_date == null)
            {
                // If we can't get a last-modified, then we just have to
                // ignore the cache next time. Using the Date header 
                // would be incorrect because the server sets the date 
                // after the resource is generated, so we would always 
                // be updating regardless of whether our page changed
                server_date = new Date(0);
            }
            return new Date(server_date);
        }

        xmlser = new XMLSerializer();
        function xmlToString(xml)
        {
            return xmlser.serializeToString(xml);
        }

        // Runs an AJAX HTTP POST and runs the function 'fn'
        // when complete.
        // Uses the date/time variables defined above for 
        // caching.
        function post_input (elt_or_postdata, uri, fn, params)
        {
            var ifmod = inputIfMod[uri] ? inputIfMod[uri] : new Date(0);
            var postdata = "";
            if (elt_or_postdata instanceof XMLDocument)
            {
                var str = xmlToString(elt_or_postdata);
                postdata = str;
            }
            else
            {
                postdata = $(elt_or_postdata).val();
            }

            $.ajax({
                url: uri,
                ifModified: true,
                headers : {"Last-Modified": changedTime.toUTCString(),
                    "If-Modified-Since": ifmod.toUTCString()
                },
                data: $.extend({"q": postdata}, params),
                type: "POST",
                datatype: "text/xml"
            }).done(fn);
        }
        $(document).ready(function()
        {
            // From: http://stackoverflow.com/a/22179984/638671
            function update_text_time (elt){
                var currentVal = $(elt).val();
                if(currentVal == oldVal) {
                    return; //check to prevent multiple simultaneous triggers
                }

                oldVal = currentVal;
                changedTime = new Date();
            }

            function force_update_text_time (elt, date){
                changedTime = new Date(date);
            }

            function transform_input (uri, params)
            {
                post_input("#input-xml", uri, function (res,stat,jqXHR) {
                    if (stat == "success")
                    {
                        var str = xmlToString(res);
                        if ($("#input-xml").val() != str)
                        {
                            $("#input-xml").val(str);
                            force_update_text_time("#input_xml", new Date());
                        }
                        var server_last_modified = response_last_modified(jqXHR)
                        inputIfMod[uri] = server_last_modified;
                    }
                }, params);
            }

            $( "#add-line-numbers-button" ).click(function( event ) {
                console.log(event);
                transform_input("tei-numbers");
            });

            $( "#render-html-button" ).click(function( event ) {
                console.log(event);
                $(".result").css("display", "none");
                $("#poem").css("display","");
                // XXX: Stream the result rather than waiting for the whole thing
                //      to load
                post_input("#input-xml", "tei-html", function (res,stat,jqXHR) {
                    console.log("status = " + stat);
                    if (stat == "success")
                    {
                        $("#poem").html(res);
                        inputIfMod["tei-html"] = response_last_modified(jqXHR);
                    }
                });
            });

            $( "#render-ctable-button" ).click(function( event ) {
                console.log(event);
                // XXX: Stream the result rather than waiting for the whole thing
                //      to load
                post_input("#input-xml", "ctable", function (res, stat) {
                    $(".result").css("display", "none");
                    $("#ctable").css("display","");
                    if (stat == "success")
                    {
                        post_input(res, "ctable-html", function (htmlres,stat,jqXHR)
                        {
                            if (stat == "success")
                            {
                                $("#ctable").html(htmlres);
                                
                                sorttable.init();/* Sorttable seems not to funtion if this isn't called after setting the html */
                                inputIfMod["ctable"] = response_last_modified(jqXHR);
                            }
                        });
                    }
                });
            });

            $( "#reload-document-button" ).click(function( event ) {
                console.log(event);
                var text = $("#input-xml").text();
                $("#input-xml").val(text);
                force_update_text_time("#input_xml", new Date());
            });

            $( "#load-document-from-web" ).click(function( event ) {
                console.log(event);
                var url = $("#document-url").val();
                $.get(url, function (res) { 
                    var str = xmlToString(res);
                    $("#input-xml").val(str);
                    force_update_text_time("#input_xml", new Date());
                });
            });

            $( "#add-analysis-button" ).click(function( event ) {
                console.log(event);
                var url = $("#analysis-url").val();
                transform_input("include-interp", {"ana": url});
            });

            $("#input-xml").on("change keyup paste", function() {
                update_text_time("#input-xml");
            });
        });
    </script>
</html>
