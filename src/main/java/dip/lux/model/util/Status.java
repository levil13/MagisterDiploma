package dip.lux.model.util;

import dip.lux.service.model.StatusType;

public class Status {
    private StatusType status;
    private String errorMsg;

    public StatusType getStatusType() {
        return status;
    }

    public void setStatusType(StatusType statusType) {
        this.status = statusType;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
