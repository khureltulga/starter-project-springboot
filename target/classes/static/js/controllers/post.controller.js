app.controller('postController', function($scope,$http,$cookies,$rootScope,userService,postService,$sce) {
	$scope.headingTitle = "Post List";
	
	$scope.getPosts = function(){
		postService.getPosts();
	}
	
	$scope.loadPostEdit = function(p){
		$scope.single_post = p;
		$("#editPost").openModal();
	}
	
	$scope.loadNewPostDialog = function(){
		$scope.new_post = [];
		$("#newPost").openModal();
	}
	
	$scope.to_trusted = function(html_code) {
	    return $sce.trustAsHtml(html_code);
	}
	
	$scope.updatePost = function(single_post){
		$http.post("/api/update-post",single_post)
		.success(function (data) {
			$scope.single_post = data;
		})
		.error(function (data) {
			console.log(data);
		});
	}
	
	$scope.createPost = function(single_post){
		$http.post("/api/create-post",{title:single_post.title, content:single_post.content, user_id:$rootScope.current_user.id})
		.success(function (data) {
			$scope.post_list.push(data);
		})
		.error(function (data) {
			console.log(data);
		});
	}
	
	$scope.deletePost = function(id, index){
		if (confirm("Are you sure to delete this post?")){
			$http.post("/api/delete-post",{id:id})
			.success(function (data) {
				$scope.post_list.splice(index, 1);
			})
			.error(function (data) {
				console.log(data);
			});
		}
		
	}
});