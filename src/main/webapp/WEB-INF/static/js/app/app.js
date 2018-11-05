var app = angular.module('app', ['ui.router', 'ngStorage', 'app.constants', 'ngFileUpload']);

app
    .config(['$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise('/');
            $stateProvider
                .state('home', {
                    url: '/',
                    templateUrl: 'templates/upload.template.ftl',
                    controller: uploadController,
                    controllerAs: 'uploadCtrl'
                })
                .state('process', {
                    url: '/process/:fileName',
                    templateUrl: 'templates/process.template.ftl',
                    controller: processController,
                    controllerAs: 'processCtrl',
                    params: {
                        fileName: null
                    }
                })
                .state('error', {
                    url: '/error',
                    templateUrl: 'templates/error.template.ftl',
                    controller: errorController,
                    controllerAs: 'errorCtrl',
                    params: {
                        errorMsg: null
                    }
                });
        }]);

app
    .factory('restService', restService)
    .factory('restRequestService', restRequestService);