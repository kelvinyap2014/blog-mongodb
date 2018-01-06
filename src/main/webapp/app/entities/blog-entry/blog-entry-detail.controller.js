(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('BlogEntryDetailController', BlogEntryDetailController);

    BlogEntryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'BlogEntry'];

    function BlogEntryDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, BlogEntry) {
        var vm = this;

        vm.blogEntry = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('blogApp:blogEntryUpdate', function(event, result) {
            vm.blogEntry = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
