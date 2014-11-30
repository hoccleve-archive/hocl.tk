// These functions assume the DOM is fully loaded and that
// jquery is available

// XXX: This only works for one kind of note
function make_sidebar_notes ()
{
    /* Each sidebar corresponds to a class of notes.
     * For class of notes, we go through the poem and
     * set a mouse-enter event to update the sidebar if
     * there is a note
     */
    $("table.poem").each( function () {
        /* Hide the note headers */
        $("th.note").css("display", "none");
        $("tr.line > td.note").each(function () {
            var note = this;
            var line = $(note).parent();
            /* Hide the notes themselves */
            $(note).css("display","none");

            var length = $(note).attr("rowspan");
            if (!length)
            {
                length = 1;
            }
            var referenced_lines = $(line).nextAll().addBack().slice(0,length);
            /* I've never written such a jquery selector yet */
            referenced_lines.mouseenter(function () {
                $(".sidebar.active").removeClass("active");
                $("tr.active").removeClass("active");
                side_bar_event(note);
                referenced_lines.addClass("active");
            });
        });
    });
}

var note_class_regex = new RegExp("^([^.]+)\\.(.*)");
function side_bar_event (note)
{
    var classes = $(note).attr("class");

    var classes_list = classes.split(" ");
    // The whole name of the class;
    var note_class = "";
    // The group that the note belongs to;
    var note_group = "";
    // The type of the note, without the note_group
    var note_type = "";

    for (var i = 0; i < classes_list.length; i++)
    {
        var res = note_class_regex.exec(classes_list[i]);
        if (res != null)
        {
            note_class = classes_list[i];
            note_group = res[1];
            note_type = res[2];
        }
    }

    var text = $(note).html();
    if (text)
    {
        $(".sidebar."+note_group).addClass("active");
        $(".sidebar."+note_group).html(note_group+"<br/><em>"+note_type + "</em>" + "<br/>" + text);
    }
}
