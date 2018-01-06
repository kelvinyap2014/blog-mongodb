(function() {
    'use strict';
    angular
        .module('blogApp')
        .factory('BlogEntry', BlogEntry);

    BlogEntry.$inject = ['$resource', 'DateUtils'];

    function BlogEntry ($resource, DateUtils) {
        var resourceUrl =  'api/blog-entries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
