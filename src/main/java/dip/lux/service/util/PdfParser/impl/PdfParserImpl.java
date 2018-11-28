package dip.lux.service.util.PdfParser.impl;

import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import dip.lux.service.util.PdfParser.PdfParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PdfParserImpl implements PdfParser {
    private PdfReader pdfReader;
    private PdfWriter pdfWriter;
    private PdfDocument pdfDocument;
    private final String LAST_TITLE_ITEM = "(\\p{L}){0,20} ?-?–? ?[0-9]{4}";

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

    private boolean isShiftedLines(String page) {
        List<String> pageLines = new ArrayList<>(Arrays.asList(page.replaceAll("\n+", "\n").split("\n")));
        for (int i = 0; i < pageLines.size(); i++) {
            if (pageLines.get(i).matches(LAST_TITLE_ITEM) && i != pageLines.size() - 1) {
                return true;
            }
        }
        return false;
    }
}
