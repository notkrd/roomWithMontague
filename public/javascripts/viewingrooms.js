function addMonologue(monologue_id) {
    $.get("/monologue", {id: monologue_id}, (function(r) { setLog(r); }));
}

function setLog(str) {
    $("#text-log").html(str);
}

function getReq(monologue_id) {
    return $.get("/monologue", {id: monologue_id})
}

$(function() {

    console.log("eh");

    function replaceMsg(some_str) {
        $("#message").text(some_str);
    }

    function tryToUtterPhrase(some_str) {
        replaceMsg(some_str);
    }

    $(".world-elt").click(function () {tryToUtterPhrase($(this).attr("data-elt-val"))});

    addMonologue("intro")

});

