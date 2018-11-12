package dip.lux.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class UtilService {
    private UtilService() {
    }

    public static boolean createDirectoryIfNotExists(String path) {
        File dir = new File(path);
        return dir.exists() || dir.mkdirs();
    }

    public static String getFileFormat(String name) {
        String[] nameParts = name.split("\\.");
        if(nameParts.length != 2){
            return "Wrong file format";
        }
        return nameParts[1];
    }

    public static String getNameWithoutFormat(String name) {
        String[] nameParts = name.split("\\.");
        if(nameParts.length != 2){
            return "Wrong file format";
        }
        return nameParts[0];
    }

    public static Document createTemporaryPDF(String pdfName) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfName));
        document.open();
        document.add(new Paragraph("This is a temporary pdf"));
        return document;
    }


}
