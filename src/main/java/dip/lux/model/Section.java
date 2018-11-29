package dip.lux.model;

import java.util.List;

public class Section {
    private static int idCounter = 1000;
    private Integer sectionId;
    private String sectionName;
    private String sectionText;
    private Integer sectionWeight;
    private List<Section> subSections;

    public Section() {
        this.sectionId = ++idCounter;
        this.sectionWeight = 0;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Section.idCounter = idCounter;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getSectionWeight() {
        return sectionWeight;
    }

    public void setSectionWeight(Integer sectionWeight) {
        this.sectionWeight = sectionWeight;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionText() {
        return sectionText;
    }

    public void setSectionText(String sectionText) {
        this.sectionText = sectionText;
    }

    public List<Section> getSubSections() {
        return subSections;
    }

    public void setSubSections(List<Section> subSections) {
        this.subSections = subSections;
    }
}
