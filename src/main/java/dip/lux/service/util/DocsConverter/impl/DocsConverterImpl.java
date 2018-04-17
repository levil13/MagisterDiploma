package dip.lux.service.util.DocsConverter.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.yeokhengmeng.docstopdfconverter.DocToPDFConverter;
import com.yeokhengmeng.docstopdfconverter.DocxToPDFConverter;
import com.yeokhengmeng.docstopdfconverter.OdtToPDF;
import dip.lux.service.util.DocsConverter.DocsConverter;


import java.io.*;

public class DocsConverterImpl implements DocsConverter {

    private DocToPDFConverter docToPDFConverter;
    private DocxToPDFConverter docxToPDFConverter;
    private OdtToPDF odtToPDFConverter;
    private final String path = "C:\\Users\\Lux\\IdeaProjects\\MagisterDiploma\\src\\main\\resources";

    private InputStream docInputStream(File newDoc) throws IOException {
        return new FileInputStream(path + newDoc);
    }

    private void createTemporaryPDF(String pdfName) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfName));
        document.open();
        document.add(new Paragraph("This is a temporary pdf"));
        document.close();
    }

    @Override
    public boolean convertByFormat(String format, File doc) throws IOException, DocumentException {
        InputStream docInput = docInputStream(doc);
        createTemporaryPDF(path + "\\converted\\test2.pdf");
        OutputStream pdfOutput = new FileOutputStream(path + "\\converted\\test2.pdf");
        switch (format) {
            case "DOC":
                return convertDocToPdf(docInput, pdfOutput);
            case "DOCX":
                return convertDocxToPdf(docInput, pdfOutput);
            case "ODT":
                return convertOdtToPdf(docInput, pdfOutput);
            default:
                return false;
        }
    }

    @Override
    public String getFileFormat(File doc) {
        String docName = doc.getName();
        if (docName.length() >= 4) {
            return isAvailableFileFormat(docName.substring(docName.length() - 4));
        } else {
            return "Wrong file name size";
        }
    }

    private String isAvailableFileFormat(String docFileFormat) {
        switch (docFileFormat) {
            case ".doc":
                return "DOC";
            case "docx":
                return "DOCX";
            case ".odt":
                return "ODT";
            default:
                return "ERROR";
        }
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
