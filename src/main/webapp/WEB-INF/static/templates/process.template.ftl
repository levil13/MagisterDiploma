<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <link href="styles/bootstrap.css" rel="stylesheet"/>
    <link href="styles/app.css" rel="stylesheet"/>
</head>
<body>
<div ng-hide="processCtrl.utilService.isLoading">
    <h1 ng-bind="processCtrl.fileName"></h1>
    <button ng-click="processCtrl.readFile(processCtrl.fileName)">
        Show file content
    </button>

    <button ng-click="processCtrl.canonizeFile(processCtrl.fileName)">
        Canonize file
    </button>

    <button ng-click="processCtrl.createDOM(processCtrl.fileName)">
        Create DOM structure
    </button>
</div>
<div ng-hide="processCtrl.utilService.isLoading">
    <div ng-hide="!processCtrl.childFiles">
        <h2>Child Files:</h2>
        <div ng-repeat="childFile in processCtrl.childFiles">
            <button ng-click="processCtrl.childFileText = childFile.fileText">{{childFile.fileName}}</button>
        </div>
    </div>
    <div ng-hide="!processCtrl.childFileText">
        <h2>Child file text:</h2>
        <div ng-bind="processCtrl.childFileText"></div>
    </div>
</div>

<div ng-hide="processCtrl.utilService.isLoading">
    <div ng-hide="!processCtrl.text">
        <h2>File Text:</h2>
        <div ng-bind="processCtrl.text">

        </div>
    </div>

    <div ng-hide="!processCtrl.canonizedText">
        <h2>Canonized Text:</h2>
        <div ng-bind="processCtrl.canonizedText">

        </div>
    </div>
</div>

<div ng-show="processCtrl.utilService.isLoading">
    Loading syka!
</div>
</body>
</html>