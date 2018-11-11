function processController($stateParams, $state, restRequestService, utilService) {

    var self = this;

    self.$onInit = function () {
        self.utilService = utilService;
        self.fileName = $stateParams.fileName;
        if (!self.fileName) {
            _updateCurrentFileName();
        }
    };

    self.readFile = function (fileName) {
        utilService.toggleLoading(true);
        var fileNameWithoutFormat = utilService.getFileNameWithoutFormat(fileName);
        restRequestService.readFile(fileNameWithoutFormat)
            .then(function (response) {
                self.text = response && response.data && response.data.fileText;
            })
            .catch(function (reason) {
                $state.go('error', {errorMsg: reason.data.errorMsg});
            })
            .finally(function () {
                utilService.toggleLoading(false);
            });
    };

    self.canonizeFile = function (fileName) {
        utilService.toggleLoading(true);
        var fileNameWithoutFormat = utilService.getFileNameWithoutFormat(fileName);
        restRequestService.canonizeFile(fileNameWithoutFormat)
            .then(function (response) {
                self.canonizedText = response && response.data && response.data.canonizedText;
            })
            .catch(function (reason) {
                $state.go('error', {errorMsg: reason.data.errorMsg});
            })
            .finally(function () {
                utilService.toggleLoading(false);
            });
    };

    self.createDOM = function (fileName) {
        utilService.toggleLoading(true);
        var fileNameWithoutFormat = utilService.getFileNameWithoutFormat(fileName);
        restRequestService.createDOM(fileNameWithoutFormat)
            .then(function (response) {
                self.canonizedFileContent = response && response.data && response.data.canonizedFile;
            })
            .catch(function (reason) {
                $state.go('error', {errorMsg: reason.data.errorMsg});
            })
            .finally(function () {
                utilService.toggleLoading(false);
            });
    };

    function _updateCurrentFileName() {
        utilService.toggleLoading(true);
        restRequestService.getCurrentFileName()
            .then(function (response) {
                self.fileName = response && response.data && response.data.fileName;
            })
            .catch(function (reason) {
                $state.go('error', {errorMsg: reason.data.errorMsg});
            })
            .finally(function () {
                utilService.toggleLoading(false);
            });
    }
}