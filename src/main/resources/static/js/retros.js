$(document).ready(function() {
    
    var e = {
        "Pros and Cons": ["Pros", "Cons"],
        Todos: ["Todos"],
        "Six thinking hats": ["Blue - Process", "White - Facts", "Red - feelings", "Green - Creativity", "Yellow - Benefits", "Black - Cautions"],
        "Star Fish Retrospective": ["Keep Doing", "Start Doing", "Stop Doing", "Less of", "More of", "Action Items"],
        Retrospective: ["What went well", "What can be improved", "Action Items"]
    }
      , t = function(t) {
        var n = $(t.currentTarget).val();
        $("#sectionWrapper input").removeAttr("disabled"),
        $("#sectionWrapper input").show(),
        $("#sectionWrapper input:gt(" + (n - 1) + ")").attr("disabled", "disabled"),
        $("#sectionWrapper input:gt(" + (n - 1) + ")").hide();
        var i = $(t.currentTarget).children("option:selected").text();
        if (e[i]) {
            var r = e[i];
            $.each(r, function(e, t) {
                $("#sectionWrapper input:eq(" + e + ")").val(t)
            })
        }
    };
    $.each(e, function(e, t) {
        $("#numberOfSections").prepend($("<option></option>").attr("value", t.length).text(e))
    }),
    $("#numberOfSections").change(t).change();
   
});
