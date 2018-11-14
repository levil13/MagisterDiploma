package dip.lux.service;

import dip.lux.model.FileEntity;
import dip.lux.model.util.Query;

import java.util.List;
import java.util.Map;

public interface FileService {
    Map<String, Object> readFile(String fileName);

    List<FileEntity> createDOM(String parentText, String parentName);

    List<Query> getQueries(String text, String fileName);

    Map<String, Object> search(List<Query> queries);
}
