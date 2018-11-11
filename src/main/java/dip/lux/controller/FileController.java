package dip.lux.controller;

import dip.lux.model.FileEntity;
import dip.lux.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    private UtilService utilService;

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
        if (uploadedFileName.equalsIgnoreCase("ERROR")) {
            response.put("errorMsg", "ERRROR AHTUNG BLYAT");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        fileEntity.setFileName(utilService.getNameWithoutFormat(uploadedFileName));

        response.put("fileName", uploadedFileName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/read/{fileName}")
    @ResponseBody
    public ResponseEntity readFile(@PathVariable String fileName) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> fileContent = getSessionOrCustomFileContent(fileName);
        if (fileContent.get("status").equals("ERROR")) {
            response.put("errorMsg", "Error during reading file");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String text = fileContent.get("text");

        fileEntity.setFileContent(text);
        response.put("fileText", text);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/canonize/{fileName}")
    @ResponseBody
    public ResponseEntity canonizeFile(@PathVariable String fileName) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> fileContent = getSessionOrCustomFileContent(fileName);
        if (fileContent.get("status").equals("ERROR")) {
            response.put("errorMsg", "Error during reading file");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String canonizedFile = shingleService.canonize(fileContent.get("text"));
        response.put("canonizedText", canonizedFile);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/create-dom/{fileName}")
    @ResponseBody
    public ResponseEntity createDOM(@PathVariable String fileName) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> fileContent = getSessionOrCustomFileContent(fileName);
        if (fileContent.get("status").equals("ERROR")) {
            response.put("errorMsg", "Error during reading file");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<String> domFiles = fileService.createDOM(fileContent.get("text"));
        response.put("domFiles", domFiles);

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

    private Map<String, String> getSessionOrCustomFileContent(String fileName) {
        Map<String, String> fileContent = new HashMap<>();
        String sessionFileContent = fileEntity.getFileContent();
        if (StringUtils.isEmpty(sessionFileContent)) {
            fileContent = fileService.readFile(fileName);
        } else if (isFileToReadCurrent(fileName)) {
            fileContent.put("text", sessionFileContent);
            fileContent.put("status", "OK");
        }
        return fileContent;
    }

    private boolean isFileToReadCurrent(String fileName) {
        return fileName.equals(fileEntity.getFileName());
    }
}
