package dip.lux.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    boolean upload(MultipartFile file);
}
