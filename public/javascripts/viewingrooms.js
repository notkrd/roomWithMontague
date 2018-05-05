$(function(){

    console.log("eh");

    function replaceMsg(some_str) {
        $("#message").text(some_str);
    }

    $(".world-elt").click(function () {replaceMsg($(this).attr("data-elt-val"))});

});