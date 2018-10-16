package dip.lux.controller;

import dip.lux.service.ShingleService;
import dip.lux.service.UtilService;
import dip.lux.service.util.PdfParser.PdfParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

@RestController
public class FileReadController {
    @Autowired
    PdfParser pdfParser;

    @Autowired
    UtilService utilService;

    @Autowired
    ShingleService shingleService;

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
}
