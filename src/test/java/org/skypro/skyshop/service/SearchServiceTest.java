package org.skypro.skyshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.FixPriceProduct;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class SearchServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SearchService searchService;


    // Вспомогательный метод для создания тестовых продуктов
    private List<Product> createTestProducts() {
        return List.of(
                new SimpleProduct(UUID.randomUUID(),"simpleTestProduct", 80),
                new DiscountedProduct(UUID.randomUUID(),"discountedTestProduct", 40,20),
                new FixPriceProduct(UUID.randomUUID(),"fixPriceTestProduct")
        );
    }

    // Вспомогательный метод для создания тестовых статей
    private List<Article> createTestArticles() {
        return List.of(
                new Article(UUID.randomUUID(),"testArticle1", "testContent1"),
                new Article(UUID.randomUUID(),"testArticle2", "testContent12")
        );
    }
    @Test
    void search_shouldReturnEmptyList_whenStorageIsEmpty() {
        // Устанавливаем поведение мока
        when(storageService.getAllSearchables()).thenReturn(Collections.emptyList());

        // Вызываем тестируемый метод
        Collection<SearchResult> result = searchService.search("молоко");

        // Проверяем результат
        assertTrue(result.isEmpty(), "Должен вернуться пустой список");

        // Проверяем, что метод мока был вызван
        verify(storageService).getAllSearchables();
    }
}
