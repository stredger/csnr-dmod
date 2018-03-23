'use strict';
// =========================================================================
//
// This routes calls the Document Service
//
// =========================================================================

var policy = require('../../../core/server/controllers/core.policy.controller');
var config = require('../../../../config/config');
var _ = require('lodash');
var superagent = require('superagent');

module.exports = function (app) {

	app.route ('/api/documents')
		.all (policy ('guest'))
		.get (function (req, response) {
			var agent1 = superagent.agent();
			var bearer_token = req.headers.authorization; 
			console.log("params:", req.query.directoryID);

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

				if (obj.files && obj.files.fileList) {
					return response.json(obj.files.fileList);
				} else {
					return response.json({});
				}
			});
		});

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
	
	//delete directory
	app.delete ('/api/document/:document/directory/delete', function (req, response) {      

        /* upload to the document management system. */
        var agent1 = superagent.agent();
        var itemid = "";
        var bearer_token = req.headers.authorization; 
        var dmsurl = 'http://' + config.dmservice + ':8080/api/documents/folders/' + req.params.document;
        
        
        console.log("DMS URL is " + dmsurl);
        agent1.delete(dmsurl)
            .set('Authorization', bearer_token)
            .end(function (err, res) {
				if (err) {
					console.log(err);
					return err;
				}
              var jsonResponse = JSON.parse(res.text);
              console.log("response is " + res.text);

              return response.json(jsonResponse);
			});

	});
	
	

	    //upload directory
		app.post ('/api/document/:project/upload/:directoryID', function (req, response) {      
                var file = req.files.file;
                
                if (file) {
                    /* upload to the document management system. */
                    var agent1 = superagent.agent();
                    var itemid = "";
                    var bearer_token = req.headers.authorization; 
                    var dmsurl = 'http://' + config.dmservice + ':8080/api/documents';
                    
                    if(req.params.directoryID !== '1') {
                    	dmsurl += '/folders/' + req.params.directoryID;
                    } 
                    
                    dmsurl += '/files/content';
                    
                    console.log("DMS URL is " + dmsurl);
                    agent1.post(dmsurl)
                        .attach('file', file.path)
                        .field('name', file.originalname)
                        .set('Authorization', bearer_token)
                        .end(function (err, res) {
							if (err) {
								console.log("Error: " + err);
								return err;
							}
                          var jsonResponse = JSON.parse(res.text);
                          console.log("response is " + res.text);

                          return response.json({"data":jsonResponse.itemID});
						});

                }

		});
		
		//create folder
		app.post ('/api/document/:project/directory/add/:directoryID', function (req, response) {      

            var agent1 = superagent.agent();
            var itemid = "";
            var bearer_token = req.headers.authorization; 
            var dmsurl = 'http://' + config.dmservice + ':8080/api/documents/folders';
            
            if(req.params.directoryID !== '1') {
            	dmsurl += '/' + req.params.directoryID;
            } 
            
            console.log("DMS URL is " + dmsurl);
            agent1.post(dmsurl)
            	.send({"name":req.body.foldername})
                .set('Authorization', bearer_token)
                .end(function (err, res) {
					if (err) {
						console.log(err);
						response.status(500);
						return response.json({"data: " : err });
					}
                  var jsonResponse = JSON.parse(res.text);
                  console.log("response is " + res.text);

                  return response.json({"data":jsonResponse});
				});
		});	
		
		

	
	  app.put('/api/document/:document/expire', function (req, response) {
			var bearer_token = req.headers.authorization;
			
			var dmsurl = 'http://' + config.dmservice + ':8080/api/documents/files/' + req.params.document;
	    	var agent = superagent.agent(); 
	    	var isoDate = new Date().toISOString();
	    	    	
	    	var result = agent.put(dmsurl)
	    	.send({"expiryDate":isoDate})
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
    
    app.put('/api/document/:document/publish', function (req, response) {
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
    
    app.put('/api/document/:document/unpublish', function (req, response) {
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
    

};

