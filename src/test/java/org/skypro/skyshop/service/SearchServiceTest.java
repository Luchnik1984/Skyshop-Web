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

    // Вспомогательный метод для создания тестового продукта
    private Product createTestProduct(String name) {
        return new SimpleProduct(UUID.randomUUID(), name, 100);
    }

    // Вспомогательный метод для создания тестовой статьи
    private Article createTestArticle(String title) {
        return new Article(UUID.randomUUID(), title, "Test content");
    }

    // Тест 1: Поиск, когда в StorageService нет объектов
    @Test
    public void search_WhenNoObjectsInStorage_ReturnsEmptyList() {
        // Подготовка
        // Настраиваем заглушку. При вызове метода возвращаем пустой список.
        when(storageService.getAllSearchables()).thenReturn(Collections.emptyList());

        // Действие
        // Сохраняем результат поиска запроса по пустому списку в коллекцию
        Collection<SearchResult> results = searchService.search("яблоки");

        // Проверка
        // Проверяем, действительно ли коллекция с результатом поиска пустая. Результат поиска отсутствует.
        assertTrue(results.isEmpty());
        // Проверяем что метод был вызван 1 раз
        verify(storageService, times(1)).getAllSearchables();
    }

    // Тест 2: Поиск, когда объекты есть, но нет подходящих
    @Test
    public void search_WhenNoMatchingObjects_ReturnsEmptyList() {
        // Подготовка
        // Создаём продукт и статью для теста с помощью вспомогательных методов.
        // Настраиваем заглушку. При вызове метода возвращаем список из созданного ранее продукта и статьи
        Product product = createTestProduct("бананы");
        Article article = createTestArticle("Колбаса");
        when(storageService.getAllSearchables())
                .thenReturn(Arrays.asList(product, article));

        // Действие
        // Сохраняем результат поиска запроса по запросу, у которого нет совпадения с созданным списком в коллекцию
        Collection<SearchResult> results = searchService.search("яблоки");

        // Проверка
        // Проверяем, действительно ли коллекция с результатом поиска пустая. Результат поиска отсутствует.
        assertTrue(results.isEmpty());
        // Проверяем что метод был вызван 1 раз
        verify(storageService, times(1)).getAllSearchables();
    }

    // Тест 3: Поиск, когда есть один подходящий продукт
    @Test
    public void search_WhenOneMatchingProduct_ReturnsOneResult() {
        // Подготовка
        Product matchingProduct = createTestProduct("яблоки");
        Product nonMatchingProduct = createTestProduct("бананы");
        when(storageService.getAllSearchables())
                .thenReturn(Arrays.asList(matchingProduct, nonMatchingProduct));

        // Действие
        Collection<SearchResult> results = searchService.search("яблок");

        // Проверка
        // Проверяем что результат получившейся коллекции равен 1
        assertEquals(1, results.size());
        // Извлекаем с помощью итератора элемент коллекции.
        // Присваиваем переменной возвращённый объект типа SearchResult
        SearchResult result = results.iterator().next();
        // Проверяем что имя продукта, найденного в результате поиска, соответствует ожидаемому значению
        assertEquals(matchingProduct.getProductName(), result.getName());
        // Проверяем что метод был вызван 1 раз
        verify(storageService, times(1)).getAllSearchables();
    }

    // Тест 4: Поиск, когда есть несколько подходящих объектов (продукты и статьи)
    @Test
    public void search_WhenMultipleMatchingObjects_ReturnsAllResults() {
        // Подготовка
        Product matchingProduct1 = createTestProduct("яблоки Гольден");
        Product matchingProduct2 = createTestProduct("зелёные яблоки");
        Article matchingArticle = createTestArticle("Статья про яблоки");
        Article nonMatchingArticle = createTestArticle("Статья про яблоНи");
        when(storageService.getAllSearchables())
                .thenReturn(Arrays.asList(matchingProduct1, matchingProduct2, matchingArticle,nonMatchingArticle));

        // Действие
        Collection<SearchResult> results = searchService.search("яблок");

        // Проверка
        assertEquals(3, results.size());
        verify(storageService, times(1)).getAllSearchables();
    }

    // Тест 5: Поиск без учёта регистра
    @Test
    public void search_IsCaseInsensitive_ReturnsResults() {
        // Подготовка
        Product product = createTestProduct("ЯбЛоКи");
        when(storageService.getAllSearchables()).thenReturn(Collections.singletonList(product));

        // Действие
        Collection<SearchResult> results = searchService.search("ябл");

        // Проверка
        assertEquals(1, results.size());
        verify(storageService, times(1)).getAllSearchables();
    }

    // Тест 6: Поиск с пустым запросом
    @Test
    public void search_WithEmptyPattern_ReturnsEmptyList() {
        // Не мокаем storageService, так как метод должен вернуть пустой список без обращения к хранилищу

        // Действие
        Collection<SearchResult> results = searchService.search("");

        // Проверка
        assertTrue(results.isEmpty());
        // Проверяем, что метод storageService не вызывался, так как пустой pattern обрабатывается сразу
        verify(storageService, never()).getAllSearchables();
    }

    // Тест 7: Поиск с null запросом
    @Test
    public void search_WithNullPattern_ReturnsEmptyList() {
        // Действие
        Collection<SearchResult> results = searchService.search(null);

        // Проверка
        assertTrue(results.isEmpty());
        // Проверяем, что метод storageService не вызывался, так как null pattern обрабатывается сразу
        verify(storageService, never()).getAllSearchables();
    }
}