package dip.lux.service.util.PdfParser.impl;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import dip.lux.service.util.PdfParser.PdfParser;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;

public class PdfParserImpl implements PdfParser {
    private PdfReader pdfReader;
    private String fullParsedFile;

    @Override
    public String parsePdf(String pdfFileName) throws IOException {
        return parsePdf(pdfFileName, null);
    }

    public String parsePdf(String pdfFileName, ArrayList<String> stopStrings) throws IOException {
        fullParsedFile = "";
        pdfReader = new PdfReader(pdfFileName);
        int pageNum = pdfReader.getNumberOfPages();
        for (int i = 1; i <= pageNum; i++) {
            String page = PdfTextExtractor.getTextFromPage(pdfReader, i);
            if(isNeedFullPage(page, stopStrings)){
                fullParsedFile += page;
            }
        }
        return fullParsedFile;
    }

    private boolean isNeedFullPage(String page, ArrayList<String> stopStrings) {
        if (CollectionUtils.isEmpty(stopStrings)){
            return true;
        }

        for(String stopString: stopStrings){
            if(page.toLowerCase().contains(stopString.toLowerCase() + "\n")){
                return false;
            }
        }
        return true;
    }
}
