package dip.lux.model;

import dip.lux.model.util.Query;
import dip.lux.model.util.Status;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileEntity {
    private String fileName;
    private String fileText;
    private List<FileEntity> childFiles;
    private Status status;
    private List<Query> queries;

    public List<Query> getQueries() {
        return queries;
    }

    public void setQueries(List<Query> queries) {
        this.queries = queries;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<FileEntity> getChildFiles() {
        return childFiles;
    }

    public void setChildFiles(List<FileEntity> childFiles) {
        this.childFiles = childFiles;
    }

    public String getFileText() {
        return fileText;
    }

    public void setFileText(String fileText) {
        this.fileText = fileText;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
