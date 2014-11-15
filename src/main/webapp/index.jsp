<html>
    <head>
        <%@include file="resources/html-head-includes" %>
        <title>Demo page</title>
    </head>
    <body>
        <form id="main-input" class="input-box">
            <textarea id="input-xml" name="text" cols=60 rows=20><jsp:include page="resources/reg+interp.xml" /></textarea>
            <input type="submit" value="Add line numbers" />
        </form>
        <div id="result">
        </div>
    </body>
    <script>
        $(document).ready(function()
        {
            $( "#main-input" ).submit(function( event ) {
                alert( "Handler for .submit() called." );
                $.post("/my-webapp/tei-line-numbers", {"text": $("input-xml").html()}, function (res) {
                    $("result").html(res);
                });
                event.preventDefault();
            });
        });
    </script>
</html>
<%@ page import="java.io.*" %>
