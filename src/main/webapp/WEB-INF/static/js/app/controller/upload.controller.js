function uploadController($scope, $state, restRequestService, utilService, transliterateService) {
    var self = this;
    self.$onInit = function () {
        self.utilService = utilService;
    };

    self.uploadFile = function (file) {
        if (file) {
            utilService.toggleLoading(true);
            restRequestService.uploadFile(file)
                .then(function (response) {
                    var fileName = response && response.data && response.data.fileName;
                    if (fileName) {
                        $state.go('process', {fileName: fileName});
                    }
                })
                .catch(function (reason) {
                    $state.go('error', {errorMsg: reason.data.errorMsg})
                })
                .finally(function () {
                    utilService.toggleLoading(false);
                });
        }
    }
}
