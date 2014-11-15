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
        $(document).ready(function()
        {
            $( "#add-line-numbers-button" ).click(function( event ) {
                console.log(event);
                $.post("/my-webapp/tei-line-numbers", {"text": $("#input-xml").val()}, function (res) {
                    $("#input-xml").val(res);
                });
            });
            $( "#render-html-button" ).click(function( event ) {
                console.log(event);
                $.post("/my-webapp/tei-html", {"text": $("#input-xml").val()}, function (res) {
                    $("#result").html(res);
                });
            });
            $( "#reload-document-button" ).click(function( event ) {
                console.log(event);
                text = $("#input-xml").text();
                $("#input-xml").val(text);
            });
        });
    </script>
</html>
<%@ page import="java.io.*" %>
