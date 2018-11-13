package dip.lux.service.model;

public enum StatusType {
    OK("OK"),
    ERROR("ERROR");

    private final String status;

    StatusType(String status) {
        this.status = status;
    }

    public boolean equals(String status){
        return this.status.toUpperCase().equals(status.toUpperCase());
    }

    public String getType() {
        return status;
    }
}
