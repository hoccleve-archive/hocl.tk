// These functions assume the DOM is fully loaded and that
// jquery is available
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
