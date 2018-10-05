package dip.lux.service;

import java.io.File;

public interface UtilService {
    boolean createDirectoryIfNotExists(String path);

    String getFileFormat(File doc);

    String getNameWithoutFormat(File doc);
}
