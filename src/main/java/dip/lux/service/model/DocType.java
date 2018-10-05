package dip.lux.service.model;

public enum DocType {
    DOC("DOC"),
    DOCX("DOCX"),
    ODT("ODT");

    private final String type;

    DocType(String type) {
        this.type = type;
    }

    public boolean equals(String type){
        return this.type.toUpperCase().equals(type.toUpperCase());
    }

    public String getType() {
        return type;
    }
}
