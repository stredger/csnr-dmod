'use strict';

angular.module('documents').service('DocumentMgrService', documentMgrService);
documentMgrService.$inject = ['$http'];
/* @ngInject */
function documentMgrService($http) {

	var headers = null;
	var bearer_token = window.localStorage.getItem('access_token');
	if (bearer_token) {
		headers = { "Authorization": "Bearer " + bearer_token };
	}

	var addDirectory = function(project, parentDir, newdirname) {
		return $http({method:'POST', url: '/api/document/' + project._id + '/directory/add/' + parentDir.model.id, data: {foldername: newdirname}, headers: headers});
	};

	
    var deleteDocument = function (document) {
    	return $http({method:'PUT', url: '/api/document/' + document._id +'/expire', headers: headers});
    };

    var deleteDir = function (directoryID) {
    	return $http({method:'DELETE', url: '/api/document/'+ directoryID +'/directory/delete', headers: headers});
    };
    
	var getDirectoryDocuments = function (project, directoryID) {
		return $http({method: 'GET', url: '/api/documents?directoryID=' + directoryID, headers: headers});
	};

	var publish = function (document) {
		return $http({method:'PUT', url: '/api/document/' + document._id +'/publish', data:{"type": document.type}, headers: headers});
	};
	
	var unpublish = function (document) {
		return $http({method:'PUT', url: '/api/document/' + document._id +'/unpublish', data:{"type" : document.type}, headers: headers});
		
	};

	return {
		addDirectory: addDirectory,
		deleteDir: deleteDir,
		deleteDocument: deleteDocument,
		getDirectoryDocuments: getDirectoryDocuments,
		publish: publish,
		unpublish: unpublish
	};
}
