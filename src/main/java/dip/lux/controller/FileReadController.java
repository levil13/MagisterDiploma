package dip.lux.controller;

import dip.lux.service.UtilService;
import dip.lux.service.util.PdfParser.PdfParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
public class FileReadController {
    @Autowired
    PdfParser pdfParser;

    @Autowired
    UtilService utilService;

    @RequestMapping(value = "/read", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public String readFile(@RequestParam String fileName){
        File file = new File(fileName);
        try {
            return pdfParser.parsePdf("C:\\Temp\\converted\\" + utilService.getNameWithoutFormat(file) + ".pdf");
        } catch (IOException e) {
            e.printStackTrace();
            return "No such file for parsing";
        }
    }
}
