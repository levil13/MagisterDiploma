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
<script src="scripts/lib/ng-file-upload.js"></script>
<script src="scripts/lib/angular-ui-router.js"></script>
<script src="scripts/lib/ngStorage.js"></script>
<script src="scripts/app/constants/urls.const.js"></script>
<script src="scripts/app/constants/constants.module.js"></script>
<script src="scripts/app/service/rest.service.js"></script>
<script src="scripts/app/service/rest-request.service.js"></script>
<script src="scripts/app/service/util.service.js"></script>
<script src="scripts/app/service/transliterate.service.js"></script>
<script src="scripts/app/controller/upload.controller.js"></script>
<script src="scripts/app/controller/process.controller.js"></script>
<script src="scripts/app/controller/error.controller.js"></script>
<script src="scripts/app/app.js"></script>
</body>
</html>