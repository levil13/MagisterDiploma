package dip.lux.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import dip.lux.model.FileEntity;
import dip.lux.service.FileService;
import dip.lux.service.util.PdfParser.PdfParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    private final String CONVERTED_PATH = "C:\\Temp\\converted\\";
    private final String NEW_SECTION_REGEX = "\n{3,}";
    private final String NEW_SUBSECTION_REGEX = "\n{2,}";
    private final String TITLE_PAGE_LAST_ITEM = "(.+)\\s?(–|-)\\s?[0-9]{4}";
    private final String pdfType = ".pdf";

    @Autowired
    private PdfParser pdfParser;

    @Autowired
    private FileEntity fileEntity;

    @Override
    public Map<String, String> readFile(String fileName) {
        Map<String, String> result = new HashMap<>();
        String fileContent = null;
        result.put("status", "OK");
        try {
            fileContent = pdfParser.parsePdf(CONVERTED_PATH + fileName + "\\" + fileName + ".pdf");
        } catch (IOException e) {
            result.put("status", "ERROR");
        }
        result.put("text", fileContent);
        return result;
    }

    @Override
    public Map<String, String> readChildFile(String parentName, String childName) {
        Map<String, String> result = new HashMap<>();
        String fileContent = null;
        result.put("status", "OK");
        try {
            fileContent = pdfParser.parsePdf(CONVERTED_PATH + parentName + "\\" + childName + ".pdf");
        } catch (IOException e) {
            result.put("status", "ERROR");
        }
        result.put("text", fileContent);
        return result;
    }

    @Override
    public ArrayList<String> createDOM(String fileContent) {
        fileContent = removePaging(fileContent);
        ArrayList<String> domFiles = new ArrayList<>();
        ArrayList<String> splittedFile = new ArrayList<>(Arrays.asList(fileContent.split(NEW_SECTION_REGEX)));
        ArrayList<String> splittedFileWithoutTitle = removeTitleListFromDOM(splittedFile);
        for(String section: splittedFileWithoutTitle){
            ArrayList<String> splittedSection = new ArrayList<>(Arrays.asList(section.split(NEW_SUBSECTION_REGEX)));
            if(splittedSection.size() > 1){
                String path = CONVERTED_PATH + fileEntity.getFileName();
                String name = path + File.separator + splittedSection.get(0) + pdfType;
                try {
                    Document document = UtilService.createTemporaryPDF(name);
                    final BaseFont bf = BaseFont.createFont("C:\\Windows\\Fonts\\Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    final Font font = new Font(bf);
                    for (int i = 1; i < splittedSection.size(); i++) {
                        document.add(new Paragraph(splittedSection.get(i), font));
                    }
                    document.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                domFiles.add(splittedSection.get(0));
            }
        }
        return domFiles;
    }

    private ArrayList<String> removeTitleListFromDOM(ArrayList<String> domStructure){
        ArrayList<String> structureForDelete = new ArrayList<>();
        for(String domStructureElement: domStructure){
            if(domStructureElement.matches(TITLE_PAGE_LAST_ITEM)){
                structureForDelete.add(domStructureElement);
                break;
            }
            structureForDelete.add(domStructureElement);
        }
        domStructure.removeAll(structureForDelete);
        return domStructure;
    }

    private String removePaging(String fileContent) {
        return fileContent.replaceAll("\n[0-9]\n","\n\n");
    }
}
