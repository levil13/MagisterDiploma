package dip.lux.service.impl;

import dip.lux.model.FileEntity;
import dip.lux.model.util.Query;
import dip.lux.model.util.Status;
import dip.lux.service.FileService;
import dip.lux.service.SearchQueryService;
import dip.lux.service.model.StatusType;
import dip.lux.service.util.PdfParser.PdfParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {

    private final String NEW_SECTION_REGEX = "\n{3,}";
    private final String NEW_SUBSECTION_REGEX = "\n{2,}";
    private final String TITLE_PAGE_LAST_ITEM = "(.+)\\s?(–|-)\\s?[0-9]{4}";
    private final String SPLIT_BY_WORDS_REGEX = " ";

    @Autowired
    private SearchQueryService searchQueryService;

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

    @Override
    public List<Query> getQueries(String text, String fileName) {
        List<String> words = new ArrayList<>(Arrays.asList(text.split(SPLIT_BY_WORDS_REGEX)));
        return setFileNameToQuery(generateQueries(words), fileName);
    }

    @Override
    public Map<String, Object> search(List<Query> queries){
        Map<String, Object> result = new HashMap<>();
        Status operationStatus = new Status();
        operationStatus.setStatusType(StatusType.OK);
        //        String queryText = queries.get(3).getQueryText();
//        Map<String, Object> searchResult = searchQueryService.searchQuery(queryText);
//        queries.get(3).setQueryResponse((Set<String>) searchResult.get("response"));
        for (Query query : queries) {
            String queryText = query.getQueryText();
            try {
                Thread.sleep(UtilService.getRandomNumberInRange(3000, 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Map<String, Object> searchResult = searchQueryService.searchQuery(queryText);
            Status searchStatus = (Status) searchResult.get("status");
            if (searchStatus.getStatusType().equals(StatusType.ERROR)) {
                operationStatus.setStatusType(StatusType.ERROR);
                operationStatus.setErrorMsg(searchStatus.getErrorMsg());
            }
            query.setQueryResponse((Set<String>) searchResult.get("response"));
        }
        result.put("status", operationStatus);
        result.put("queriesWithResponses", queries);
        return result;
    }

    private List<Query> setFileNameToQuery(List<Query> queriesWithoutFileName, String fileName) {
        List<Query> queriesWithFileNames = new ArrayList<>();
        for (Query queryWithoutFileName : queriesWithoutFileName) {
            queryWithoutFileName.setFileName(fileName);
            queriesWithFileNames.add(queryWithoutFileName);
        }
        return queriesWithFileNames;
    }

    private List<Query> generateQueries(List<String> words) {
        List<Query> queries = new ArrayList<>();
        for (int queryFirstWordIndex = 0; queryFirstWordIndex < words.size(); queryFirstWordIndex += 3) {
            Query query = new Query(BigInteger.valueOf(new Random(1000000).nextInt()));
            int queryLastWordIndex = queryFirstWordIndex + 5;
            if (queryLastWordIndex >= words.size()) {
                queryLastWordIndex = words.size();
            }
            List<String> queryWords = words.subList(queryFirstWordIndex, queryLastWordIndex);
            String queryText = UtilService.createQuery(queryWords);

            query.setFirstWordIndex(queryFirstWordIndex);
            query.setLastWordIndex(queryLastWordIndex);
            query.setQueryText(queryText);

            queries.add(query);
        }
        return queries;
    }

    private FileEntity childFileFactory(String childName, String childText, Status childStatus) {
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
