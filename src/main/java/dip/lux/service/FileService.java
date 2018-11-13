package dip.lux.service;

import dip.lux.model.FileEntity;

import java.util.List;
import java.util.Map;

public interface FileService {
    Map<String, Object> readFile(String fileName);

    List<FileEntity> createDOM(String parentText, String parentName);
}
