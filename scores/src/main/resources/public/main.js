var fab = require('fabulous');
var jiff = require('jiff');
var stomp = require('stomp-websocket');
var SockJS = require('sockjs-client');

module.exports = fab.runAt(gameDayView, document.querySelector('.scoreboard'));

function gameDayView(node, context) {
	var stomp = Stomp.over(new SockJS('/scores'));

	stomp.connect('guest', 'guest', function() {
		stomp.subscribe("/app/scores", initialScores);
		stomp.subscribe("/topic/scores", updateScores);
	});

	function initialScores(message) {
		context.games = JSON.parse(message.body);
	}

	function updateScores(message) {
		context.games = jiff.patch(JSON.parse(message.body), context.games);
	}
}
