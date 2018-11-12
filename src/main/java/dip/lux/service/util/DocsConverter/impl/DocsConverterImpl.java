package dip.lux.service.util.DocsConverter.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.yeokhengmeng.docstopdfconverter.DocToPDFConverter;
import com.yeokhengmeng.docstopdfconverter.DocxToPDFConverter;
import com.yeokhengmeng.docstopdfconverter.OdtToPDF;
import dip.lux.service.ValidationService;
import dip.lux.service.impl.UtilService;
import dip.lux.service.util.DocsConverter.DocsConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

public class DocsConverterImpl implements DocsConverter {

    private final String CONVERTED_PATH = "C:\\Temp\\converted\\";
    private final String pdfType = ".pdf";

    @Autowired
    private ValidationService validationService;

    private DocToPDFConverter docToPDFConverter;
    private DocxToPDFConverter docxToPDFConverter;
    private OdtToPDF odtToPDFConverter;

    @Override
    public boolean convertByFormat(String format, File doc) throws IOException, DocumentException {
        InputStream docInput = new FileInputStream(doc);
        String name = doc.getName();
        String docNameWithoutFormat = UtilService.getNameWithoutFormat(name);
        String docName = docNameWithoutFormat + pdfType;
        String filePath = CONVERTED_PATH + docNameWithoutFormat;
        UtilService.createDirectoryIfNotExists(filePath);
        UtilService.createTemporaryPDF(filePath + File.separator + docName);
        OutputStream pdfOutput = new FileOutputStream(filePath + File.separator + docName);
        if (validationService.isDOC(format)) {
            return convertDocToPdf(docInput, pdfOutput);
        }
        if (validationService.isDOCX(format)) {
            return convertDocxToPdf(docInput, pdfOutput);
        }
        if (validationService.isODT(format)) {
            return convertOdtToPdf(docInput, pdfOutput);
        }
        return false;
    }

    private boolean convertDocToPdf(InputStream inputStream, OutputStream outputStream) {
        docToPDFConverter = new DocToPDFConverter(inputStream, outputStream, true, true);
        try {
            docToPDFConverter.convert();
            return true;
        } catch (Exception e) {
            System.out.println("Error in DocToPdf converter");
            e.printStackTrace();
            return false;
        }
    }

    private boolean convertDocxToPdf(InputStream inputStream, OutputStream outputStream) {
        docxToPDFConverter = new DocxToPDFConverter(inputStream, outputStream, true, true);
        try {
            docxToPDFConverter.convert();
            return true;
        } catch (Exception e) {
            System.out.println("Error in DocxToPdf converter");
            e.printStackTrace();
            return false;
        }
    }

    private boolean convertOdtToPdf(InputStream inputStream, OutputStream outputStream) {
        odtToPDFConverter = new OdtToPDF(inputStream, outputStream, true, true);
        try {
            odtToPDFConverter.convert();
            return true;
        } catch (Exception e) {
            System.out.println("Error in OdtToPdf converter");
            e.printStackTrace();
            return false;
        }
    }
}
