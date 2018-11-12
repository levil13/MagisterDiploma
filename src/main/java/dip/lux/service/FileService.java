package dip.lux.service;

import java.util.ArrayList;
import java.util.Map;

public interface FileService {
    Map<String, String> readFile(String fileName);

    Map<String, String> readChildFile(String parentFile, String childFile);

    ArrayList<String> createDOM(String fileContent);
}
