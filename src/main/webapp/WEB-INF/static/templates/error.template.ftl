<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <link href="styles/bootstrap.css" rel="stylesheet"/>
    <link href="styles/app.css" rel="stylesheet"/>
</head>
<body>
<div ng-hide="errorCtrl.utilService.isLoading">
    <h1 ng-bind="errorCtrl.errorMsg"></h1>
</div>
<div ng-show="errorCtrl.utilService.isLoading">
    Loading...
</div>
</body>
</html>