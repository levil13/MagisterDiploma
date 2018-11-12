package dip.lux.service.util.PdfParser;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface PdfParser {
    String parsePdf(String pdfFileName) throws IOException;

    String parsePdf(String pdfFileName, List<String> stopStrings) throws IOException;
}
