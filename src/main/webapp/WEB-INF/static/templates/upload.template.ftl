<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <link href="styles/bootstrap.css" rel="stylesheet"/>
    <link href="styles/app.css" rel="stylesheet"/>
</head>
<body>
<div ng-hide="uploadCtrl.utilService.isLoading">
    <h1>Upload your file!</h1>
    <button type="file"
            ngf-select="uploadCtrl.uploadFile($file, $invalidFiles)"
            ngf-max-size="50MB">
        Select File
    </button>
</div>
<div ng-show="uploadCtrl.utilService.isLoading">
    Loading syka!
</div>
</body>
</html>