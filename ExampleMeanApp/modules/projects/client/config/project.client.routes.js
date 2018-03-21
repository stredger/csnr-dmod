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
	// -------------------------------------------------------------------------
	//
	// the detail view of a project
	//
	// -------------------------------------------------------------------------
//	.state('p.detail', {
//		url: '/detail',
//		templateUrl: 'modules/projects/client/views/project-partials/project.detail.html',
//		resolve: {
//			project: function ($stateParams, ProjectModel) {
//				return ProjectModel.byCode ($stateParams.projectid);
//			},
//			activeperiod: function ($stateParams, CommentPeriodModel, project) {
//				if (!project) { return null; }
//				// Go through the periods on the project, surface the active one and enable commenting
//				// right from here.
//				// The following code is duplicated in commentperiod.routes.js
//				return CommentPeriodModel.forProject (project._id)
//				.then( function (periods) {
//					var openPeriod = null;
//					_.each(periods, function (period) {
//						if (period.openState.state === CommentPeriodModel.OpenStateEnum.open) {
//							openPeriod = period;
//							return false;
//						}
//					});
//					if (openPeriod) {
//						// console.log("Found open period:", openPeriod);
//						return openPeriod;
//					} else {
//						return null;
//					}
//				});
//			}
//		},controller: function ($scope, $state, project, ProjectModel, $window, activeperiod) {
//			$scope.project = project;
//			$scope.activeperiod = null;
//
//			if (activeperiod) {
//				// Switch on the UI for comment period
//				// console.log("activeperiod:", activeperiod);
//				$scope.activeperiod = activeperiod;
//				$scope.allowCommentSubmit = (activeperiod.userCan.addComment) || activeperiod.userCan.vetComments;
//			}
//
//			// complete the current phase.
//			$scope.publishProject = function() {
//				ProjectModel.publishProject( project ).then( function(res) {
//					$scope.project = res;
//					$state.go($state.current, {}, {reload: true});
//				});
//			};
//		}
//	})
//	// -------------------------------------------------------------------------
//	//
//	// the detail view of a project
//	//
//	// -------------------------------------------------------------------------
//	.state('p.edit', {
//		url: '/edit',
//		templateUrl: 'modules/projects/client/views/project-partials/project.entry.html',
//		controller: 'controllerProjectEntry',
//		data: { }
//	});
}]);











