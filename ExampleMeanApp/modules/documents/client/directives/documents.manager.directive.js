'use strict';
angular.module('documents')

	.directive('documentMgr', ['_', 'moment', 'Authentication', 'DocumentMgrService', 'AlertService', 'ConfirmService',  'TreeModel', 'ProjectModel', 'FolderModel', function (_, moment, Authentication, DocumentMgrService, AlertService, ConfirmService, TreeModel, ProjectModel, FolderModel) {
		return {
			restrict: 'E',
			scope: {
				project: '=',
				opendir: '='
			},
			templateUrl: 'modules/documents/client/views/document-manager.html',
			controller: function ($scope, $filter, $log, $modal, $timeout, _, moment, Authentication, DocumentMgrService, TreeModel, ProjectModel) {
				var tree = new TreeModel();
				var self = this;
				self.busy = true;

				if ($scope.opendir) {
					try {
						self.opendir = $scope.opendir.substr(1,$scope.opendir.length - 1);
						self.opendir = self.opendir.split('=');
						self.opendir = parseInt(self.opendir[1]);
					} catch (e) {
						console.log("couldn't parse directory");
					}
					self.openDir = null;
				}

				$scope.authentication = Authentication;

				ProjectModel.getProjectDirectory($scope.project)
				.then( function (dir) {
					$scope.project.directoryStructure = dir || {
						id: 1,
						lastId: 1,
						name: 'ROOT',
						published: true
					};

					self.rootNode = tree.parse($scope.project.directoryStructure);


					if (self.opendir) {
						console.log("Going to directory:", self.opendir);
						self.selectNode(self.opendir);
					} else {
						self.selectNode(self.rootNode);
					}

					$scope.$apply();
				});

				// default sort is by name ascending...
				self.sorting = {
					column: 'name',
					ascending: true
				};

				// self.rootNode = tree.parse($scope.project.directoryStructure);
				self.selectedNode = undefined;
				self.currentNode = undefined;
				self.currentPath = undefined;

				self.allChecked = false;
				self.checkedDirs = [];
				self.checkedFiles = [];
				self.lastChecked = {fileId: undefined, directoryID: undefined};

				self.unsortedFiles = [];
				self.unsortedDirs = [];

				self.currentFiles = [];
				self.currentDirs = [];

				self.batchMenuEnabled = false;

				self.infoPanel = {
					open: false,
					type: 'None',
					data: undefined,
					toggle: function() {
						self.infoPanel.open = !self.infoPanel.open;
					},
					close: function() {
						self.infoPanel.open = false;
					},
					reset: function() {
						//self.infoPanel.enabled = false;
						//self.infoPanel.open = false;
						self.infoPanel.type = 'None';
						self.infoPanel.data = undefined;
					},
					setData: function() {
						self.infoPanel.reset();
						// check to see if there is a single lastChecked item set first...
						if (self.lastChecked) {
							if (self.lastChecked.fileId) {
								self.infoPanel.type = 'File';
								
								var file = _.find(self.currentFiles, function(o) {
									return o.itemID === self.lastChecked.fileId;
								});
								self.infoPanel.data = file ? file : undefined;
							} else if (self.lastChecked.directoryID) {
								self.infoPanel.type = 'Directory';
								var node =_.find(self.currentDirs, function(o) { return o.model.id === self.lastChecked.directoryID; });
								self.infoPanel.data = node ? node.model : undefined;
							}
						} else {
							if (_.size(self.checkedDirs) + _.size(self.checkedFiles) > 1) {
								self.infoPanel.type = 'Multi';
								self.infoPanel.data = {
									checkedFiles: _.size(self.checkedFiles),
									checkedDirs: _.size(self.checkedDirs),
									totalFiles: _.size(self.currentFiles),
									totalDirs: _.size(self.currentDirs)
								}; // what to show here?
							}
						}
					}
				};

				self.sortBy = function(column) {
					//is this the current column?
					if (self.sorting.column.toLowerCase() === column.toLowerCase()){
						//so we reverse the order...
						self.sorting.ascending = !self.sorting.ascending;
					} else {
						// changing column, set to ascending...
						self.sorting.column = column.toLowerCase();
						self.sorting.ascending = true;
					}
					self.applySort();
				};

				self.applySort = function() {
					// sort ascending first...
					self.currentFiles = _(self.unsortedFiles).chain().sortBy(function (f) {
						// more making sure that the displayName is set...
						if (_.isEmpty(f.displayName)) {
							f.displayName = f.documentFileName || f.internalOriginalName;
						}
						f.order = f.order || 0;

						if (self.sorting.column === 'name') {
							return _.isEmpty(f.displayName) ? null : f.displayName.toLowerCase();
						} else if (self.sorting.column === 'author') {
							return _.isEmpty(f.documentAuthor) ? null : f.documentAuthor.toLowerCase();
						} else if (self.sorting.column === 'type') {
							return _.isEmpty(f.internalExt) ? null : f.internalExt.toLowerCase();
						} else if (self.sorting.column === 'size') {
							return _.isEmpty(f.internalExt) ? 0 : f.fileSize;
						} else if (self.sorting.column === 'date') {
							//date uploaded
							return _.isEmpty(f.dateUploaded) ? 0 : f.dateUploaded;
						} else if (self.sorting.column === 'pub') {
							//is published...
							return !f.isPublished;
						}
						// by name if none specified... or we incorrectly identified...
						return _.isEmpty(f.displayName) ? null : f.displayName.toLowerCase();
					}).sortBy(function (f) {
						return f.order;
					}).value();

					// directories always/only sorted by name
					self.currentDirs = _(self.unsortedDirs).chain().sortBy(function (d) {
						if (_.isEmpty(d.model.name)) {
							return null;
						}
						d.model.order = d.model.order || 0;
						return d.model.name.toLowerCase();
					}).sortBy(function (d) {
						return d.model.order;
					}).value();

					if (!self.sorting.ascending) {
						// and if we are not supposed to be ascending... then reverse it!
						self.currentFiles = _(self.currentFiles).reverse().value();
						if (self.sorting.column === 'name') {
							// name is the only sort that applies to Directories.
							// so if descending on name, then we need to reverse it.
							self.currentDirs = _(self.currentDirs).reverse().value();
						}
					}
				};

				self.checkAll = function() {
					_.each(self.currentDirs, function(o) { o.selected = self.allChecked; });
					_.each(self.currentFiles, function(o) { o.selected = self.allChecked; });

					var doc;
					if (self.allChecked) {
						doc = _.last(self.currentFiles) || _.last(self.currentDirs);
					}

					self.syncCheckedItems(doc);
				};

				self.checkFile = function(doc) {
					// ADD/remove to the selected file list...
					self.syncCheckedItems(doc);
				};
				self.selectFile = function(doc) {
					// selected a file, make it the only item selected...
					var checked = doc.selected;
					_.each(self.currentDirs, function(o) { o.selected = false; });
					_.each(self.currentFiles, function(o) { o.selected = false; });
					doc.selected = !checked;
					self.syncCheckedItems(doc);
				};

				self.dblClick = function(doc){
					/*
					If user can not read the document (BG: I'm not sure this is possible) then show an alert to say
					"You can not read this document" (BG: someone needs to review the text)
					Else (user can read file)
						If the doc is a pdf then open it with the pdf viewer
						Else show a confirmation dialog to offer the user can download the file.
							If user selects yes then download the file.
							Else no op
					 */
					if(!$scope.authentication.token) {
						AlertService.success('You can not have access to read this document.');
						return;
					}
					if(doc.internalMime === 'application/pdf') {
						openPDF(doc);
						return;
					}
					// $filter bytes is filterBytes in documents.client.controllers.js
					var size = doc.fileSize;
					var msg = 'Confirm download of: ' + doc.displayName + ' (' + size + ')';

					var scope = {
						titleText: doc.displayName,
							confirmText: msg,
							confirmItems: undefined,
							okText: 'OK',
							cancelText: 'Cancel',
							onOk: downLoadFile,
							onCancel: cancelDownload,
							okArgs: doc
					};
					ConfirmService.confirmDialog(scope);
					return;

					function downLoadFile(doc) {
						var pdfURL = window.location.protocol + "//" + window.location.host + "/api/document/" + doc._id + "/fetch";
						window.open(pdfURL, "_self");
						return Promise.resolve(true);
					}
					function cancelDownload() {
						return Promise.resolve();
					}
					function openPDF(doc){
						var modalDocView = $modal.open({
							resolve: {
								pdfobject: { _id: doc._id }
							},
							templateUrl: 'modules/documents/client/views/partials/pdf-viewer.html',
							controller: 'controllerModalPdfViewer',
							controllerAs: 'pdfViewer',
							windowClass: 'document-viewer-modal'
						});
						modalDocView.result.then(function () {}, function () {});
					}
				};

				self.checkDir = function(doc) {
					self.syncCheckedItems(doc);
				};
				self.selectDir = function(doc) {
					// selected a dir, make it the only item selected...
					var checked = doc.selected;
					_.each(self.currentDirs, function(o) { o.selected = false; });
					_.each(self.currentFiles, function(o) { o.selected = false; });
					doc.selected = !checked;
					self.syncCheckedItems(doc);
				};
				self.openDir = function(doc) {
					var theNode = self.rootNode.first(function (n) {
						return n.model.id === doc.model.id;
					});
					if (!theNode) {
						theNode = self.rootNode;
					}
					self.currentNode = theNode;
					
					FolderModel.lookupForProjectIn($scope.project._id, doc.model.id)
					.then(function (folder) {
						console.log("FOLDER:", folder);
						self.currentNode.children = [];
						_.each(folder, function (fs) {
					    	
							var newnode = tree.parse({id: fs.itemID,
									_id: fs.itemID,
									name: fs.name,
									displayName: fs.name,
									documentDate: fs.lastModifiedDate,
									published: fs.securityMetadata.generalVisibility === "ExternallyVisible"
							});
							
							self.currentNode.addChild(newnode);

						});
						
						self.selectNode(doc.model.id);
					});	
					
				};

				self.selectNode = function (nodeId) {
					self.busy = true;
					var theNode = self.rootNode.first(function (n) {
						return n.model.id === nodeId;
					});
					if (!theNode) {
						theNode = self.rootNode;
					}

					self.currentNode = theNode; 
					self.folderURL = window.location.protocol + "//" + window.location.host + "/p/" + $scope.project.code + "/docs?folder=" + self.currentNode.model.id;
					self.currentPath = theNode.getPath() || [];
					self.unsortedFiles = [];
					self.unsortedDirs = [];
					self.currentFiles = [];
					self.currentDirs = [];
					
					DocumentMgrService.getDirectoryDocuments($scope.project, self.currentNode.model.id )
					.then(
						function (result) {

							self.unsortedFiles = _.map(result.data, function(f) {

								if (_.isEmpty(f.displayName)) {
									f.displayName = f.filename;
								}
								f._id = f.itemID;
								
								var extIndex = f.filename.lastIndexOf(".");
								var ext = f.filename.substring(extIndex+1, f.filename.length);
								f.internalExt = ext;
								
								f.isPublished= (f.fileMetadata.securityMetadata.generalVisibility === "ExternallyVisible" && f.fileMetadata.ocioSecurityClassification === "PUBLIC");
								return _.extend(f,{selected:  (_.find(self.checkedFiles, function(d) { return d._id.toString() === f._id.toString(); }) !== undefined), type: 'File'});
							});

							self.unsortedDirs = _.map(self.currentNode.children, function (n) {
								return _.extend(n,{selected: (_.find(self.checkedDirs, function(d) { return d.model.id === n.model.id; }) !== undefined), type: 'Directory'});
							});
														
							self.applySort();
							// since we loaded this, make it the selected node
							self.selectedNode = self.currentNode;

							// see what is currently checked
							self.syncCheckedItems();
							self.busy = false;
						},
						function (error) {
							$log.error('getDirectoryDocuments error: ', JSON.stringify(error));
							self.busy = false;
						}
					);
				};
			   

				self.syncCheckedItems = function(doc) {
					self.checkedDirs = _.filter(self.currentDirs, function(o) { return o.selected; }) || [];
					self.checkedFiles = _.filter(self.currentFiles, function(o) { return o.selected; }) || [];
					// any kind of contexts that depend on what is selected needs to be done here too...
					self.lastChecked = undefined;
					if (doc && doc.selected && (_.size(self.checkedDirs) + _.size(self.checkedFiles) === 1)){
						// console.log("DOC:", doc);
						if (doc.model) {
							self.lastChecked = { directoryID: doc.model.id, fileId: undefined };
						} else {
							self.lastChecked = { directoryID: undefined, fileId: doc.itemID };
						}
					}
					if (!doc && (_.size(self.checkedDirs) + _.size(self.checkedFiles) === 1)){
						// if no doc passed in, but there is a single selected item, make it lastSelected
						// most probable case is a selectNode after a context menu operation...
						if (_.size(self.checkedDirs) === 1) {
							self.lastChecked = { directoryID: self.checkedDirs[0].model.id, fileId: undefined };
						} else {
							self.lastChecked = { directoryID: undefined, fileId: self.checkedFiles[0]._id.toString() };
						}
					}
					self.infoPanel.setData();
					self.deleteSelected.setContext();
					self.publishSelected.setContext();

					// in the batch menu, we have some folder management and publish/unpublish of files.
					// so user needs to be able to manage folders, or have some selected files they can pub/unpub
					self.batchMenuEnabled = ($scope.authentication.token && _.size(self.checkedDirs) > 0) || _.size(self.publishSelected.publishableFiles) > 0 || _.size(self.publishSelected.unpublishableFiles) > 0;
				};

				self.deleteDocument = function(document) {
					return DocumentMgrService.deleteDocument(document)
					.then(function(result) {
						return null;
					}, function(err) {
						self.busy = false;
						AlertService.error('Error deleting document.');
					});
				};

				self.deleteDir = function(folder) {
					self.busy = true;
					return DocumentMgrService.deleteDir(folder.model.id)
					.then(function(result) {
						//update the directoryStructure
						$scope.project.directoryStructure.children.forEach(function(item, index, object) {
							  if (item.id === folder.model.id) {
							    object.splice(index, 1);
							  }
						});
						
						$scope.$broadcast('documentMgrRefreshNode', { directoryStructure: $scope.project.directoryStructure });
						
						self.busy = false;
						AlertService.success('The selected folder was deleted.');
					}, function(err) {
						self.busy = false;
						AlertService.error('Error deleting folder. Check that there are no files in the folder.');
					});
				};

				self.deleteFile = function(doc) {
					self.busy = true;
					return self.deleteDocument(doc)
						.then(function(result) {
							self.selectNode(self.currentNode.model.id); // will mark as not busy...
							var name = doc.displayName || doc.documentFileName || doc.internalOriginalName;
							AlertService.success('Delete File', 'The selected file was deleted.');
						}, function(error) {
							$log.error('deleteFile error: ', JSON.stringify(error));
							self.busy = false;
							AlertService.error('The selected file could not be deleted.');
						});
				};

				self.deleteSelected = {
					titleText: 'Delete File(s)',
					okText: 'Yes',
					cancelText: 'No',
					ok: function() {
						/*
							Here the user has selected OK on the confirm dialog. We need to show the progress which is behind the
							confirm dialog. To do this we'll place the long running task in a setImmediate and return from this
							ok method.
						*/
						var dirs = _.size(self.checkedDirs);
						var files = _.size(self.checkedFiles);
						if (dirs === 0 && files === 0) {
							return Promise.resolve();
						} else {
							$timeout(doDelete, 10);
							return Promise.resolve();
						}
						// do the work ....
						function doDelete() {
							self.busy = true;

							var dirPromises = _.map(self.deleteSelected.deleteableFolders, function(d) {
								return DocumentMgrService.removeDirectory($scope.project, d);
							});

							var filePromises = _.map(self.deleteSelected.deleteableFiles, function(f) {
								return self.deleteDocument(f);
							});

							var directoryStructure;
							return Promise.all(dirPromises)
								.then(function(result) {
									//$log.debug('Dir results ', JSON.stringify(result));
									if (!_.isEmpty(result)) {
										var last = _.last(result);
										directoryStructure = last.data;
									}
									return Promise.all(filePromises);
								})
								.then(function(result) {
									//$log.debug('File results ', JSON.stringify(result));
									if (directoryStructure) {
										//$log.debug('Setting the new directory structure...');
										$scope.project.directoryStructure = directoryStructure;
										$scope.$broadcast('documentMgrRefreshNode', { directoryStructure: directoryStructure });
									}
									//$log.debug('Refreshing current directory...');
									self.selectNode(self.currentNode.model.id);
									self.busy = false;
									AlertService.success('The selected items were deleted.');
								}, function(err) {
									self.busy = false;
									AlertService.error('The selected items could not be deleted.');
								});
						}
					},
					cancel: undefined,
					confirmText:  'Are you sure you want to delete the selected item(s)?',
					confirmItems: [],
					deleteableFolders: [],
					deleteableFiles: [],
					setContext: function() {
						self.deleteSelected.confirmItems = [];
						self.deleteSelected.titleText = 'Delete selected';
						self.deleteSelected.confirmText = 'Are you sure you want to delete the following the selected item(s)?';
						var dirs = _.size(self.checkedDirs);
						var files = _.size(self.checkedFiles);
						if (dirs > 0 && files > 0) {
							self.deleteSelected.titleText = 'Delete Folder(s) and File(s)';
							self.deleteSelected.confirmText = 'Are you sure you want to delete the following ('+ dirs +') folders and ('+ files +') files?';
						} else if (dirs > 0) {
							self.deleteSelected.titleText = 'Delete Folder(s)';
							self.deleteSelected.confirmText = 'Are you sure you want to delete the following ('+ dirs +') selected folders?';
						} else if (files > 0) {
							self.deleteSelected.titleText = 'Delete File(s)';
							self.deleteSelected.confirmText = 'Are you sure you want to delete the following ('+ files +') selected files?';
						}

						self.deleteSelected.confirmItems = [];
						self.deleteSelected.deleteableFolders = [];
						self.deleteSelected.deleteableFiles = [];

						_.each(self.checkedDirs, function(o) {
							if ($scope.authentication.token) {
								self.deleteSelected.confirmItems.push(o.model.name);
								self.deleteSelected.deleteableFolders.push(o);
							}
						});
						_.each(self.checkedFiles, function(o) {
							 if ($scope.authentication.token) {
							 	var name = o.displayName || o.documentFileName || o.internalOriginalName;
							 	self.deleteSelected.confirmItems.push(name);
							 	self.deleteSelected.deleteableFiles.push(o);
							 }
						});

					}
				};

				self.publishFiles = function(files) {
					self.busy = true;
					var filePromises = _.map(files, function(f) {
						return DocumentMgrService.publish(f);
					});
					return Promise.all(filePromises)
						.then(function(result) {
							//$log.debug('Publish File results ', JSON.stringify(result));
							//$log.debug('Refreshing current directory...');
							var published = _.map(result, function(o) { if (o.isPublished) return o.displayName || o.documentFileName || o.internalOriginalName; });
							var unpublished = _.map(result, function(o) { if (!o.isPublished) return o.displayName || o.documentFileName || o.internalOriginalName; });
							self.selectNode(self.currentNode.model.id);
							AlertService.success(_.size(published) + ' of ' + _.size(files) + ' files successfully published.');
						}, function(err) {
							self.busy = false;
							AlertService.error('The selected files could not be published.');
						});
				};

				self.unpublishFiles = function(files) {
					self.busy = true;
					var filePromises = _.map(files, function(f) {
						return DocumentMgrService.unpublish(f);
					});
					return Promise.all(filePromises)
						.then(function(result) {
							//$log.debug('Unpublish File results ', JSON.stringify(result));
							//$log.debug('Refreshing current directory...');
							var published = _.map(result, function(o) { if (o.isPublished) return o.displayName || o.documentFileName || o.internalOriginalName; });
							var unpublished = _.map(result, function(o) { if (!o.isPublished) return o.displayName || o.documentFileName || o.internalOriginalName; });
							self.selectNode(self.currentNode.model.id);
							AlertService.success(_.size(unpublished) + ' of ' + _.size(files) + ' files successfully unpublished.');
						}, function(err) {
							self.busy = false;
							AlertService.error('The selected files could not be unpublished.');
						});
				};

				self.publishFolder = function(folder) {
					self.busy = true;
					return DocumentMgrService.publish(folder.model)
						.then(function(result) {
							
							//update folder status
							var childFolders =  _.map($scope.project.directoryStructure.children, function(o) {
								if(o.id === folder.model.id) {
									o.published = true;
								}
								
								return o;
							});
							
							$scope.project.directoryStructure.children = childFolders;
							
							$scope.$broadcast('documentMgrRefreshNode', { directoryStructure: $scope.project.directoryStructure });
							self.selectNode(self.currentNode.model.id);
							AlertService.success(folder.model.name + ' folder successfully published.');
						}, function(err) {
							self.busy = false;
							AlertService.error('The selected files could not be published.');
						});
				};
				
				
				self.unpublishFolder = function(folder) {
					self.busy = true;
					return  DocumentMgrService.unpublish(folder.model)
						.then(function(result) {
							
							//update folder status
							var childFolders =  _.map($scope.project.directoryStructure.children, function(o) {
								if(o.id === folder.model.id) {
									o.published = false;
								}
								
								return o;
							});
							
							$scope.project.directoryStructure.children = childFolders;
							
							$scope.$broadcast('documentMgrRefreshNode', { directoryStructure: $scope.project.directoryStructure });
							self.selectNode(self.currentNode.model.id);
							AlertService.success(folder.model.name + ' folder successfully un-published.');
						}, function(err) {
							self.busy = false;
							AlertService.error('The selected folder could not be unpublished.');
						});
				};


				self.publishFile = function(file) {
					return self.publishFiles([file]);
				};

				self.unpublishFile = function(file) {
					return self.unpublishFiles([file]);
				};

				self.publishSelected = {
					titleText: 'Publish File(s)',
					okText: 'Yes',
					cancelText: 'No',
					publish: function() {
						return self.publishFiles(self.publishSelected.publishableFiles);
					},
					unpublish: function() {
						return self.unpublishFiles(self.publishSelected.unpublishableFiles);
					},
					cancel: undefined,
					confirmText:  'Are you sure you want to publish the selected item(s)?',
					confirmItems: [],
					publishableFiles: [],
					unpublishableFiles: [],
					setContext: function() {
						self.publishSelected.confirmItems = [];
						self.publishSelected.publishableFiles = [];
						self.publishSelected.unpublishableFiles = [];
						// only documents/files....
						_.each(self.checkedFiles, function(o) {
							var canDoSomething = false;
							 if ($scope.authentication.token) {
								canDoSomething = true;
								self.publishSelected.publishableFiles.push(o);
							 }
							 if ($scope.authentication.token) {
								canDoSomething = true;
								self.publishSelected.unpublishableFiles.push(o);
							 }
							 if (canDoSomething) {
								var name = o.displayName || o.documentFileName || o.internalOriginalName;
								self.publishSelected.confirmItems.push(name);
							 }
						});

					}
				};


				self.onPermissionsUpdate = function() {
					//console.log('onPermissionsUpdate...');
					self.selectNode(self.currentNode.model.id);
				};

				self.onDocumentUpdate = function(value) {
					// should refresh the table and the info panel...
					//console.log('onDocumentUpdate...');
					self.selectNode(self.currentNode.model.id);
				};

				$scope.$on('documentMgrRefreshNode', function (event, args) {					
					if (args.nodeId) {
                        console.log('documentMgrRefreshNode...', args.nodeId);
						self.selectNode(args.nodeId);
                    } else if(args.directoryStructure) {
						self.rootNode = tree.parse(args.directoryStructure);
						self.selectNode(self.currentNode.model.id);
					} else if (args.newNode){
						var folder = args.newNode.data;
						var newnode = tree.parse({id: folder.itemID,
							_id: folder.itemID,
							name: folder.name,
							displayName: folder.name,
							documentDate: folder.lastModifiedDate,
							published: folder.securityMetadata.generalVisibility === "ExternallyVisible"});
						
						self.currentNode.addChild(newnode);
						self.selectNode(self.currentNode.model.id);
					}
				});
			},
			controllerAs: 'documentMgr'
		};
	}])
;
