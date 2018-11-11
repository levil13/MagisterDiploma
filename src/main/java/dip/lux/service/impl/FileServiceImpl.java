package dip.lux.service.impl;

import dip.lux.service.FileService;
import dip.lux.service.util.PdfParser.PdfParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    private final String CONVERTED_PATH = "C:\\Temp\\converted\\";

    @Autowired
    private PdfParser pdfParser;

    @Override
    public Map<String, String> readFile(String fileName) {
        Map<String, String> result = new HashMap<>();
        String fileContent = null;
        result.put("status", "OK");
        try {
            fileContent = pdfParser.parsePdf(CONVERTED_PATH + fileName + "\\" + fileName + ".pdf");
        } catch (IOException e) {
            result.put("status", "ERROR");
        }
        result.put("text", fileContent);
        return result;
    }

    @Override
    public ArrayList<String> createDOM(String fileContent) {
        return null;
    }
}
