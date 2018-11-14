function resultController($window, $stateParams, $localStorage, utilService, restRequestService) {

    var self = this;

    self.$onInit = function () {
        self.fileName = $stateParams.fileName;
        self.queries = $stateParams.queries;

        if (self.fileName) {
            $localStorage.fileName = self.fileName;
        } else {
            self.fileName = $localStorage.fileName;
        }

        if (!self.queries.size) {
            self.getExistingUsages(self.fileName);
        }
    };

    self.getExistingUsages = function (fileName) {
        utilService.toggleLoading(true);
        var fileNameWithoutFormat = utilService.getFileNameWithoutFormat(fileName);
        restRequestService.getExistingUsages(fileNameWithoutFormat)
            .then(function (response) {
                self.queries = response.data && response.data.queriesWithResponses
            })
            .catch(function (reason) {
                $state.go('error', {errorMsg: reason.data.errorMsg});
            })
            .finally(function () {
                utilService.toggleLoading(false);
            });
    };

    self.goToUrl = function (url) {

    };

}