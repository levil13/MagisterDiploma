function uploadController($state, restRequestService) {
    var self = this;
    self.$onInit = function(){
        console.log("uploadControllerInit");
    };

    self.uploadFile = function(file) {
        if (file) {
            restRequestService.uploadFile(file)
                .then(function (response) {
                    var fileName = response && response.data && response.data.fileName;
                    if(fileName){
                        $state.go('process', {fileName: fileName});
                    }
                })
                .catch(function (reason) {
                    $state.go('error', {errorMsg: reason.data.errorMsg})
                });
        }
    }
}
