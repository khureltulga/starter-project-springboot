app.service('postService', function ($rootScope,$cookies,$http) {
	this.getPosts = function(){
		$http.get("/api/get-all-post")
		.success(function (data) {
			$rootScope.post_list = data;
		})
		.error(function (data) {
			console.log(data);
		});
	}
});