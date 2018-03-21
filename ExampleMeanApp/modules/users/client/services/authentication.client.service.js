'use strict';

// Authentication service for user variables
angular.module('users').factory('Authentication', ['$window', '$rootScope',
  function ($window, $rootScope) {
    var auth = {
      user: $window.user,
      token: $window.localStorage.getItem('access_token')
    };

    $rootScope.$watch(function(){
      return $window.localStorage.getItem('access_token');
    }, function(new_token, old_token){
      auth.token = new_token;
    });

    return auth;
  }
]);
