restRequestService.$inject = ['restService', 'Upload', 'urls'];

function restRequestService(restService, Upload, urls) {
    var service = {};

    service.uploadFile = function (file) {
        return Upload.upload({
            url: urls.BASE + '/file/upload',
            data: {file: file}
        });
    };

    service.getCurrentFileName = function () {
        return restService.post('/file/current-file-name');
    };

    return service;
}