package dip.lux.service.impl;

import dip.lux.model.util.Status;
import dip.lux.service.SearchQueryService;
import dip.lux.service.model.StatusType;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SearchQueryServiceImpl implements SearchQueryService {
    private static Pattern patternDomainName;
    private Matcher matcher;
    private static final String DOMAIN_NAME_PATTERN
            = "([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}";
    static {
        patternDomainName = Pattern.compile(DOMAIN_NAME_PATTERN);
    }

    public Map<String, Object> searchQuery(String query) {
        Status operationStatus = new Status();
        operationStatus.setStatusType(StatusType.OK);

        Set<String> response = new HashSet<>();
        Map<String, Object> result = new HashMap<>();
        String request = "https://www.google.com/search?q=" + query + "&num=5";

        try {
            Document connection = initConnection(request);

            // get all links
            Elements links = connection.select("a[href]");
            for (Element link : links) {
                String temp = link.attr("href");
                if(temp.startsWith("/url?q=")){
                    response.add(getDomainName(temp));
                }

            }

        } catch (HttpStatusException e) {
            e.printStackTrace();
            operationStatus.setErrorMsg("Error in html parser");
            operationStatus.setStatusType(StatusType.ERROR);
        } catch (IOException e) {
            e.printStackTrace();
        }

        result.put("status", operationStatus);
        result.put("response", response);

        return result;
    }

    private Document initConnection(String request) throws IOException {
        return Jsoup
                .connect(request)
                .userAgent(
                        "Mozilla/5.0 AppleWebKit/537.36 " +
                                "(KHTML, like Gecko; compatible; Googlebot/2.1; " +
                                "+http://www.google.com/bot.html) Safari/537.36")
                .timeout(5000).get();
    }

    private String getDomainName(String url){
        String domainName = "";
        matcher = patternDomainName.matcher(url);
        if (matcher.find()) {
            domainName = matcher.group(0).toLowerCase().trim();
        }
        return domainName;

    }
}
