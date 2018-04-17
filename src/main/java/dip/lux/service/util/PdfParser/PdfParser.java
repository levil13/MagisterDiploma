package dip.lux.service.util.PdfParser;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface PdfParser {
    String parsePdf(String pdfFileName) throws IOException;
}
