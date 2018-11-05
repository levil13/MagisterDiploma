<!DOCTYPE html>

<html lang="en" ng-app="app">
<head>
    <title>${title}</title>
    <link href="styles/bootstrap.css" rel="stylesheet"/>
    <link href="styles/app.css" rel="stylesheet"/>
</head>
<body>

<div ui-view></div>
<script src="scripts/lib/angular.js"></script>
<script src="scripts/lib/angular-ui-router.js"></script>
<script src="scripts/lib/ngStorage.js"></script>
<script src="scripts/app/constants/urls.const.js"></script>
<script src="scripts/app/constants/constants.module.js"></script>
<script src="scripts/app/controller/UserController.js"></script>
<script src="scripts/app/service/rest.service.js"></script>
<script src="scripts/app/app.js"></script>
</body>
</html>