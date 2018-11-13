package dip.lux.service.impl;

import dip.lux.model.FileEntity;
import dip.lux.model.util.Status;
import dip.lux.service.FileService;
import dip.lux.service.model.StatusType;
import dip.lux.service.util.PdfParser.PdfParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {

    private final String NEW_SECTION_REGEX = "\n{3,}";
    private final String NEW_SUBSECTION_REGEX = "\n{2,}";
    private final String TITLE_PAGE_LAST_ITEM = "(.+)\\s?(–|-)\\s?[0-9]{4}";

    @Autowired
    private PdfParser pdfParser;

    @Override
    public Map<String, Object> readFile(String fileName) {
        Map<String, Object> result = new HashMap<>();
        Status operationStatus = new Status();
        String fileText = null;
        operationStatus.setStatusType(StatusType.OK);
        try {
            fileText = pdfParser.parsePdf(UtilService.generateConvertedPath(fileName, fileName));
        } catch (IOException e) {
            operationStatus.setStatusType(StatusType.ERROR);
            operationStatus.setErrorMsg("Can not find file to read");
        }
        result.put("status", operationStatus);
        result.put("text", fileText);
        return result;
    }

    @Override
    public List<FileEntity> createDOM(String parentText, String parentName) {
        List<FileEntity> childFiles = new ArrayList<>();
        Map<String, String> sections = prepareAndSplitBySections(parentText);
        for (Map.Entry<String, String> section : sections.entrySet()) {
            String pathToFile = UtilService.generateConvertedPath(parentName, section.getKey());
            Status createFileStatus = UtilService.createFile(pathToFile, section.getValue());
            childFiles.add(childFileFactory(section.getKey(), section.getValue(), createFileStatus));
        }
        return childFiles;
    }

    private FileEntity childFileFactory(String childName, String childText, Status childStatus){
        FileEntity childFile = new FileEntity();
        childFile.setFileName(childName);
        childFile.setFileText(childText);
        childFile.setStatus(childStatus);
        return childFile;
    }

    private ArrayList<String> removeTitleListFromDOM(ArrayList<String> domStructure) {
        ArrayList<String> structureForDelete = new ArrayList<>();
        for (String domStructureElement : domStructure) {
            if (domStructureElement.matches(TITLE_PAGE_LAST_ITEM)) {
                structureForDelete.add(domStructureElement);
                break;
            }
            structureForDelete.add(domStructureElement);
        }
        domStructure.removeAll(structureForDelete);
        return domStructure;
    }

    private Map<String, String> prepareAndSplitBySections(String fileText) {
        ArrayList<String> rawSections = removePagingAndTitle(fileText);
        return formatSections(rawSections);
    }

    private Map<String, String> formatSections(ArrayList<String> rawSections) {
        Map<String, String> formattedSections = new HashMap<>();
        for (String rawSection : rawSections) {
            ArrayList<String> sectionNameAndText = new ArrayList<>(Arrays.asList(rawSection.split(NEW_SUBSECTION_REGEX)));
            if (sectionNameAndText.size() > 1) {
                String sectionName = sectionNameAndText.get(0);
                String sectionText = getSectionText(sectionNameAndText);
                formattedSections.put(sectionName, sectionText);
            }
        }
        return formattedSections;
    }

    private String getSectionText(ArrayList<String> sectionNameAndText) {
        StringBuilder sectionText = new StringBuilder();
        for (int i = 1; i < sectionNameAndText.size(); i++) {
            sectionText.append(sectionNameAndText.get(i));
        }
        return sectionText.toString();
    }

    private ArrayList<String> removePagingAndTitle(String text) {
        text = removePaging(text);
        ArrayList<String> sectionsWithTitle = new ArrayList<>(Arrays.asList(text.split(NEW_SECTION_REGEX)));
        return removeTitleListFromDOM(sectionsWithTitle);
    }

    private String removePaging(String fileContent) {
        return fileContent.replaceAll("\n[0-9]\n", "\n\n");
    }
}
