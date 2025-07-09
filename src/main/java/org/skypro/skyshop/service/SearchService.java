package org.skypro.skyshop.service;

import org.skypro.skyshop.model.search.SearchResult;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final StorageService storageService;

    public SearchService(StorageService storageService) {
        this.storageService = storageService;
    }

    public Collection<SearchResult> search(String pattern) {
        // Ранний возврат для null или пустого pattern
        if (pattern == null || pattern.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return storageService.getAllSearchables().stream()
                .filter(searchable -> containsIgnoreCase(searchable.getProductName(), pattern))
                .map(SearchResult::fromSearchable)
                .collect(Collectors.toList());

    }

    private boolean containsIgnoreCase(String text, String pattern) {
        if (text == null || pattern == null)
            return false;
        return text.toLowerCase().contains(pattern.toLowerCase());
    }
}
