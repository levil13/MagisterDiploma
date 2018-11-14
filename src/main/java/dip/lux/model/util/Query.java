package dip.lux.model.util;

import java.math.BigInteger;
import java.util.Set;

public class Query {
    private BigInteger id;
    private String queryText;
    private Integer firstWordIndex;
    private Integer lastWordIndex;
    private String fileName;
    private Set<String> queryResponse;

    public Query(BigInteger id, String queryText, Integer firstWordIndex, Integer lastWordIndex, String fileName) {
        this.id = id;
        this.queryText = queryText;
        this.firstWordIndex = firstWordIndex;
        this.lastWordIndex = lastWordIndex;
        this.fileName = fileName;
    }

    public Set<String> getQueryResponse() {
        return queryResponse;
    }

    public void setQueryResponse(Set<String> queryResponse) {
        this.queryResponse = queryResponse;
    }

    public Query(BigInteger id) {
        this.id = id;
    }

    public Query() {
    }

    public Integer getFirstWordIndex() {
        return firstWordIndex;
    }

    public void setFirstWordIndex(Integer firstWordIndex) {
        this.firstWordIndex = firstWordIndex;
    }

    public Integer getLastWordIndex() {
        return lastWordIndex;
    }

    public void setLastWordIndex(Integer lastWordIndex) {
        this.lastWordIndex = lastWordIndex;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String words) {
        this.queryText = words;
    }
}
