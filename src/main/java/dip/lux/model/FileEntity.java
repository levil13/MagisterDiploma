package dip.lux.model;

import org.springframework.stereotype.Component;

@Component
public class FileEntity {
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
