package dip.lux.service.impl;

import com.itextpdf.text.DocumentException;
import dip.lux.service.UploadService;
import dip.lux.service.ValidationService;
import dip.lux.service.model.StatusType;
import dip.lux.service.util.DocsConverter.DocsConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UploadServiceImpl implements UploadService {
    private static Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);
    private static final String RAW_FOLDER = "C:\\Temp\\raw";
    private static final String CONVERTED_FOLDER = "C:\\Temp\\converted";
    private Integer counter = 0;

    @Autowired
    private DocsConverter docsConverter;

    @Autowired
    private ValidationService validationService;

    @Override
    public String upload(MultipartFile file) {
        boolean isDirectoryExists = UtilService.createDirectoryIfNotExists(RAW_FOLDER);
        if (isDirectoryExists) {
            byte[] fileBytes = getFileBytes(file);
            String fileName = file.getOriginalFilename();
            if (isFileUploaded(fileName)) {
                fileName = generateOriginalFileName(fileName);
            }
            if (validationService.isPDF(UtilService.getFileFormat(fileName))) {
                String fileNameWithoutFormat = UtilService.getNameWithoutFormat(fileName);
                String folderPath = UtilService.generateConvertedFolderPath(fileNameWithoutFormat);
                UtilService.createDirectoryIfNotExists(folderPath);
                writeFile(folderPath, fileBytes, fileName);
                return fileName;
            } else {
                writeFile(RAW_FOLDER, fileBytes, fileName);
                try {
                    formatFile(fileName);
                } catch (IOException | DocumentException e) {
                    logger.error("Can't format the file: " + e);
                    return StatusType.ERROR.getType();
                }
                return fileName;
            }
        }
        return StatusType.ERROR.getType();
    }

    private void formatFile(String fileName) throws IOException, DocumentException {
        File newDoc = new File(RAW_FOLDER + File.separator + fileName);
        String name = newDoc.getName();
        String format = UtilService.getFileFormat(name);
        docsConverter.convertByFormat(format, newDoc);
    }

    private void writeFile(String path, byte[] fileBytes, String fileName) {
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(
                new File(path + File.separator + fileName)))) {
            stream.write(fileBytes);
        } catch (IOException e) {
            logger.error("Couldn't write file");
        }
    }

    private byte[] getFileBytes(MultipartFile file) {
        byte[] bytes = new byte[0];
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            logger.error("Couldn't parse file to bytes");
        }
        return bytes;
    }

    private String generateOriginalFileName(String fileName) {
        counter++;
        String generatedName = createOriginalFileName(fileName, counter);
        if (isFileUploaded(generatedName)) {
            generatedName = generateOriginalFileName(generatedName);
        }
        return generatedName;
    }

    private boolean isFileUploaded(String fileName) {
        String fileFormat = UtilService.getFileFormat(fileName);
        List<String> uploadedFiles = getFilesInFolder(new File(RAW_FOLDER));
        if(validationService.isPDF(fileFormat)){
            return new File(CONVERTED_FOLDER + File.separator + UtilService.getNameWithoutFormat(fileName)).exists();
        }

        if (CollectionUtils.isEmpty(uploadedFiles)) {
            return false;
        }

        for (String uploadedFile : uploadedFiles) {
            if (uploadedFile.equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    private String createOriginalFileName(String fileName, Integer counter) {
        String[] fileNameParts = fileName.split("\\.");
        return fileNameParts[0] + "_" + counter + "." + fileNameParts[1];
    }

    private List<String> getFilesInFolder(File folder) {
        List<String> filesNames = new ArrayList<>();
        for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                getFilesInFolder(fileEntry);
            } else {
                filesNames.add(fileEntry.getName());
            }
        }
        return filesNames;
    }
}
