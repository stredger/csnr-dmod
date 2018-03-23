'use strict';
// =========================================================================
//
// Routes for Projects
//
// =========================================================================
var Project     = require ('../controllers/project.controller');
var _           = require ('lodash');
var routes = require ('../../../core/server/controllers/core.routes.controller');
var policy = require ('../../../core/server/controllers/core.policy.controller');

module.exports = function (app) {
	routes.setCRUDRoutes (app, 'project', Project, policy);

	app.route ('/api/project/bycode/:projectcode')
		.all (policy ('guest'))
		.get (function (req, res) {
			routes.setSessionContext(req)
				.then( function (opts) {
					var ctrl = new Project(opts);
					return ctrl.one ({code:req.params.projectcode}, "-directoryStructure");
				})
				.then(function(proj) {
					if (!proj)  {
						return res.status(404).send ({message: 'Project Not Found'});
					}
					return res.json (proj);
				});
		});


//	app.route ('/api/projects/published')
//		.get (routes.setAndRun (Project, function (model, req) {
//			return model.published ();
//		}));

	app.route ('/api/project/:project/directory/list')
		.all (policy ('guest'))
		.get (function (req, response) {
			// return model.getDirectoryStructure (req.Project);
			// Get the list of folders
			var config = require('../../../../config/config');
			var TreeModel = require ('tree-model');
			var superagent = require('superagent');
			var agent1 = superagent.agent();
			var bearer_token = req.headers.authorization; 
			
			// console.log("bearer_token ", bearer_token);
			var dmsurl = 'http://' + config.dmservice + ':8080/api/documents';

			//set the authorization if there is a bearer token
			if(bearer_token) {
				agent1.set('Authorization', bearer_token);
			}
			
			agent1.get(dmsurl)
			.end(function (err, res) {
				if (err) {
					console.log(err);
					return response.json({});
				}
				var files = [];
				var obj = JSON.parse(res.text);
				// console.log("response is ",obj.folders.folderList);
				// console.log("**************************************************************** ");
				if (obj.folders && obj.folders.folderList) {
					var fs = [];
					var tree = new TreeModel();
					var root = tree.parse({id: 1, name: 'ROOT', lastId: 1, published: true});
					_.each(obj.folders.folderList, function (folder) {
						console.log("folder:", folder);
						var node123 = tree.parse({id: folder.itemID,
												_id: folder.itemID,
												name: folder.name,
												displayName: folder.name,
												documentDate: folder.lastModifiedDate,
												published: folder.securityMetadata.generalVisibility === "ExternallyVisible"});
						// Add it to the parent
						root.addChild(node123);
					});
					return response.json(root.model);
				} else {
					return response.json({});
				}
			});
		});
};

