restService.$inject = ['$q', '$http', '$log', 'urls'];

function restService($q, $http, $log, urls) {
    var service = {};

    service.restUrl = urls.BASE;

    service.http = function (method, url, data, params, config) {
        return $http({
            method: method,
            url: url,
            data: data,
            params: params,
            config: config
        });
    };

    service.get = function (url, params, config) {
        return service.commonCall("GET", service.restUrl + url, null, params, config);
    };

    service.post = function (url, data, params, config) {
        return service.commonCall("POST", service.restUrl + url, data, params, config);
    };

    service.put = function (url, data, params, config) {
        return service.commonCall("PUT", service.restUrl + url, data, params, config);
    };

    service.delete = function (url, data, params, config) {
        return service.commonCall("DELETE", service.restUrl + url, data, params, config);
    };

    service.commonCall = function (method, url, data, params, config) {
        var startTime = new Date().getTime();
        var request = service.http(method, url, data, params, config);
        request
            .then(function (response) {
                service.logResponse(method, url, data, params, response, startTime);
                return response;
            });
        return request;
    };

    service.logResponse = function (method, url, data, params, response, startTime, isCancelled) {
        try {
            var format = "%1$s:[%2$s], ";
            if (data) {
                format += "data:%3$o, ";
            }
            if (params) {
                format += "params:%4$o, ";
            }
            format += "response:%5$o, time: %6$dms, cancelled: %7$s";
            $log.log(format, method, url, data, params, response.data, (new Date().getTime() - startTime), isCancelled);
        } catch (ignore) {
        }
    };

    return service;
}