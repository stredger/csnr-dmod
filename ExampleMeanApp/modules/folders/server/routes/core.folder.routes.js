'use strict';
// =========================================================================
//
// Routes for Folders
//
// =========================================================================
var path     	= require('path');
var FolderClass = require (path.resolve('./modules/folders/server/controllers/core.folder.controller'));
var routes 		= require (path.resolve('./modules/core/server/controllers/core.routes.controller'));
var policy 		= require (path.resolve('./modules/core/server/controllers/core.policy.controller'));
var config = require('../../../../config/config');
var _ = require('lodash');
module.exports = function (app) {
	//
	// get put new delete
	//
	routes.setCRUDRoutes (app, 'folders', FolderClass, policy, ['get','put','new', 'delete', 'query'], {all:'guest',get:'guest'});
	app.route ('/api/folders/for/project/:projectid/in/:parentid')
		.all (policy ('guest'))
		.get (function (req, response) {
			// return model.getFoldersForProject (req.params.projectid, req.params.parentid);
			console.log("PARENT:", req.params.parentid);
			var superagent = require('superagent');
			var agent1 = superagent.agent();
			var bearer_token = req.headers.authorization; 
			console.log("**************************************************************** ");
			// console.log("bearer_token ", bearer_token);
			var dmsurl = 'http://' + config.dmservice + ':8080/api/documents';
			if (req.params.parentid !== '1') {
				dmsurl += '/folders/' + req.params.parentid;
			}
			console.log("DMS URL is " + dmsurl);
			agent1.get(dmsurl)
			.set('Authorization', bearer_token)
			.end(function (err, res) {
				if (err) {
					console.log(err);
					return response.json({});
				}
				var files = [];
				var obj = JSON.parse(res.text);
				// console.log("response is ",obj.folders.folderList);
				console.log("**************************************************************** ");
				if (obj.folders && obj.folders.folderList) {
					var fs = [];
					console.log("folder count:", obj.folders.folderList.length);
					_.each(obj.folders.folderList, function (folder) {
						// console.log("folder:", folder);
						folder.displayName = folder.name;
						folder.documentDate = folder.lastModifiedDate;
						fs.push(folder);
					});
					return response.json(fs);
				} else {
					return response.json({});
				}
			});
		});
	app.route ('/api/folders/for/project/:projectid/:folderid')
		.all (policy ('guest'))
		.get (routes.setAndRun (FolderClass, function (model, req) {
			return model.getFolderObject (req.params.projectid, req.params.folderid);
		}));
	app.route('/api/publish/folders/:folders').all(policy('user'))
		.put(routes.setAndRun(FolderClass, function (model, req) {
			return model.publish(req.Folder);
		}));
	app.route('/api/unpublish/folders/:folders').all(policy('user'))
		.put(routes.setAndRun(FolderClass, function (model, req) {
			return model.unpublish(req.Folder);
		}));
};

