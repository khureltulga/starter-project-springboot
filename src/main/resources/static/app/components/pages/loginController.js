angular
.module('altairApp')
.controller('loginCtrl', [
        '$scope',
        '$rootScope',
        '$http',
        '$state',
        'utils',
        function ($scope,$rootScope,$http,$state,utils) {

                        	  $scope.registerFormActive = false;

                        	  var $login_card = $('#login_card'),
                        	  $login_form = $('#login_form'),
                        	  $login_help = $('#login_help'),
                        	  $register_form = $('#register_form'),
                        	  $login_password_reset = $('#login_password_reset');

                        	  // show login form (hide other forms)
                        	  var login_form_show = function() {
                        		  $login_form
                        		  .show()
                        		  .siblings()
                        		  .hide();
                        	  };

                        	  // show register form (hide other forms)
                        	  var register_form_show = function() {
                        		  $register_form
                        		  .show()
                        		  .siblings()
                        		  .hide();
                        	  };

                        	  // show login help (hide other forms)
                        	  var login_help_show = function() {
                        		  $login_help
                        		  .show()
                        		  .siblings()
                        		  .hide();
                        	  };

                        	  // show password reset form (hide other forms)
                        	  var password_reset_show = function() {
                        		  $login_password_reset
                        		  .show()
                        		  .siblings()
                        		  .hide();
                        	  };

                        	  $scope.loginHelp = function($event) {
                        		  $event.preventDefault();
                        		  utils.card_show_hide($login_card,undefined,login_help_show,undefined);
                        	  };

                        	  $scope.backToLogin = function($event) {
                        		  $event.preventDefault();
                        		  $scope.registerFormActive = false;
                        		  utils.card_show_hide($login_card,undefined,login_form_show,undefined);
                        	  };

                        	  $scope.registerForm = function($event) {
                        		  $event.preventDefault();
                        		  $scope.registerFormActive = true;
                        		  utils.card_show_hide($login_card,undefined,register_form_show,undefined);
                        	  };

                        	  $scope.passwordReset = function($event) {
                        		  $event.preventDefault();
                        		  utils.card_show_hide($login_card,undefined,password_reset_show,undefined);
                        	  };
                        	  
                        	  $scope.credentials = {};
                        	  
                        	  var authenticate = function(credentials, callback) {

                        		  var headers = credentials ? {
                        			  authorization : "Basic "
                        				  + btoa(unescape(encodeURIComponent(credentials.username)) + ":"
                        						  + unescape(encodeURIComponent(credentials.password)))
                        		  } : {};

                        		  $http.get('/user', {
                        			  headers : headers
                        		  }).success(function(data) {
                        			  console.log(data);
                        			  if (data.name) {						
                        				  //UIkit.notify("<i class='uk-icon-check'></i> Login success...", {status:'success',pos:'bottom-right',timeout: 0})
                        				  $rootScope.user=data;
                        				  $rootScope.authenticated = true;
                        			  } else {
                        				  $rootScope.authenticated = false;
                        			  }
                        			  callback && callback($rootScope.authenticated);
                        		  }).error(function() {
                        			  $rootScope.authenticated = false;
                        			  callback && callback(false);
                        		  });

                        	  }

                        	  $scope.login = function() {
                        		  $rootScope.loggedout=false;
                        		  console.log($scope.credentials);
                        		  authenticate($scope.credentials, function(authenticated) {
                        			  
                        			  if (authenticated) {
                        				  console.log("Login succeeded")


                        				  var promise = $http.get("/izr/core/defaultSuccess").success(
                        						  function (data) {
                        							  var response = data;
                        							  console.log(response);
                        							  $state.go(response.url);
                        							  // $state.go(response);
                        						  })

                        						  //	$state.go('restricted.forms.file_upload');					
                        						  $scope.error = false;
                        				  localStorage.setItem("session", {});
                        				  $rootScope.authenticated = true;
                        			  } else {					

                        				  $scope.error = true;
                        				  console.log("Login failed")
                        				  self.error = true;
                        				  $rootScope.authenticated = false;
                        			  }
                        		  })
                        	  };

                          }
                          ]);