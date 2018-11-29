function processController($q, $rootScope, $stateParams, $state, restRequestService, utilService) {

    var self = this;

    self.$onInit = function () {
        self.utilService = utilService;
        self.fileName = $stateParams.fileName;
        self.forms = [];
        if (!self.fileName) {
            _updateCurrentFileName();
        }
        var readFilePromise = self.readFile(self.fileName);
        var createDOMPromise = self.createDOM(self.fileName);

        $q.all([readFilePromise, createDOMPromise])
            .then(function (responses) {
                self.text = responses[0] && responses[0].data && responses[0].data.fileText;
                self.sections = responses[1] && responses[1].data && responses[1].data.sections;
            })
            .catch(function (reason) {
                $state.go('error', {errorMsg: reason.data.errorMsg});
            })
            .finally(function () {
                utilService.toggleLoading(false);
            });
    };

    self.changeWeight = function (section) {
        utilService.toggleLoading(true);
        var form = self.forms['form' + section.sectionId];
        var input = form['input' + section.sectionId];
        if (section && section.sectionWeight) {
            if (section.sectionWeight > 5) {
                section.sectionWeight = 5;
            }

            if (section.sectionWeight < -5) {
                section.sectionWeight = -5;
            }
        }
        input.$viewValue = section.sectionWeight;
        var sectionName = section && section.sectionName;
        var sectionWeight = section && section.sectionWeight;
        restRequestService.updateSectionWeight(sectionName, sectionWeight)
            .then(function (response) {
                console.log(response);
            })
            .catch(function (reason) {
                console.log(reason);
            })
            .finally(function () {
                utilService.toggleLoading(false);
            })
    };

    self.toggleSubSections = function (index, state) {
        self.sections[index].showSubSections = !state;
    };

    self.readFile = function (fileName) {
        utilService.toggleLoading(true);
        var fileNameWithoutFormat = utilService.getFileNameWithoutFormat(fileName);
        return restRequestService.readFile(fileNameWithoutFormat);
    };

    self.readChildFile = function (childFileName) {
        utilService.toggleLoading(true);
        restRequestService.readChildFile(utilService.getFileNameWithoutFormat(self.fileName), childFileName)
            .then(function (response) {
                self.sectionText = response && response.data && response.data.sectionText;
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
        return restRequestService.createDOM(fileNameWithoutFormat);
    };

    self.findUsages = function (fileName) {
        utilService.toggleLoading(true);
        var fileNameWithoutFormat = utilService.getFileNameWithoutFormat(fileName);
        restRequestService.calculateQueries(fileNameWithoutFormat)
            .then(function (response) {
                self.queriesSize = response.data.queriesSize;
                return restRequestService.searchQueries(fileNameWithoutFormat);
            })
            .then(function (response) {
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
}