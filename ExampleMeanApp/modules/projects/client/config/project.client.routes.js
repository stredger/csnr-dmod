'use strict';

angular.module('project').config (
	['$locationProvider', '$stateProvider', '$urlRouterProvider', '_',
	function ($locationProvider, $stateProvider, $urlRouterProvider, _) {

	$stateProvider
	.state('p', {
		url: '/p/:projectid',
		abstract: true,
		templateUrl: 'modules/projects/client/views/project.abstract.html',
		resolve: {
			project: function ($stateParams, ProjectModel) {
				//console.log ('> loading project /p id = ' + $stateParams.projectid);
				if ($stateParams.projectid === 'new') {
					return ProjectModel.getNew ();
				} else {
					return ProjectModel.byCode ($stateParams.projectid);
				}
			},
			eaoAdmin: function () {
				return '';//project.adminRole;
			},
			proponentAdmin: function () {
				return '';//project.proponentAdminRole;
			}
		},
		controller: function ($scope, $stateParams, project, ENV, $rootScope, ProjectModel, Menus) {
			//console.log ('< loaded project /p id = ' + $stateParams.projectid + ', userCan = ', JSON.stringify(project.userCan));
			$scope.project = project;
			$scope.environment = ENV;
			$scope.isNew = ($stateParams.projectid === 'new');

			ProjectModel.setModel(project);

			$scope.intakeQuestions = ProjectModel.getProjectIntakeQuestions();


			var unbind = $rootScope.$on('refreshProject', function() {
				// console.log('refreshProject', $stateParams.projectid);
				$scope.project = angular.copy( ProjectModel.byCode ($stateParams.projectid) );
			});
			$scope.$on('$destroy',unbind);

		},
		data: { }
	});
}]);











