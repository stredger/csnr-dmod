'use strict';
// =========================================================================
//
// Routes for Documents
//
// Does not use the normal crud routes, mostly special sauce
//
// =========================================================================
var DocumentClass  = require ('../controllers/core.document.controller');
var routes = require ('../../../core/server/controllers/core.routes.controller');
var policy = require('../../../core/server/controllers/core.policy.controller');
var config = require('../../../../config/config');
var _ = require('lodash');
var superagent = require('superagent');

module.exports = function (app) {
	//
	// get put new delete
	//
	//routes.setCRUDRoutes (app, 'document', DocumentClass, policy, ['get','put','new', 'delete', 'query'], {all:'guest',get:'guest'});
	//
	// getAllDocuments                 : '/api/documents'
	//
	app.route ('/api/documents')
		.all (policy ('guest'))
		.get (function (req, response) {
			var agent1 = superagent.agent();
			var bearer_token = req.headers.authorization; 
			console.log("params:", req.query.directoryID);
			// console.log("bearer_token ", bearer_token);
			var dmsurl = 'http://' + config.dmservice + ':8080/api/documents';
			
			if (req.query.directoryID !== '1') {
				dmsurl += '/folders/' + req.query.directoryID;
			}
			
			//set the authorization if there is a bearer token
			if(bearer_token) {
				agent1.set('Authorization', bearer_token);
			}
			
			
			console.log("DMS URL is " + dmsurl);
			agent1.get(dmsurl)
			.end(function (err, res) {
				if (err) {
					console.log(err);
					return response.json({});
				}
				var files = [];
				var obj = JSON.parse(res.text);
				// console.log("response is ",obj.files);
				// console.log("**************************************************************** ");
				if (obj.files && obj.files.fileList) {
					return response.json(obj.files.fileList);
				} else {
					return response.json({});
				}
			});
		});
	//
	// getProjectDocuments             : '/api/documents/' + projectId
	//
         
//	app.route ('/api/documents/:projectid')
//		.all (policy ('guest'))
//		.get (routes.setAndRun (DocumentClass, function (model, req) {
//                        console.log('api/documents' + JSON.stringify(req.headers)); 
//			return model.getDocumentsForProject (req.params.projectid, req.headers.reviewdocsonly);
//		}));
//	//
//	// getProjectDocumentTypes         : '/api/documents/types/' + projectId
//	//
//	app.route ('/api/documents/types/:projectid')
//		.all (policy ('guest'))
//		.get (routes.setAndRun (DocumentClass, function (model, req) {
//			return model.getDocumentTypesForProject (req.params.projectid, req.headers.reviewdocsonly);
//		}));
//	//
//	// getProjectDocumentSubTypes      : '/api/documents/subtypes/' + projectId
//	//
//	app.route ('/api/documents/subtypes/:projectid')
//		.all (policy ('guest'))
//		.get (routes.setAndRun (DocumentClass, function (model, req) {
//			return model.getDocumentSubTypesForProject (req.params.projectid);
//		}));
//	//
//	// getProjectDocumentFolderNames   : '/api/documents/folderNames/' + projectId
//	//
//	app.route ('/api/documents/folderNames/:projectid')
//		.all (policy ('guest'))
//		.get (routes.setAndRun (DocumentClass, function (model, req) {
//			return model.getDocumentFolderNamesForProject (req.params.projectid);
//		}));
//	//
//	// getProjectDocumentFolderNames (for MEM)   : '/api/documents/memtypes/' + projectId
//	//
//	app.route ('/api/documents/memtypes/:projectid')
//		.all (policy ('guest'))
//		.get (routes.setAndRun (DocumentClass, function (model, req) {
//			return model.getDocumentTypesForProjectMEM (req.params.projectid);
//		}));
//	//
//	// getProjectDocumentVersions      : '/api/documents/versions/' + projectId
//	//
//	app.route ('/api/documents/versions/:document')
//		.all (policy ('guest'))
//		.get (routes.setAndRun (DocumentClass, function (model, req) {
//			return model.getDocumentVersions (req.Document);
//		}));
//	//
//	// getDocumentsInList              : '/api/documentlist', data:documentList
//	//
//	app.route ('/api/documentlist')
//		.all (policy ('guest'))
//		.put (routes.setAndRun (DocumentClass, function (model, req) {
//			return model.getList (req.body);
//		}));
	//
	// fetch a document (download multipart stream)
	//
	app.route ('/api/document/:document/fetch')
		.all (policy ('guest'))
		.get (function (req, res) {
			console.log("Document: " + req);
           
                
                var downloadurl = '/api/documents/files/' + req.params.document + '/download';

                console.log("downloadurl:", downloadurl);

                var options = {
                    host: config.dmservice,
                    port: 8080,
                    path: downloadurl
                };
                var http = require('http');
                http.get(options, function (response) {
                	 res.writeHead(200, {
                          'Content-Type': response.headers['content-type'],
                          'Content-Disposition': response.headers['content-disposition'],
                          'Content-Length': response.headers['content-length']
                     });
                    response.on('data', function (data) {
                        res.write(data);
                    }).on('end', function () {
                        res.end();                        
                    });
                });

            
		});
	//
	// upload comment document:  We do this to force the model as opposed to trusting the
	// 'headers' from an untrustworthy client.
	//
//	app.route ('/api/commentdocument/:project/upload')
//	.all (policy ('guest'))
//		.post (routes.setAndRun (DocumentClass, function (model, req) {
//			return new Promise (function (resolve, reject) {
//                var file = req.files.file;
//
//
//				if (file) {
//                    var opts = { oldPath: file.path, projectCode: req.Project.code };
//
//
//
//					routes.moveFile (opts)
//					.then (function (newFilePath) {
//						var theModel = model.create ({
//							// Metadata related to this specific document that has been uploaded.
//							// See the document.model.js for descriptions of the parameters to supply.
//							project                 : req.Project,
//							//projectID             : req.Project._id,
//							projectFolderType       : req.body.documenttype,//req.body.projectfoldertype,
//							projectFolderSubType    : req.body.documentsubtype,//req.body.projectfoldersubtype,
//							projectFolderName       : req.body.documentfoldername,
//							projectFolderURL        : newFilePath,
//							projectFolderDatePosted : Date.now(),
//							// NB                   : In EPIC, projectFolders have authors, not the actual documents.
//							projectFolderAuthor     : req.body.projectfolderauthor,
//							// These are the data as it was shown on the EPIC website.
//							documentAuthor          : req.body.documentauthor,
//							documentFileName        : req.body.documentfilename,
//							documentFileURL         : req.body.documentfileurl,
//							documentFileSize        : req.body.documentfilesize,
//							documentFileFormat      : req.body.documentfileformat,
//							documentIsInReview      : req.body.documentisinreview,
//							documentVersion         : 0,
//							documentSource			: 'COMMENT',
//							// These are automatic as it actually is when it comes into our system
//							internalURL             : newFilePath,
//							internalOriginalName    : file.originalname,
//							internalName            : file.name,
//							internalMime            : file.mimetype,
//							internalExt             : file.extension,
//							internalSize            : file.size,
//							internalEncoding        : file.encoding,
//							directoryID             : req.body.directoryid || 0
//                        });                      
//
//                        return theModel;
//					})
//					.then (resolve, reject);
//				}
//				else {
//					reject ("no file to upload");
//				}
//			});
//		}));
	//
	// upload document
	//
	app.route ('/api/document/:project/upload').all (policy ('guest'))
		.post (routes.setAndRun (DocumentClass, function (model, req) {      
            return new Promise(function (resolve, reject) {
                console.log("incoming upload");
                var file = req.files.file;
                if (file && file.originalname === 'this-is-a-file-that-we-want-to-fail.xxx') {
                    reject('Fail uploading this file.');
                } else if (file) {
                    var opts = { oldPath: file.path, projectCode: req.Project.code };


                    /* upload to the document management system. */
                    var superagent = require('superagent');
                    var agent1 = superagent.agent();
                    var itemid = "";
                    var bearer_token = req.headers.authorization; 
                    var dmsurl = 'http://' + config.dmservice + ':8080/api/documents/files/content';
                    console.log("DMS URL is " + dmsurl);
                    console.log("File.path is " + file.path);
                    agent1.post(dmsurl)
                        .attach('file', file.path)
                        .set('Authorization', bearer_token)
                        .end(function (err, res) {
                            if (err) {
                                console.log(err);
                            }
                            var jsonResponse = JSON.parse(res.text);
                            console.log("response is " + res.text);

                           // itemid = res.text.substring(1, res.text.length - 1);
                            itemid = jsonResponse.itemID;    
                            console.log("itemid is " + itemid);
							resolve();
                        });
                }
				else {
					reject ("no file to upload");
				}
			});
		}));
//    app.route('/api/document/makeLatest/:document').all(policy('user'))
//        .put(routes.setAndRun(DocumentClass, function (model, req) {
//            return model.makeLatest(req.Document);
//        })); 
    
    app.put('/api/document/publish/:document', function (req, response) {
		var bearer_token = req.headers.authorization;
		
		var dmsurl = 'http://' + config.dmservice + ':8080/api/documents/';
		
		if(req.body.type === "File") {
			dmsurl += 'files/';
		} else {
			dmsurl += 'folders/';
		}
		
		dmsurl += req.params.document;
		
    	console.log("DMSURL: " + dmsurl);
    	var agent = superagent.agent(); 
    	
    	var result = agent.put(dmsurl)
    	.send({"generalVisibility":"ExternallyVisible", "ocioSecurityClassification" : "PUBLIC"})
    	.set('Authorization', bearer_token)
    	.set('Content-Type','application/json')
    	.end(function (err, res) {
			if (err) {
				console.log(err);
				return response.json({});
			}
			var files = [];
			var obj = JSON.parse(res.text);
			if (obj.files && obj.files.fileList) {
				return response.json(obj.files.fileList);
			} else {
				return response.json({});
			}
		});
    	
	});
    
    app.put('/api/document/unpublish/:document', function (req, response) {
		var bearer_token = req.headers.authorization;
		var dmsurl = 'http://' + config.dmservice + ':8080/api/documents/';
		
		if(req.body.type === "File") {
			dmsurl += 'files/';
		} else {
			dmsurl += 'folders/';
		}
		
		dmsurl += req.params.document;
		
    	console.log("DMSURL: " + dmsurl);
    	
    	var agent = superagent.agent(); 
    	
    	var result = agent.put(dmsurl)
    	.send({"generalVisibility":"InternalOnly", "ocioSecurityClassification" : "CONFIDENTIAL"})
    	.set('Authorization', bearer_token)
    	.set('Content-Type','application/json')
    	.end(function (err, res) {
			if (err) {
				console.log("Error occurred");
				console.log(err);
				return response.json({});
			}
			var files = [];
			var obj = JSON.parse(res.text);
			if (obj.files && obj.files.fileList) {
				return response.json(obj.files.fileList);
			} else {
				return response.json({});
			}
		});
    	
	});
    
//	app.route('/api/getDocumentByEpicURL').all(policy('guest'))
//		.put(routes.setAndRun(DocumentClass, function (model, req) {
//			return model.getEpicProjectFolderURL(req.body);
//		}));

};

