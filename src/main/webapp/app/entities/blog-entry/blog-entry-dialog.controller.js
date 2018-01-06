(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('BlogEntryDialogController', BlogEntryDialogController);

    BlogEntryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'BlogEntry'];

    function BlogEntryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, BlogEntry) {
        var vm = this;

        vm.blogEntry = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.blogEntry.id !== null) {
                BlogEntry.update(vm.blogEntry, onSaveSuccess, onSaveError);
            } else {
                BlogEntry.save(vm.blogEntry, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('blogApp:blogEntryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
