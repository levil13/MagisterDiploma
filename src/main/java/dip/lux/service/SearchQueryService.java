package dip.lux.service;

import java.util.Map;

public interface SearchQueryService {
    Map<String, Object> searchQuery(String query);
}
