package org.skypro.skyshop.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.SearchResult;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SearchService searchService;

    private Product createTestProduct(String name) {
        return new SimpleProduct(UUID.randomUUID(), name, 100);
    }

    private Article createTestArticle(String title) {
        return new Article(UUID.randomUUID(), title, "Test content");
    }

    @Test
    public void search_WhenNoObjectsInStorage_ReturnsEmptyList() {

        when(storageService.getAllSearchables()).thenReturn(Collections.emptyList());

        Collection<SearchResult> results = searchService.search("яблоки");
        assertTrue(results.isEmpty());
        verify(storageService, times(1)).getAllSearchables();
    }

    @Test
    public void search_WhenNoMatchingObjects_ReturnsEmptyList() {

        Product product = createTestProduct("бананы");
        Article article = createTestArticle("Колбаса");
        when(storageService.getAllSearchables())
                .thenReturn(Arrays.asList(product, article));

        Collection<SearchResult> results = searchService.search("яблоки");

        assertTrue(results.isEmpty());
        verify(storageService, times(1)).getAllSearchables();
    }

    @Test
    public void search_WhenOneMatchingProduct_ReturnsOneResult() {
        Product matchingProduct = createTestProduct("яблоки");
        Product nonMatchingProduct = createTestProduct("бананы");
        when(storageService.getAllSearchables())
                .thenReturn(Arrays.asList(matchingProduct, nonMatchingProduct));


        Collection<SearchResult> results = searchService.search("яблок");

        assertEquals(1, results.size());
        SearchResult result = results.iterator().next();

        assertEquals(matchingProduct.getProductName(), result.getName());

        verify(storageService, times(1)).getAllSearchables();
    }

    @Test
    public void search_WhenMultipleMatchingObjects_ReturnsAllResults() {

        Product matchingProduct1 = createTestProduct("яблоки Гольден");
        Product matchingProduct2 = createTestProduct("зелёные яблоки");
        Article matchingArticle = createTestArticle("Статья про яблоки");
        Article nonMatchingArticle = createTestArticle("Статья про яблоНи");
        when(storageService.getAllSearchables())
                .thenReturn(Arrays.asList(matchingProduct1, matchingProduct2, matchingArticle, nonMatchingArticle));

        Collection<SearchResult> results = searchService.search("яблок");

        assertEquals(3, results.size());
        verify(storageService, times(1)).getAllSearchables();
    }

    @Test
    public void search_IsCaseInsensitive_ReturnsResults() {

        Product product = createTestProduct("ЯбЛоКи");
        when(storageService.getAllSearchables()).thenReturn(Collections.singletonList(product));

        Collection<SearchResult> results = searchService.search("ябл");

        assertEquals(1, results.size());
        verify(storageService, times(1)).getAllSearchables();
    }

    @Test
    public void search_WithEmptyPattern_ReturnsEmptyList() {

        Collection<SearchResult> results = searchService.search("");

        assertTrue(results.isEmpty());

        verify(storageService, never()).getAllSearchables();
    }

    @Test
    public void search_WithNullPattern_ReturnsEmptyList() {
        Collection<SearchResult> results = searchService.search(null);

        assertTrue(results.isEmpty());

        verify(storageService, never()).getAllSearchables();
    }
}