app.controller('usersController', function($scope,$http,$cookies,$rootScope,userService,postService,globalService) {
	$scope.getUsers = function(){
		$http.post("/api/get-users")
		.success(function (data) {
			$scope.user_list = data;
			//$http.defaults.headers.common['Authorization'] = 'Basic ' + base64.encode($scope.newUser.username + ':' + $scope.newUser.password);
			//$location.url('/user/home');
		})
		.error(function (data) {
			console.log(data);
		});
	}


	$scope.RegisterNewUser = function(user){
		$http.post("/api/save",user)
		.success(function (data) {
			$scope.user_list.push(data);
			//$http.defaults.headers.common['Authorization'] = 'Basic ' + base64.encode($scope.newUser.username + ':' + $scope.newUser.password);
			//$location.url('/user/home');
		})
		.error(function (data) {
			console.log(data);
		});
	}
	
	$scope.LoginUser = function(user){
		$http.post("/api/login",user)
		.success(function (data) {
			$rootScope.current_user = data;
			$rootScope.is_Authenticated = true;
		})
		.error(function (data) {
			console.log(data);
		});
	}
	
});