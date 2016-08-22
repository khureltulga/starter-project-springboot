app.service('userService', function ($rootScope,$cookies) {

	this.getUserDetail = function(id){
		$http.post("/api/get-user-detail",{id:id})
		.success(function (data) {
			return data;
		})
		.error(function (data) {
			console.log(data);
		});
	}
});