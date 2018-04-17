package dip.lux.controller;

import com.itextpdf.text.DocumentException;
import dip.lux.service.util.DocsConverter.DocsConverter;
import dip.lux.service.util.PdfParser.PdfParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/rest")
public class RestTestController {
    @Autowired
    PdfParser pdfParser;

    @Autowired
    DocsConverter docsConverter;

    @RequestMapping(method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public String testPdfParser() throws IOException, DocumentException {
        File newDoc = new File("/raw/test2.docx");
        String format = docsConverter.getFileFormat(newDoc);
        docsConverter.convertByFormat(format, newDoc);
        try {
            return pdfParser.parsePdf("/converted/test2.pdf");
        } catch (IOException e) {
            e.printStackTrace();
            return "No such file for parsing";
        }
    }
}
