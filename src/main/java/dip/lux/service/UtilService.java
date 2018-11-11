package dip.lux.service;

public interface UtilService {
    boolean createDirectoryIfNotExists(String path);

    String getFileFormat(String name);

    String getNameWithoutFormat(String fileName);
}
