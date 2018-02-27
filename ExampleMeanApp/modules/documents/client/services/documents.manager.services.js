'use strict';

angular.module('documents').service('DocumentMgrService', documentMgrService);
documentMgrService.$inject = ['$http'];
/* @ngInject */
function documentMgrService($http) {

	// PUT /api/project/:project/directory/add/:parentid'
	// Body: foldername: foldername
	var headers = null;
	var bearer_token = window.localStorage.getItem('access_token');
	if (bearer_token) {
		headers = { "Authorization": "Bearer " + bearer_token };
	}

	var addDirectory = function(project, parentDir, newdirname) {
		return $http({method:'PUT', url: '/api/project/' + project._id + '/directory/add/' + parentDir.model.id, data: {foldername: newdirname}, headers: headers});
	};

	// PUT /api/project/:project/directory/rename/:folderid')
	// Body: foldername: newname

//	var renameDirectory = function(project, dir, newname) {
//		return $http({method:'PUT', url: '/api/project/' + project._id + '/directory/rename/' + dir.model.id, data: {foldername: newname}, headers: headers});
//	};
//
//	// PUT /api/project/:project/directory/remove/:folderid
//
//	var removeDirectory = function(project, dir) {
//		return $http({method:'PUT', url: '/api/project/' + project._id + '/directory/remove/' + dir.model.id, data: {}, headers: headers});
//	};
//
//	// PUT /api/project/:project/directory/move/:folderid/:newparentid
//
//	var moveDirectory = function(project, sourceDir, destDir) {
//		return $http({method:'PUT', url: '/api/project/' + project._id + '/directory/move/' + sourceDir.model.id + '/' + destDir.model.id, data: {}, headers: headers});
//	};


	var getDirectoryDocuments = function (project, directoryID) {
		// return $http({method: 'GET', url: '/api/query/document?project=' + project._id.toString() + '&directoryID=' + directoryID.toString(), headers: headers});
		return $http({method: 'GET', url: '/api/documents?directoryID=' + directoryID, headers: headers});
	};

	var publish = function (document) {
		return $http({method:'PUT', url: '/api/document/publish/' + document._id, data:{"type": document.type}, headers: headers});
	};
	
	var unpublish = function (document) {
		return $http({method:'PUT', url: '/api/document/unpublish/' + document._id, data:{"type" : document.type}, headers: headers});
		
	};

	return {
		addDirectory: addDirectory,
	//	renameDirectory: renameDirectory,
	//	removeDirectory: removeDirectory,
	//	moveDirectory: moveDirectory,
		getDirectoryDocuments: getDirectoryDocuments,
		publish: publish,
		unpublish: unpublish
	};
}
