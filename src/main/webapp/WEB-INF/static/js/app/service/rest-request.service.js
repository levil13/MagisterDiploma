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
        return restService.get('/file/read/' + fileName);
    };

    service.readChildFile = function (parentName, childName) {
        return restService.get('/file/read/' + parentName + '/' + childName);
    };

    service.canonizeFile = function (fileName) {
        return restService.get('/file/canonize/' + fileName);
    };

    service.createDOM = function (fileName) {
        return restService.get('/file/create-dom/' + fileName);
    };

    return service;
}