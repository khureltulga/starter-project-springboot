angular
    .module('altairApp')
    .controller('userListController',
        function($compile, $scope, $timeout, $resource, DTOptionsBuilder, DTColumnDefBuilder) {
            var vm = this;
            vm.dtOptions = DTOptionsBuilder
                .fromSource('/data/users')
                .withOption('serverSide', true)
                .withOption('processing', true)
                .withOption('order', [0, 'asc'])
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
            vm.dtColumnDefs = [
                DTColumnDefBuilder.newColumnDef(0).withOption('data', 'id').withTitle('ID'),
                DTColumnDefBuilder.newColumnDef(1).withOption('data', 'name').withTitle('Name'),
                DTColumnDefBuilder.newColumnDef(2).withOption('data', 'email').withTitle('Email')
            ];
        }
    );