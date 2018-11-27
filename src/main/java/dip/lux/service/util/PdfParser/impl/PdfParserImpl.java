package dip.lux.service.util.PdfParser.impl;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import dip.lux.service.util.PdfParser.PdfParser;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

public class PdfParserImpl implements PdfParser {
    private PdfReader pdfReader;
    private String fullParsedFile;

    @Override
    public String parsePdf(String pdfFileName) throws IOException {
        return parsePdf(pdfFileName, null);
    }

    public String parsePdf(String pdfFileName, List<String> stopStrings) throws IOException {
        StringBuilder fullParsedFile = new StringBuilder();
        pdfReader = new PdfReader(pdfFileName);
        int pageNum = pdfReader.getNumberOfPages();
        for (int i = 1; i <= pageNum; i++) {
            try {
                String page = PdfTextExtractor.getTextFromPage(pdfReader, i);
                if (i > 1) {
                    fullParsedFile.append("\nNPD\n");
                }
                fullParsedFile.append(page);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fullParsedFile.toString();
    }

    private boolean isNeedFullPage(String page, List<String> stopStrings) {
        if (CollectionUtils.isEmpty(stopStrings)) {
            return true;
        }

        for (String stopString : stopStrings) {
            if (page.toLowerCase().contains(stopString.toLowerCase() + "\n")) {
                return false;
            }
        }
        return true;
    }
}
