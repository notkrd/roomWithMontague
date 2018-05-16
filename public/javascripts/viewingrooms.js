function addMonologue(monologue_id) {
    $.get("/ajax/monologue", {id: monologue_id}, (function(r) { addToLog(r); }));
}

function makeAssertion(an_utterance, utterance_parsed) {
    $.get("/ajax/assert", {utterance: an_utterance, parse: utterance_parsed}, (function(r) { addToLog(r); }));
    clearMsg();
}

function theMessage() {
    return $("#message");
}

function theLog() {
    return $("#text-log");
}

function addToLog(stuff) {
    theLog().prepend(stuff);
}

function replaceMsg(some_str) {
    theMessage().text(some_str);
}

function addMsg(some_str) {
    theMessage().append(" " + some_str);
}

function clearMsg() {
    theMessage().text("");
    theMessage().data("parsed", [])
}

function restartDiscourse() {
    theLog().text("");
}

function tryToUtterPhrase(some_str, some_cat) {
    addMsg(some_str);
    theMessage().data("parsed").push({"phrase": some_str, "cat": some_cat});
}

$(function() {

    console.log("eh");

    if($("#world-vals").data("world") == "This room") {
        theMessage().text("the water is wet");
        theMessage().data("parsed", [{"phrase": "the water", "cat": "NP"}, {"phrase": "is wet", "cat": "VP"}]);
    }

    else if($("#world-vals").data("world") == "That room") {
        theMessage().text("the mathematician is dead");
        theMessage().data("parsed", [{"phrase": "the mathematician", "cat": "NP"}, {"phrase": "is dead", "cat": "VP"}]);
    }
    else {
        theMessage.text("");
        theMessage().data("parsed", []);
    }

    $(".world-elt").click(function () {tryToUtterPhrase($(this).data("elt-val"), $(this).data("cat"))});
    $("#assert-button").click(function () {makeAssertion(theMessage().text(), theMessage().data("parsed"))});
    $("#clear-button").click(function () {clearMsg()});
    $("#restart-button").click(function () {restartDiscourse()});

});