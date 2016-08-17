var app = angular.module('app', ['ngRoute','ngResource','ngCookies','ui.tinymce','datatables']);

app.config(function($routeProvider){
	$routeProvider
	.when('/',{
		templateUrl: '/user/list.html',
		controller: 'usersController',
		requireLogin: false
	})
	.when('/register',{
		templateUrl: '/user/register.html',
		controller: 'usersController',
		requireLogin: false
	})
	.when('/list',{
		templateUrl: '/user/list.html',
		controller: 'usersController',
		requireLogin: false
	})
	.when('/login',{
		templateUrl: '/user/login.html',
		controller: 'usersController',
		requireLogin: false
	})
	.when('/posts',{
		templateUrl: '/post/list.html',
		controller: 'postController',
		requireLogin: true
	})
	.otherwise(
			{ redirectTo: '/'}
	);
});

app.run(function(globalService,$rootScope) {
	globalService.init();
	$rootScope.$on("$routeChangeStart", function(event, next, current) {
		
	    if(next.requireLogin && !$rootScope.is_Authenticated) {
	    	Materialize.toast("You don't have permission to access this page!", 3000, "red");
	        event.preventDefault();
	    }
	});
});

app.service('globalService', function ($rootScope,$cookies,$http) {
	$rootScope.routeTask = function(){
		$rootScope.$on("$routeChangeStart", function(event, next, current) {
		    if(next.requireLogin && !$rootScope.is_Authenticated) {
		    	Materialize.toast("You don't have permission to access this page!", 3000, "red");
		        event.preventDefault();
		    }
		});
	}
	
	$rootScope.logOutUser = function(){
		if (confirm("Are you sure to log out?")){
			$cookies.remove("user_id");
			$rootScope.current_user = null;
			$rootScope.is_Authenticated = false;
			Materialize.toast("Logged out!", 3000, "red");
		}
		
	}
	
	this.init = function () {
		$rootScope.is_Authenticated = false;
		if ($cookies.get("user_id") != null){
			//this.getUserDetail($cookies.get("user_id"));
			
			$http.post("/api/get-user-detail",{id:$cookies.get("user_id")})
			.success(function (data) {
				$rootScope.current_user = data;
				if ($rootScope.current_user != null && $rootScope.current_user != undefined){
					$rootScope.is_Authenticated = true;
				}
				else{
					$rootScope.is_Authenticated = false;
				}
				//$rootScope.routeTask();
			})
			.error(function (data) {
				console.log(data);
				//$rootScope.routeTask();
			});
		}
		else{
			$rootScope.is_Authenticated = false;
		}
	};
});