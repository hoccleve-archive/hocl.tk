<html>
    <head>
        <%@include file="resources/html-head-includes" %>
        <title>Demo page</title>
    </head>
    <body>
        <div style="float: left;" >
            <textarea id="input-xml" name="text" cols=60 rows=20><jsp:include page="resources/reg+interp.xml" /></textarea>
            <div id="buttons">
                <button id="add-line-numbers-button" type="submit">Add line numbers</button>
                <button id="render-html-button" type="submit">Render HTML</button>
                <button id="reload-document-button" type="submit">Reload original document</button>
            </div>
        </div>
        <div style="float: right;" id="result">
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

        // From: http://stackoverflow.com/a/22179984/638671
        function update_text_time (elt){
            var currentVal = $(elt).val();
            if(currentVal == oldVal) {
                return; //check to prevent multiple simultaneous triggers
            }

            oldVal = currentVal;
            changedTime = new Date();
        }

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

        // Runs an AJAX HTTP POST and runs the function 'fn'
        // when complete.
        // Uses the date/time variables defined above for 
        // caching.
        function post_input (elt, uri, fn)
        {
            var ifmod = inputIfMod[uri] ? inputIfMod[uri] : new Date(0);
            $.ajax({
                url: uri,
                //ifModified: true,
                //headers : {"Last-Modified": changedTime.toUTCString(),
                    //"If-Modified-Since": ifmod.toUTCString()
                //},
                data: {"text": $(elt).val()},
                type: "POST",
                datatype: "text/xml"
            })
            .done(fn);
        }

        $(document).ready(function()
        {

            $( "#add-line-numbers-button" ).click(function( event ) {
                console.log(event);
                post_input("#input-xml", "tei-numbers", function (res,stat,jqXHR) {
                    console.log("status = " + stat);
                    if (stat == "success")
                    {
                        var str = (new XMLSerializer()).serializeToString(res);
                        $("#input-xml").val(str);
                        inputIfMod["tei-numbers"] = response_last_modified(jqXHR);
                        update_text_time("#input_xml");
                    }
                });
            });

            $( "#render-html-button" ).click(function( event ) {
                console.log(event);
                post_input("#input-xml", "tei-html", function (res,stat,jqXHR) {
                    console.log("status = " + stat);
                    if (stat == "success")
                    {
                        inputIfMod["tei-html"] = response_last_modified(jqXHR);
                        $("#result").html(res);
                    }
                });
            });
            $( "#reload-document-button" ).click(function( event ) {
                console.log(event);
                text = $("#input-xml").text();
                $("#input-xml").val(text);
                update_text_time("#input_xml");
            });

            $("#input-xml").on("change keyup paste", function() {
                update_text_time(this);
            });
        });
    </script>
</html>
