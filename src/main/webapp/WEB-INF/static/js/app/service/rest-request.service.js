restRequestService.$inject = ['restService', 'Upload', 'urls', 'utilService'];

function restRequestService(restService, Upload, urls, utilService) {
    var service = {};

    service.uploadFile = function (file) {
        Upload.rename(file, utilService.prepareFileNameForUpload(file.name));
        return Upload.upload({
            url: urls.BASE + '/file/upload',
            data: {file: file}
        });
    };

    service.getCurrentFileName = function () {
        return restService.post('/file/current-file-name');
    };

    service.readFile = function (fileName) {
        return restService.get('/file/read/', {parentName: fileName});
    };

    service.readChildFile = function (parentName, childName) {
        return restService.get('/file/read/', {parentName: parentName, childName: childName});
    };

    service.canonizeFile = function (fileName) {
        return restService.get('/file/canonize/' + fileName);
    };

    service.createDOM = function () {
        return restService.get('/file/create-dom/');
    };

    service.calculateQueries = function (fileName) {
        return restService.get('/file/calculate-queries/', {fileName: fileName});
    };

    service.searchQueries = function (fileName) {
        return restService.get('/file/searchQueries/', {fileName: fileName});
    };

    service.getExistingUsages = function (fileName) {
        return restService.get('/file/get-existing-queries/', {fileName: fileName});
    };

    return service;
}