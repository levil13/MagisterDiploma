function utilService(transliterateService) {
    var service = {};

    service.isLoading = false;

    service.toggleLoading = function (state) {
        service.isLoading = state;
    };

    service.getFileNameWithoutFormat = function (fileName) {
        return fileName.split('.')[0];
    };

    service.getFileFormat = function(fileName){
        return fileName.split('.')[1];
    };

    service.prepareFileNameForUpload = function (fileName) {
        var preparedFileName = service.getFileNameWithoutFormat(fileName);
        preparedFileName = transliterateService.transliterate(preparedFileName);
        preparedFileName = preparedFileName
            .replace(/\s\s+/g, ' ')
            .replace(' ', '_');
        return preparedFileName.concat('.', service.getFileFormat(fileName));
    };

    return service;
}