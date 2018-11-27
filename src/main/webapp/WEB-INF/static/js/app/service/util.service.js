function utilService(transliterateService) {
    var service = {};

    service.isLoading = false;

    service.toggleLoading = function (state) {
        service.isLoading = state;
    };

    service.getFileNameWithoutFormat = function (fileName) {
        return fileName.split('.')[0];
    };

    service.getFileFormat = function (fileName) {
        return fileName.split('.')[1];
    };

    service.prepareFileNameForUpload = function (fileName) {
        var preparedFileName = service.getFileNameWithoutFormat(fileName);
        preparedFileName = transliterateService.transliterate(preparedFileName);
        preparedFileName = preparedFileName
            .trim()
            .replace(/[^0-9a-zA-Z.]/g, '');
        return preparedFileName.concat('.', service.getFileFormat(fileName));
    };

    service.calculateSearchTime = function (queriesSize) {
        var totalSeconds = queriesSize / 3;
        var hours = Math.floor(totalSeconds / 3600);
        var minutes = Math.floor((totalSeconds - (hours * 3600)) / 60);
        var seconds = totalSeconds - (hours * 3600) - (minutes * 60);

        // round seconds
        seconds = Math.round(seconds * 100) / 100;

        var result = (hours < 10 ? "0" + hours : hours);
        result += " hours " + (minutes < 10 ? "0" + minutes : minutes);
        result += " minutes " + (seconds < 10 ? "0" + seconds : seconds);
        result += " seconds";
        return result;
    };

    return service;
}