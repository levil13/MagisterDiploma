package dip.lux.service;

public interface ValidationService {
    boolean isValidFileType(String fileName);

    boolean isDOCX(String format);

    boolean isDOC(String format);

    boolean isODT(String format);
}
