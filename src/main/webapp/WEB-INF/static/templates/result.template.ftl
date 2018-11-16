<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <link href="styles/bootstrap.css" rel="stylesheet"/>
    <link href="styles/app.css" rel="stylesheet"/>
</head>
<body>

<div>
    <h2>Found usages in file: {{resultCtrl.fileName}}</h2>
</div>

<div ng-repeat="query in resultCtrl.queries">
    From word # {{query.firstWordIndex}}
    To word # {{query.lastWordIndex}}
    <div ng-repeat="queryResponseItem in query.queryResponse">
        <a href="" ng-click="resultCtrl.goToUrl(queryResponseItem)">{{queryResponseItem}}</a>
    </div>
</div>

<div ng-show="resultCtrl.utilService.isLoading">
    Loading...
</div>
</body>
</html>