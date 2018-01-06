(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('BlogEntryController', BlogEntryController);

    BlogEntryController.$inject = ['DataUtils', 'BlogEntry'];

    function BlogEntryController(DataUtils, BlogEntry) {

        var vm = this;

        vm.blogEntries = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            BlogEntry.query(function(result) {
                vm.blogEntries = result;
                vm.searchQuery = null;
            });
        }
    }
})();
