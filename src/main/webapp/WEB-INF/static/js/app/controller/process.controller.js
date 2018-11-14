function processController($rootScope, $stateParams, $state, restRequestService, utilService) {

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

    self.readChildFile = function (childFileName) {
        utilService.toggleLoading(true);
        restRequestService.readChildFile(utilService.getFileNameWithoutFormat(self.fileName), childFileName)
            .then(function (response) {
                self.childFileText = response && response.data && response.data.childFileText;
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
                self.childFiles = response && response.data && response.data.childFiles;
            })
            .catch(function (reason) {
                $state.go('error', {errorMsg: reason.data.errorMsg});
            })
            .finally(function () {
                utilService.toggleLoading(false);
            });
    };

    self.findUsages = function (fileName) {
        utilService.toggleLoading(true);
        var fileNameWithoutFormat = utilService.getFileNameWithoutFormat(fileName);
        restRequestService.calculateQueries(fileNameWithoutFormat)
            .then(function (response) {
                self.queriesSize = response.data.queriesSize;
                _startTimer();
                return restRequestService.searchQueries(fileNameWithoutFormat);
            })
            .then(function (response) {
                _stopTimer();
                $state.go('result', {
                    fileName: fileNameWithoutFormat,
                    queries: response.data && response.data.queriesWithResponses
                });
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

    function _startTimer() {
        $rootScope.$broadcast('timer-start');
    }

    function _stopTimer() {
        $rootScope.$broadcast('timer-stop');
    }
}