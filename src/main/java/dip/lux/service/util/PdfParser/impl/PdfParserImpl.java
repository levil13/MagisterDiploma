package dip.lux.service.util.PdfParser.impl;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import dip.lux.service.util.PdfParser.PdfParser;

import java.io.IOException;

public class PdfParserImpl implements PdfParser {
    private PdfReader pdfReader;
    private String fullParsedFile;

    @Override
    public String parsePdf(String pdfFileName) throws IOException {
        fullParsedFile = "";
        pdfReader = new PdfReader(pdfFileName);
        int pageNum = pdfReader.getNumberOfPages();
        for (int i = 1; i <= pageNum; i++) {
            String page = PdfTextExtractor.getTextFromPage(pdfReader, i);
            fullParsedFile += page;
        }
        return fullParsedFile;
    }
}
