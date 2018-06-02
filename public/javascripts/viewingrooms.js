// Logic for stuff

function updateCat(some_cat) {

}

function escapeKey(some_key) {
    return some_key.replace(/ /g,"_");
}

function idForElt(a_phrase, a_cat) {
    return escapeKey(a_cat) + "-" + escapeKey(phrasesToString(a_phrase));
}

function phrasesToString(some_phrs) {
    var the_str = "";
    console.log("hmm");
    for (var a_phr in some_phrs) {
        if(some_phrs[a_phr].hasOwnProperty("phrase")) {
            the_str += some_phrs[a_phr].phrase + " ";
        }
        else if(some_phrs[a_phr].hasOwnProperty("1")) {
            the_str += some_phrs[a_phr][1] + " ";
        }
    }
    return the_str.trim();
}

function phraseHTML(a_phrase, a_cat) {
    return "<li><a id='"+ idForElt(a_phrase, a_cat) + "' class='verboten world-elt' href='#nowhere' data-elt-val='" + JSON.stringify(a_phrase) + "' data-cat='"+ a_cat + "'>" + phrasesToString(a_phrase) + "</a></li>";
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
    console.log(some_json);
    if(some_json.hasOwnProperty('new_cat')) {
        theMessage().data("cat", some_json.new_cat)
    }
    if(some_json.hasOwnProperty('new_opts')) {
        var all_elts = $(".world-elt");
        all_elts.removeClass("permitted");
        all_elts.addClass("verboten");
        for (var an_opt in some_json.new_opts) {
            var a_cat = some_json.new_opts[an_opt];
            console.log(a_cat);
            var to_change = $('.world-elt[data-cat="' + a_cat + '"]');
            to_change.removeClass("verboten");
            to_change.addClass("permitted");
        }
    }
}

function learnPhrase(a_phrase, a_cat) {
    if(!$("#"+idForElt(a_phrase, a_cat)).length ) {
        $("#" + escapeKey(a_cat)).append(phraseHTML(a_phrase, a_cat))
    }
}

function learnSomePhrases(some_phrases) {
    for (var a_cat in some_phrases) {
        for (var a_phrase_key in some_phrases[a_cat]) {
            learnPhrase(JSON.parse(some_phrases[a_cat][a_phrase_key]), a_cat);
        }
    }
}

function makeAssertion(an_utterance, utterance_parsed, a_world) {
    $.ajax({url: jsRoutes.controllers.Assertions.assert(a_world).url,
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({"utterance": an_utterance, "parsed": utterance_parsed, "world": a_world}),
        headers: {"Content-Type": "application/json"},
        success: (function(r) { updateAfterAsserting(r); })
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

function updateAfterAsserting(stuff) {
    console.log(stuff);
    if(stuff.hasOwnProperty("discourse") && stuff.hasOwnProperty("success")) {
        theLog().prepend(stuff.discourse);
        localStorage.setItem("discourse_" + theWorld(), theLog().html());

        if(stuff.success === true && stuff.hasOwnProperty("new_phrases")) {
            learnSomePhrases(stuff["new_phrases"])
        }
    }
    var world_elts = $(".world-elt");
    world_elts.off();
    world_elts.click(function () {tryToUtterPhrase($(this).data("elt-val"), $(this).data("cat"))});
}

function showData() {
    var the_data = JSON.stringify(theMessage().data("parsed"));
    theLog().prepend("You are saying, or might as well be saying, " + the_data + ". <a href='https://github.com/notkrd/roomWithMontague/blob/master/app/models/DiscoWorld.scala'>Parser source</a> <br><br>");
    localStorage.setItem("discourse_" + theWorld(), theLog().html());
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
    localStorage.setItem("discourse_" + theWorld(), theLog().html());
}

function tryToUtterPhrase(some_phrs, some_cat) {
    addMsg(phrasesToString(some_phrs));
    requestComposition(theMessage().data("cat"), some_cat);
    theMessage().data("parsed", theMessage().data("parsed").concat(some_phrs));
    theMessage().data("cat", updateCat(some_cat));
}

function initializeDiscourse(the_world) {
    if (the_world === "This room") {
        theMessage().text("the water is wet");
        theMessage().data("parsed", [{"phrase": "the water", "cat": "Entity"}, {"phrase": "is", "cat": "Auxiliary Verb"}, {"phrase": "wet", "cat": "Adjective"}]);
        theMessage().data("cat", "Sentence");
        $("#world-source").attr("href", "https://github.com/notkrd/roomWithMontague/blob/master/app/models/thisRoom.scala")
    }

    else if (the_world === "That room") {
        theMessage().text("the mathematician is dead");
        theMessage().data("parsed", [{"phrase": "the mathematician", "cat": "Entity"}, {"phrase": "is", "cat": "Auxiliary Verb"}, {"phrase": "dead", "cat": "Adjective"}]);
        theMessage().data("cat", "Sentence");
        $("#world-source").attr("href", "https://github.com/notkrd/roomWithMontague/blob/master/app/models/thatRoom.scala")

    }
    else {
        theMessage.text("");
        theMessage().data("parsed", []);
        theMessage().data("cat", "Empty");
    }

    var past_discourse = localStorage.getItem("discourse_" + the_world);
    if (past_discourse != null) {
        theLog().html(past_discourse);
    }
}

// Initialization

$(function() {

    console.log("eh");
    initializeDiscourse($("#world-vals").data("world"));

    $(".world-elt").click(function () {tryToUtterPhrase($(this).data("elt-val"), $(this).data("cat"))});
    $("#assert-button").click(function () {makeAssertion(theMessage().text(), theMessage().data("parsed"), theWorld())});
    $("#clear-button").click(clearMsg);
    $("#restart-button").click(restartDiscourse);
    $("#structure-button").click(showData);

});