package dip.lux.service.impl;

import dip.lux.model.Section;
import dip.lux.model.util.Query;
import dip.lux.model.util.Status;
import dip.lux.service.FileService;
import dip.lux.service.SearchQueryService;
import dip.lux.service.model.StatusType;
import dip.lux.service.util.PdfParser.PdfParser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {

    private final String NEW_SUBSECTION_REGEX = "([0-9]+\\.?)+ ((\\p{L})+\\s*?-?)+";
    private final String NEW_SUBSECTION_NUMBER_REGEX = "([0-9]+\\.?)+";
    private final String SUBSECTION_INSIDE_TEXT = "(\\p{L})+([0-9]+\\.?)+ ((\\p{L})+\\s*?-?)+";
    private final String SPLIT_BY_WORDS_REGEX = " ";
    private final String SPLIT_BY_PAGES = "\nNPD\n";
    private final String PAGING_REGEX = "([0-9])+(((\\s?)+(\n)+))+";
    private final String ADDITION = ".*ДОДАТОК.*";
    private final String CYRILLIC_LETTERS = ".*(\\p{L}).*";
    private final String MAIN_SECTION = "[0-9]+(_(\\p{L}+)_?)+(-?(\\p{L}+)_?)+";

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
    public List<Section> createDOM(String parentText, String parentName) {
        List<Section> sections = prepareSections(parentText);
        for (Section section : sections) {
            String pathToFile = UtilService.generateConvertedPath(parentName, section.getSectionName());
            UtilService.createFile(pathToFile, section.getSectionText());
        }
        List<Section> sectionsWithSubs = prepareSubSections(sections);
        return sectionsWithSubs;
    }

    private List<Section> prepareSubSections(List<Section> parentSections) {
        for (Section parentSection : parentSections) {
            if (isSubSectionsExists(parentSection)) {
                List<Section> subSections = new ArrayList<>();
                List<String> parentSectionTextLines = new ArrayList<>(Arrays.asList(parentSection.getSectionText().split("\n")));
                parentSectionTextLines = exctractSubsectionsFromText(parentSectionTextLines);
                for (String parentSectionTextLine : parentSectionTextLines) {
                    if (isNewSubSection((parentSectionTextLine))) {
                        Section subSection = new Section();
                        String sectionName = parentSectionTextLine;
                        subSection.setSectionName(sectionName);
                        subSections.add(subSection);
                    } else {
                        if (subSections.size() > 0) {
                            Section lastSubSection = subSections.get(subSections.size() - 1);
                            String lastSubSectionText = lastSubSection.getSectionText();
                            if (lastSubSectionText == null) {
                                lastSubSectionText = "";
                            }
                            String lastSectionNewText = lastSubSectionText + parentSectionTextLine;
                            lastSubSection.setSectionText(lastSectionNewText);
                        }
                    }
                }
                parentSection.setSubSections(subSections);
            }
        }
        return parentSections;
    }

    private boolean isSubSectionsExists(Section parentSection) {
        return parentSection.getSectionName().matches(MAIN_SECTION);
    }

    private List<String> exctractSubsectionsFromText(List<String> textLines) {
        List<String> lines = new ArrayList<>();
        for(String textLine: textLines){
            StringBuilder newLine = new StringBuilder();
            List<String> words = new ArrayList<>(Arrays.asList(textLine.split(SPLIT_BY_WORDS_REGEX)));
            for(String word: words){
                if(word.matches(SUBSECTION_INSIDE_TEXT)){
                    List<String> wordParts = new ArrayList<>(Arrays.asList(textLine.split(NEW_SUBSECTION_NUMBER_REGEX)));
                    newLine.append(wordParts.get(1));
                } else {
                    newLine.append(word);
                }
                newLine.append(" ");
            }
            lines.add(newLine.toString());
        }

        return lines;
    }

    private boolean isNewSubSection(String firstLine) {
        return firstLine.matches(NEW_SUBSECTION_REGEX);
    }

    @Override
    public List<Query> getQueries(String text, String fileName) {
        List<String> words = new ArrayList<>(Arrays.asList(text.split(SPLIT_BY_WORDS_REGEX)));
        return setFileNameToQuery(generateQueries(words), fileName);
    }

    @Override
    public Map<String, Object> search(List<Query> queries) {
        Map<String, Object> result = new HashMap<>();
        Status operationStatus = new Status();
        operationStatus.setStatusType(StatusType.OK);
        //        String queryText = queries.get(3).getQueryText();
//        Map<String, Object> searchResult = searchQueryService.searchQuery(queryText);
//        queries.get(3).setQueryResponse((Set<String>) searchResult.get("response"));
        for (Query query : queries) {
            String queryText = query.getQueryText();
            try {
                Thread.sleep(500);
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
            Query query = new Query();
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

    private List<Section> prepareSections(String text) {
        List<String> pages = new ArrayList<>(Arrays.asList(text.split(SPLIT_BY_PAGES)));
        pages = removePaging(pages);
        List<Section> sections = new ArrayList<>();
        for (String page : pages) {
            List<String> lines = new ArrayList<>(Arrays.asList(page.split("\n")));
            String firstLine = "";
            for (String line : lines) {
                if (StringUtils.isNotEmpty(line.trim())) {
                    firstLine = line;
                    break;
                }
            }
            StringBuilder sectionText = new StringBuilder();
            if (isNewSection(firstLine)) {
                Section section = new Section();
                List<String> pageElements = new ArrayList<>(Arrays.asList(page.split(" \n \n")));
                String sectionName = pageElements.get(0)
                        .replaceAll("\n", "")
                        .replaceAll(" ", "_");

                for (int i = 1; i < pageElements.size(); i++) {
                    sectionText.append(pageElements.get(i));
                }
                section.setSectionName(sectionName);
                section.setSectionText(sectionText.toString());
                sections.add(section);
            } else {
                if (sections.size() > 0) {
                    Section lastSection = sections.get(sections.size() - 1);
                    String lastSectionText = lastSection.getSectionText();
                    String lastSectionNewText = lastSectionText + "\n" + page;
                    lastSection.setSectionText(lastSectionNewText);
                }
            }

        }
        return sections;
    }

    private boolean isNewSection(String firstLine) {
        return firstLine.toUpperCase().matches(CYRILLIC_LETTERS) && (firstLine.toUpperCase().equals(firstLine) || firstLine.toUpperCase().matches(ADDITION));
    }

    private List<String> removePaging(List<String> pages) {
        List<String> pagesWithoutPaging = new ArrayList<>();
        for (String page : pages) {
            pagesWithoutPaging.add(page.replaceAll(PAGING_REGEX, ""));
        }
        return pagesWithoutPaging;
    }
}
