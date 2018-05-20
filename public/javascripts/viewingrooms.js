// Logic for stuff

function updateCat(some_cat) {

}

// DOM access stuff

function addMonologue(monologue_id) {
    $.get("/ajax/monologue", {id: monologue_id}, (function(r) { addToLog(r); }));
}

function makeAssertion(an_utterance, utterance_parsed, a_world) {
    $.get("/ajax/assert", {utterance: an_utterance, parsed: utterance_parsed, world: a_world}, (function(r) { addToLog(r); }));
    clearMsg();
}

function theMessage() {
    return $("#message");
}

function theLog() {
    return $("#text-log");
}

function theWorld() {
    return $("#world-vals").data("world");
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
    theMessage().data("cat", "Empty");
}

function restartDiscourse() {
    theLog().text("");
}

function tryToUtterPhrase(some_str, some_cat) {
    addMsg(some_str);
    theMessage().data("parsed").push({"phrase": some_str, "cat": some_cat});
    theMessage().data("cat", updateCat(some_cat));
}


function initializeDiscourse(the_world) {
    if (the_world == "This room") {
        theMessage().text("the water is wet");
        theMessage().data("parsed", [{"phrase": "the water", "cat": "NP"}, {"phrase": "is wet", "cat": "VP"}]);
        theMessage().data("cat", "Sentence");
    }

    else if (the_world == "That room") {
        theMessage().text("the mathematician is dead");
        theMessage().data("parsed", [{"phrase": "the mathematician", "cat": "NP"}, {"phrase": "is dead", "cat": "VP"}]);
        theMessage().data("cat", "Sentence");
    }
    else {
        theMessage.text("");
        theMessage().data("parsed", []);
        theMessage().data("cat", "Empty");
    }
}

// Initialization

$(function() {

    console.log("eh");
    initializeDiscourse($("#world-vals").data("world"));

    $(".world-elt").click(function () {tryToUtterPhrase($(this).data("elt-val"), $(this).data("cat"))});
    $("#assert-button").click(function () {makeAssertion(theMessage().text(), theMessage().data("parsed"), theWorld())});
    $("#clear-button").click(function () {clearMsg()});
    $("#restart-button").click(function () {restartDiscourse()});

});