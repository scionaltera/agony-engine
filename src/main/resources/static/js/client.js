var socket = null;
var stompClient = null;
var isReconnecting = false;
var reconnectDelay = 2;
var commandHistory = [];
var commandHistoryIndex = -1;
var commandHistoryLength = 50;
var scrollBackLength = 500;

$(document).ready(function () {
    $("#user-input-form").submit(function (event) {
        sendInput();
        event.preventDefault();
        return false;
    });

    connect();
});

$(document).keyup(function (event) {
    if (event.which === 38) { // up arrow
        commandHistoryIndex++;

        if (commandHistoryIndex >= commandHistory.length) {
            commandHistoryIndex = commandHistory.length - 1;
        }

        if (commandHistoryIndex >= 0) {
            $("#user-input").val(commandHistory[commandHistoryIndex]);
        }
    } else if (event.which === 40) { // down arrow
        commandHistoryIndex--;

        if (commandHistoryIndex < 0) {
            commandHistoryIndex = -1;
        }

        if (commandHistoryIndex >= 0) {
            $("#user-input").val(commandHistory[commandHistoryIndex]);
        } else {
            $("#user-input").val("");
        }
    }
});

function connect() {
    socket = new SockJS('/mud');
    stompClient = webstomp.over(socket, { "heartbeat" : false });
    stompClient.connect(
        {},
        function (frame) {
            console.log('Connected: ' + frame);
            showOutput(["[green]Connected to server."]);

            reconnectDelay = 2;

            stompClient.subscribe('/user/queue/output', function (message) {
                var msg = JSON.parse(message.body);
                showOutput(msg.output);
            },
            { "actor" : actor });
        },
        function () {
            if (isReconnecting === false) {
                showOutput(["[red]Disconnected from server. Attempting to reconnect in " + reconnectDelay + " seconds..."]);

                isReconnecting = true;

                setTimeout(function () {
                    console.log('Disconnected.');
                    showOutput(["[dyellow]Reconnecting to server."]);

                    isReconnecting = false;

                    this.connect();
                }, reconnectDelay * 1000);

                reconnectDelay = Math.min(reconnectDelay * 2, 60);
            }
        });
}

function sendInput() {
    var inputBox = $("#user-input");

    commandHistoryIndex = -1;
    commandHistory.unshift(inputBox.val());

    if (commandHistory.length > commandHistoryLength) {
        commandHistory.pop();
    }

    $("#output-list").find("li:last-child").append("<span class='yellow'> " + htmlEscape(inputBox.val()) + "</span>");

    stompClient.send("/app/input", JSON.stringify({'input': inputBox.val()}));
    inputBox.val('');
}

function showOutput(message) {
    var outputBox = $("#output-box");
    var outputList = $("#output-list");

    for (var i = 0; i < message.length; i++) {
        if ("" === message[i]) {
            outputList.append("<li>&nbsp;</li>");
        } else {
            outputList.append("<li>" + replaceColors(message[i]) + "</li>");
        }
    }

    outputBox.prop("scrollTop", outputBox.prop("scrollHeight"));

    var scrollBackOverflow = outputList.find("li").length - scrollBackLength;

    if (scrollBackOverflow > 0) {
        outputList.find("li").slice(0, scrollBackOverflow).remove();
    }
}

function replaceColors(message) {
    return String(message).replace(/\[(\w+)]/g, "<span class='$1'>");
}

function htmlEscape(str) {
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/\//g, '&#x2F;');
}
