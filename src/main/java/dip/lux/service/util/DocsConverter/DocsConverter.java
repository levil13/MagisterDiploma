package dip.lux.service.util.DocsConverter;

import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public interface DocsConverter {
    boolean convertByFormat(String format, File file) throws IOException, DocumentException;
}
