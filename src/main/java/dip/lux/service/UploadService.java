package dip.lux.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String upload(MultipartFile file);
}
