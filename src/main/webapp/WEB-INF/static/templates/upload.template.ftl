<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <link href="styles/bootstrap.css" rel="stylesheet"/>
    <link href="styles/app.css" rel="stylesheet"/>
</head>
<body>
<div>
    <h1>Upload your file!</h1>
    <button type="file"
            ngf-select="uploadCtrl.uploadFile($file, $invalidFiles)"
            ngf-max-size="50MB">
        Select File
    </button>
</div>
</body>
</html>