// Logic for stuff

function updateCat(some_cat) {

}

// DOM access stuff

function requestComposition(l_cat, r_cat) {
    $.ajax({url: jsRoutes.controllers.Assertions.compose(theWorld()).url,
        method: "GET",
        data: {"lcat": l_cat, "rcat": r_cat},
        success: (function(r)  { updateWithCat(r) })
    });
}

function updateWithCat(some_json) {
    console.log(some_json)
    if(some_json.hasOwnProperty('new_cat')) {
        theMessage().data("cat", some_json.new_cat)
    }
    if(some_json.hasOwnProperty('new_opts')) {
        var all_elts = $(".world-elt");
        all_elts.removeClass("permitted");
        all_elts.addClass("verboten");
        for (var an_opt in some_json.new_opts){
            var a_cat = some_json.new_opts[an_opt];
            console.log(a_cat);
            var to_change = $('.world-elt[data-cat="' + a_cat + '"]');
            to_change.removeClass("verboten");
            to_change.addClass("permitted");
        }
    }
}

function makeAssertion(an_utterance, utterance_parsed, a_world) {
    $.ajax({url: jsRoutes.controllers.Assertions.assert(a_world).url,
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({"utterance": an_utterance, "parsed": utterance_parsed, "world": a_world}),
        headers: {"Content-Type": "application/json"},
        success: (function(r) { addToLog(r); })
    });
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
    theMessage().data("parsed", []);
    requestComposition("Vacant", "Vacant");
}

function restartDiscourse() {
    theLog().text("");
}

function tryToUtterPhrase(some_str, some_cat) {
    addMsg(some_str);
    requestComposition(theMessage().data("cat"), some_cat);
    theMessage().data("parsed").push({"phrase": some_str, "cat": some_cat});
    theMessage().data("cat", updateCat(some_cat));
}


function initializeDiscourse(the_world) {
    if (the_world === "This room") {
        theMessage().text("the water is wet");
        theMessage().data("parsed", [{"phrase": "the water", "cat": "Entity"}, {"phrase": "is", "cat": "Auxiliary Verb"}, {"phrase": "wet", "cat": "Adjective"}]);
        theMessage().data("cat", "Sentence");
    }

    else if (the_world === "That room") {
        theMessage().text("the mathematician is dead");
        theMessage().data("parsed", [{"phrase": "the mathematician", "cat": "Entity"}, {"phrase": "is", "cat": "Auxiliary Verb"}, {"phrase": "dead", "cat": "Adjective"}]);
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