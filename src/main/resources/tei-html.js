// These functions assume the DOM is fully loaded and that
// jquery is available

// XXX: This only works for one kind of note
function make_sidebar_notes_old ()
{
    var all_notes_class = $("#sidebar").attr("class");
    var re = new RegExp("^"+all_notes_class +"\\.");

    $("tr.line td.note").mouseenter( function () {
        var note_part = this;
        var classes = $(note_part).attr("class");
        console.log(classes);
        var note_class = "";
        for (var x in classes)
        {
            if (re.exec(x) != null)
            {
                note_class = x;
            }
        }

        var text = note_part.text();
        if (text)
        {
                console.log("notes class = "+notes_class);
                $("#sidebar").html(notes_class +"<br/>"+text);
                $("#sidebar").addClass("active");
                $("tr.active").removeClass("active");
                $(this).parent().addClass("active");
        }
    });
}

function make_sidebar_notes ()
{
    // Get the ones with notes
    $("tr.line").mouseenter( function () {
        var note_part = $(this).children("td.note");
        var text = note_part.text();
        if (text)
        {
            $("#sidebar").text(text);
            $("#sidebar").addClass("active");
            $("tr.active").removeClass("active");
            $(this).addClass("active");
        }
    });
    // Make mouseover events
}
