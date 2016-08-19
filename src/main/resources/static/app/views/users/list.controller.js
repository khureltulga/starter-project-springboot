angular
.module('altairApp')
.controller('userListController',
		function($compile, $scope, $timeout, $resource, DTOptionsBuilder, DTColumnBuilder) {
	var vm = this;
	vm.dtInstance = {};

	vm.dtOptions = DTOptionsBuilder
	.fromSource('/data/users')
	.withOption('processing', true)
	.withOption('serverSide', true)
	.withOption('order', [0, 'asc'])
	.withDataProp('data')
	.withDOM("<'dt-uikit-header'<'uk-grid'<'uk-width-medium-2-3'l><'uk-width-medium-1-3'f>>>" +
			"<'uk-overflow-container'tr>" +
	"<'dt-uikit-footer'<'uk-grid'<'uk-width-medium-3-10'i><'uk-width-medium-7-10'p>>>")
	.withPaginationType('full_numbers')
	// Active Buttons extension
	.withButtons([
	              {
	            	  extend:    'copyHtml5',
	            	  text:      '<i class="uk-icon-files-o"></i> Copy',
	            	  titleAttr: 'Copy'
	              },
	              {
	            	  extend:    'print',
	            	  text:      '<i class="uk-icon-print"></i> Print',
	            	  titleAttr: 'Print'
	              },
	              {
	            	  extend:    'excelHtml5',
	            	  text:      '<i class="uk-icon-file-excel-o"></i> XLSX',
	            	  titleAttr: ''
	              },
	              {
	            	  extend:    'csvHtml5',
	            	  text:      '<i class="uk-icon-file-text-o"></i> CSV',
	            	  titleAttr: 'CSV'
	              },
	              {
	            	  extend:    'pdfHtml5',
	            	  text:      '<i class="uk-icon-file-pdf-o"></i> PDF',
	            	  titleAttr: 'PDF'
	              }
	              ]);
	vm.dtColumn = [
	               DTColumnBuilder.newColumn("id").withOption('data', 'id').withTitle('ID'),
	               DTColumnBuilder.newColumn("name").withOption('data', 'name').withTitle('Name'),
	               DTColumnBuilder.newColumn("email").withOption('data', 'email').withTitle('Email'),
	               DTColumnBuilder.newColumn(null).withTitle('Actions').notSortable()
	               .renderWith(function(data, type, full, meta) {
	            	   return '<center><a class="md-btn md-btn-primary md-btn-mini md-btn-wave-light md-btn-icon waves-effect waves-button waves-light" ng-click="edit(' + data.id + ')">' +
	            	   '   <i class="material-icons">edit</i>' +
	            	   '</a>&nbsp;' +
	            	   '<a class="md-btn md-btn-danger md-btn-mini md-btn-wave-light md-btn-icon waves-effect waves-button waves-light" ng-click="delete(' + data.id + ')">' +
	            	   '   <i class="material-icons">delete</i>' +
	            	   '</a></center>';
	               })
	               ];
}
).controller('uiGridController',
		function($scope, $http, $q) {
	$scope.gridOptions = {
			enableFiltering: true,
		    flatEntityAccess: true,
		    showGridFooter: true,
		    fastWatch: true
		    };

	$scope.gridOptions.columnDefs = [
	                                 {name:'id'},
	                                 {name:'name'},
	                                 {field:'age'}, // showing backwards compatibility with 2.x.  you can use field in place of name
	                                 {name: 'address.city'},
	                                 {name: 'age again', field:'age'}
	                                 ];

	var canceler = $q.defer();

	$http.get('http://ui-grid.info/data/10000_complex.json', {timeout: canceler.promise})
	.success(function(data) {
		$scope.gridOptions.data = data;	
	});

	$scope.$on('$destroy', function(){
		canceler.resolve();  // Aborts the $http request if it isn't finished.
	});

}
);