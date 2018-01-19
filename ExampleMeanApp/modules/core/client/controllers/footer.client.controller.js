'use strict';

angular.module('core')
	.controller('controllerFooter', controllerFooter);
	
// -----------------------------------------------------------------------------------
//
// Controller Footer
//
// -----------------------------------------------------------------------------------
controllerFooter.$inject = ['Authentication', '$rootScope'];
/* @ngInject */
function controllerFooter(Authentication, $rootScope) {
	var footer = this;
	footer.authentication = Authentication;
	footer.side = true;
   
	footer.loginLink = "https://i1auth.nrs.gov.bc.ca/pub/webade-oauth2/oauth/authorize?response_type=token&client_id=DMOD_UI&redirect_uri=";
	footer.loginLink += window.location.origin + '/webade-oauth2';


	footer.signout = function () {
		// Destroy the token
		window.localStorage.removeItem('access_token');
		window.location.href = "/api/auth/signout";
	};

	$rootScope.$on('$stateChangeSuccess', 
	function(event, toState, toParams, fromState, fromParams){ 
	// sidebar if user is logged in or on a project page.
            if(Authentication.user) {
                Authentication.user.roles = ['admin','user','eao'];
            } 
	    footer.side = (!!Authentication.user || toState.name.match(/^p\./i)) && toState.name !== 'not-found';
	});

	
}
