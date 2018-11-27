package dip.lux.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import dip.lux.model.util.Status;
import dip.lux.service.model.StatusType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;


public class UtilService {
    private static final String CONVERTED_PATH = "C:\\Temp\\converted";
    private static final String pdfType = ".pdf";

    private UtilService() {
    }

    public static boolean createDirectoryIfNotExists(String path) {
        File dir = new File(path);
        return dir.exists() || dir.mkdirs();
    }

    public static String getFileFormat(String name) {
        if(name == null){
            return "Empty name";
        }
        String[] nameParts = name.split("\\.");
        if (nameParts.length != 2) {
            return "Wrong file format";
        }
        return nameParts[1];
    }

    public static String getNameWithoutFormat(String name) {
        String[] nameParts = name.split("\\.");
        if (nameParts.length != 2) {
            return "Wrong file format";
        }
        return nameParts[0];
    }

    public static Document createTemporaryPDF(String pdfName) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfName));
        document.open();
        document.add(new Paragraph("TemporaryPDF"));
        return document;
    }

    public static String generateConvertedPath(String... pathVariables) {
        StringBuilder generatedPath = new StringBuilder(CONVERTED_PATH);
        for (String pathVariable : pathVariables) {
            generatedPath.append(File.separator);
            generatedPath.append(pathVariable);
        }
        generatedPath.append(pdfType);
        return generatedPath.toString();
    }

    public static String generateConvertedFolderPath(String... pathVariables) {
        StringBuilder generatedPath = new StringBuilder(CONVERTED_PATH);
        for (String pathVariable : pathVariables) {
            generatedPath.append(File.separator);
            generatedPath.append(pathVariable);
        }
        return generatedPath.toString();
    }

    public static Status createFile(String path, String content) {
        Status operationStatus = new Status();
        operationStatus.setStatusType(StatusType.OK);
        try {
            Document document = UtilService.createTemporaryPDF(path);
            final BaseFont bf = BaseFont.createFont("C:\\Windows\\Fonts\\Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            final Font font = new Font(bf);
            document.add(new Paragraph(content, font));
            document.close();
        } catch (FileNotFoundException e) {
            operationStatus.setStatusType(StatusType.ERROR);
            operationStatus.setErrorMsg("File not found");
            e.printStackTrace();
        } catch (DocumentException e) {
            operationStatus.setStatusType(StatusType.ERROR);
            operationStatus.setErrorMsg("Document error");
            e.printStackTrace();
        } catch (IOException e) {
            operationStatus.setStatusType(StatusType.ERROR);
            operationStatus.setErrorMsg("IOException");
            e.printStackTrace();
        }
        return operationStatus;
    }

    public static String createQuery(List<String> queryParams) {
        StringBuilder query = new StringBuilder("\"");
        for (int i = 0; i < queryParams.size(); i++) {
            query.append(queryParams.get(i));
            if(i != queryParams.size() - 1){
                query.append("%20");
            }
        }
        query.append("\"");
        return query.toString();
    }

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


}
