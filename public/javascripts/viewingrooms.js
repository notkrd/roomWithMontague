function addMonologue(monologue_id) {
    $.get("/ajax/monologue", {id: monologue_id}, (function(r) { addToLog(r); }));
}

function makeAssertion(an_utterance) {
    $.get("/ajax/assert", {utterance: an_utterance}, (function(r) { addToLog(r); }));
    clearMsg();
}

function addToLog(stuff) {
    $("#text-log").prepend(stuff);
}

function replaceMsg(some_str) {
    $("#message").text(some_str);
}

function addMsg(some_str) {
    $("#message").append(" " + some_str);
}

function clearMsg() {
    $("#message").text("");
}

function restartDiscourse() {
    $("#text-log").text("");
}

$(function() {

    console.log("eh");

    function tryToUtterPhrase(some_str) {
        addMsg(some_str);
    }

    $(".world-elt").click(function () {tryToUtterPhrase($(this).data("elt-val"))});
    $("#assert-button").click(function () {makeAssertion($("#message").text())});
    $("#clear-button").click(function () {clearMsg()});
    $("#restart-button").click(function () {restartDiscourse()});

});

