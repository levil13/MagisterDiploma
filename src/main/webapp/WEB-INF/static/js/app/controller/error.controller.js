function errorController($stateParams) {
    var self = this;

    self.$onInit = function () {
        self.errorMsg = $stateParams.errorMsg;
    }
}