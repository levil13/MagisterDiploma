var app = angular.module('app', ['ui.router', 'ngStorage', 'app.constants']);

app
    .config(['$stateProvider', '$urlRouterProvider',
    function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise('/');
        $stateProvider
            .state('home', {
                url: '/',
                templateUrl: 'templates/home.ftl',
                controller: UserController
            });
    }]);

app
    .factory('restService', restService);