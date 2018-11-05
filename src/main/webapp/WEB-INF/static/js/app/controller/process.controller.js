function processController($stateParams, $state, restRequestService) {

    var self = this;

    self.$onInit = function () {
        self.fileName = $stateParams.fileName;
        if(!self.fileName){
            restRequestService.getCurrentFileName()
                .then(function (response) {
                    self.fileName = response && response.data && response.data.fileName;
                })
                .catch(function (reason) {
                    $state.go('error', {errorMsg: reason.data.errorMsg});
                });
        }
    }
}