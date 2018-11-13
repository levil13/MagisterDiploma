package dip.lux.controller;

import dip.lux.model.FileEntity;
import dip.lux.model.util.Status;
import dip.lux.service.FileService;
import dip.lux.service.ShingleService;
import dip.lux.service.UploadService;
import dip.lux.service.ValidationService;
import dip.lux.service.impl.UtilService;
import dip.lux.service.model.StatusType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/file")
public class FileController {
    @Autowired
    private UploadService uploadService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private FileEntity fileEntity;

    @Autowired
    private ShingleService shingleService;

    @Autowired
    private FileService fileService;

    @PostMapping(value = "/upload")
    public ResponseEntity singleFileUpload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("errorMsg", "File is empty");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!validationService.isValidFileType(file.getOriginalFilename())) {
            response.put("errorMsg", "Illegal file format");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String uploadedFileName = uploadService.upload(file);
        if (uploadedFileName.equals(StatusType.ERROR.getType())) {
            response.put("errorMsg", "Error in upload");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        fileEntity.setFileName(UtilService.getNameWithoutFormat(uploadedFileName));
        response.put("fileName", uploadedFileName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/read")
    @ResponseBody
    public ResponseEntity readFile(@RequestParam(value = "parentName") String parentName,
                                   @RequestParam(value = "childName", required = false) String childName) {
        Map<String, Object> response = new HashMap<>();
        if (!isSessionFile(parentName)) {
            updateSessionFileContent(parentName);
        }
        FileEntity fileToRead = fileEntity;
        if (StringUtils.isNotEmpty(childName)) {
            fileToRead = findSessionChildFile(childName);
        }
        if (fileToRead.getStatus().getStatusType().equals(StatusType.ERROR)) {
            response.put("errorMsg", fileToRead.getStatus().getErrorMsg());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("fileText", fileToRead.getFileText());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/canonize/{fileName}")
    @ResponseBody
    public ResponseEntity canonizeFile(@PathVariable String fileName) {
        Map<String, Object> response = new HashMap<>();
        FileEntity fileToCanonize = new FileEntity();
        if (isSessionFile(fileName)) {
            fileToCanonize = fileEntity;
        }
        if (isSessionChildFile(fileName)) {
            fileToCanonize = findSessionChildFile(fileName);
        }
        if (fileToCanonize.getStatus().getStatusType().equals(StatusType.ERROR)) {
            response.put("errorMsg", fileToCanonize.getStatus().getErrorMsg());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String canonizedFile = shingleService.canonize(fileToCanonize.getFileText());
        response.put("canonizedText", canonizedFile);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/create-dom")
    @ResponseBody
    public ResponseEntity createDOM() {
        Map<String, Object> response = new HashMap<>();
        if (StringUtils.isEmpty(fileEntity.getFileText())) {
            updateSessionFileContent(fileEntity.getFileName());
        }
        List<FileEntity> childFiles = fileService.createDOM(fileEntity.getFileText(), fileEntity.getFileName());
        for (FileEntity childFile : childFiles) {
            Status childStatus = childFile.getStatus();
            if (childStatus.getStatusType().equals(StatusType.ERROR)) {
                response.put("errorMsg", childStatus.getErrorMsg() + " in file " + childFile.getFileName());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        fileEntity.setChildFiles(childFiles);
        response.put("childFiles", childFiles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/current-file-name")
    @ResponseBody
    public ResponseEntity getCurrentFileName() {
        Map<String, Object> response = new HashMap<>();
        String fileName = fileEntity.getFileName();
        if (StringUtils.isEmpty(fileName)) {
            response.put("errorMsg", "No current file name");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("fileName", fileName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void updateSessionFileContent(String fileName) {
        Map<String, Object> fileContent = fileService.readFile(fileName);
        fileEntity.setStatus((Status) fileContent.get("status"));
        fileEntity.setFileText((String) fileContent.get("text"));
    }

    private boolean isSessionFile(String fileName) {
        return (StringUtils.equals(fileName, fileEntity.getFileName())) && StringUtils.isNotEmpty(fileEntity.getFileText());
    }

    private boolean isSessionChildFile(String childName) {
        List<FileEntity> sessionChildFiles = fileEntity.getChildFiles();
        if (CollectionUtils.isEmpty(sessionChildFiles)) {
            return false;
        }
        for (FileEntity sessionChildFile : sessionChildFiles) {
            if (StringUtils.equals(sessionChildFile.getFileName(), childName)) {
                return true;
            }
        }
        return false;
    }

    private FileEntity findSessionChildFile(String childName) {
        List<FileEntity> sessionChildFiles = fileEntity.getChildFiles();
        FileEntity foundFile = null;
        for (FileEntity sessionChildFile : sessionChildFiles) {
            if (StringUtils.equals(sessionChildFile.getFileName(), childName)) {
                foundFile = sessionChildFile;
                break;
            }
        }
        return foundFile;
    }
}
