package dip.lux.controller;

import dip.lux.model.FileEntity;
import dip.lux.service.ShingleService;
import dip.lux.service.UploadService;
import dip.lux.service.UtilService;
import dip.lux.service.ValidationService;
import dip.lux.service.util.PdfParser.PdfParser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    private PdfParser pdfParser;

    @Autowired
    private UtilService utilService;

    @Autowired
    private ShingleService shingleService;

    @PostMapping("/upload")
    public ResponseEntity singleFileUpload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("errorMsg", "File is empty");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(!validationService.isValidFileType(file.getOriginalFilename())){
            response.put("errorMsg", "Illegal file format");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String uploadedFileName = uploadService.upload(file);
        if(uploadedFileName.equalsIgnoreCase("ERROR")){
            response.put("errorMsg", "ERRROR AHTUNG BLYAT");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        fileEntity.setFileName(uploadedFileName);

        response.put("fileName", uploadedFileName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/read", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public String readFile(@RequestParam String fileName){
        File file = new File(fileName);
        try {
            ArrayList<String> stopStrings = new ArrayList<>();
            stopStrings.add("Курсовая работа");
            stopStrings.add("Заключение");
            stopStrings.add("Литература");
            stopStrings.add("Контрольный пример");
            return shingleService.canonize(pdfParser.parsePdf("C:\\Temp\\converted\\" + utilService.getNameWithoutFormat(file) + ".pdf", stopStrings));
        } catch (IOException e) {
            e.printStackTrace();
            return "No such file for parsing";
        }
    }

    @PostMapping("/current-file-name")
    @ResponseBody
    public ResponseEntity getCurrentFileName(){
        Map<String, Object> response = new HashMap<>();
        String fileName = fileEntity.getFileName();
        if(StringUtils.isEmpty(fileName)){
            response.put("errorMsg", "No current file name");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("fileName", fileName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
