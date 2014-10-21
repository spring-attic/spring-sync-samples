var fab = require('fabulous');
var rest = require('fabulous/rest');
var Document = require('fabulous/Document');

var TodosController = require('./TodosController');

exports.main = fab.run(todosApp);

function todosApp(node, context) {
	context.controller = new TodosController([]);

	var todosClient = rest.at('/todos');
	var patchClient = rest.at('/sync/todos');

	Document.sync([
		Document.fromPatchSyncRemote(function(patch) {
			return patchClient.patch({ entity: patch }).entity();
		}, todosClient.get().entity()),
		Document.fromProperty('todos', context.controller)
	]);
}

