function errorController($stateParams, utilService) {
    var self = this;

    self.$onInit = function () {
        self.utilService = utilService;
        self.errorMsg = $stateParams.errorMsg;
    }
}