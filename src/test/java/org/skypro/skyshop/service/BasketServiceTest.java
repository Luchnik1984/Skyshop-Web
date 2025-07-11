package org.skypro.skyshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.exeptions.NoSuchProductException;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BasketServiceTest {

    @Mock
    private ProductBasket productBasket;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private BasketService basketService;

    // Вспомогательный метод для создания тестового продукта
    private Product createTestProduct(String name, int costOfProduct){
        return new SimpleProduct(UUID.randomUUID(), name, costOfProduct );
    }

    // Тест 1: Добавление несуществующего товара в корзину
    @Test
    void addToBasket_WhenProductNotExists_ThrowsException() {
        // Подготовка
        // создаём случайный UUID, ID представляющий несуществующего товара
        UUID nonExistentId = UUID.randomUUID();
        // Когда метод getProductOrThrow вызывается с nonExistentId,
        // он выбрасывает исключение NoSuchProductException.
        // Это имитирует ситуацию, когда товар с таким ID не найден.
        when(storageService.getProductOrThrow(nonExistentId))
                .thenThrow(new NoSuchProductException(nonExistentId));

        // Действие + Проверка
        // Проверяем что вызов basketService.addToBasket(nonExistentId)
        // действительно приводит к выбросу исключения NoSuchProductException.
        assertThrows(NoSuchProductException.class, () ->
                basketService.addToBasket(nonExistentId));

        // Проверяем, метод productBasket никогда не вызывался; что корзина не модифицировалась
        verify(productBasket, never()).addProduct(any());
    }

    // Тест 2: Добавление существующего товара
    @Test
    void addToBasket_WhenProductExists_CallsAddToBasket() {
        // Подготовка
        UUID productId = UUID.randomUUID();
        Product product = createTestProduct("Яблоки", 100);
        when(storageService.getProductOrThrow(productId)).thenReturn(product);

        // Действие
        basketService.addToBasket(productId);

        // Проверка
        // Проверяем что метод добавления продукта был вызван 1 раз
        verify(productBasket, times(1)).addProduct(productId);
    }

    // Тест 3: Получение пустой корзины
    @Test
    void getUserBasket_WhenBasketEmpty_ReturnsEmptyBasket() {
        // Подготовка
        // Настройка поведения mock-объекта productBasket.
        // Когда вызывается метод getProducts, он должен возвращать пустую мапу.
        when(productBasket.getProducts()).thenReturn(Collections.emptyMap());

        // Действие
        UserBasket result = basketService.getUserBasket();
        //Ожидается, что:
        //Список товаров (items) будет пустым.
        //Общая стоимость (total) будет равна 0.

        // Проверка
        // Проверяет, что список элементов (items) в объекте result (который является корзиной) пуст.
        assertTrue(result.getItems().isEmpty());
        // Проверяет, что общая сумма (total) в объекте result равна 0.
        assertEquals(0, result.getTotal());
        // Проверяет, что метод getProductById объекта storageService никогда не был вызван.
        verify(storageService, never()).getProductById(any());
    }

    // Тест 4: Получение непустой корзины
    @Test
    void getUserBasket_WhenBasketHasItems_ReturnsCorrectBasket() {
        // Подготовка
        Product product1 = createTestProduct("Яблоки", 100); // Продукт 1 (будет вторым при сортировке)
        Product product2 = createTestProduct("Хлеб", 50); // Продукт 2 (будет первым при сортировке)

        // Создаём тестовую корзину: ID товара -> количество
        Map<UUID, Integer> basketContents = Map.of(
                product1.getId(), 2, // 2 яблока
                product2.getId(), 1   // 1 хлеб
        );

        // Настраиваем моки:
        // 1) Корзина возвращает наши тестовые данные
        when(productBasket.getProducts()).thenReturn(basketContents);

        // 2) StorageService возвращает товар по ID (используем thenAnswer для гибкости)
        when(storageService.getProductById(any(UUID.class))).thenAnswer(invocation -> {
            UUID productId = invocation.getArgument(0);
            return productId.equals(product1.getId()) ? Optional.of(product1) : Optional.of(product2);
        });

        // Действие
        UserBasket result = basketService.getUserBasket();

        // Проверка
        // Убеждаемся, что в корзине 2 позиции,
        // несмотря на то, что product1 добавлено 2 штуки.
        assertEquals(basketContents.size(), result.getItems().size());

        // Вычисляем общую стоимость товаров в корзине на основе тестовых данных
        int expectedTotal = basketContents.entrySet().stream()
                // Преобразует каждую пару "ID товара → количество" в стоимость этой позиции (цена × количество).
                .mapToInt(entry -> {
                    // Сравниваем ID из мапы с ID наших тестовых товаров.
                    Product p = entry.getKey().equals(product1.getId()) ? product1 : product2;
                    // Возвращаем стоимость позиции = цена товара* количество
                    return p.getCostOfProduct() * entry.getValue();
                })
                // Суммируем все значения из предыдущего шага
                .sum();
        // Проверяем
        assertEquals(expectedTotal, result.getTotal());

        // Проверяем сортировку по имени
        List<BasketItem> items = new ArrayList<>(result.getItems());
       // Проверка относительного порядка

        assertTrue(
                // Берём первый товар в отсортированном списке
                // Получаем его название
                items.get(0).getProduct().getProductName()
                        // Сравниваем с названием второго товара в списке
                        // Условие < 0 - проверяем что первый стоит раньше второго при сортировке по алфавиту
                        .compareToIgnoreCase(items.get(1).getProduct().getProductName()) < 0,
                "Товары должны быть отсортированы по алфавиту"
        );
    }

    // Дополнительный тест 5: Проверка обработки отсутствующего в хранилище товара
    @Test
    void getUserBasket_WhenProductMissingInStorage_ThrowsException() {
        // Подготовка
        // Генерируем случайный UUID, который заведомо отсутствует в хранилище
        UUID missingProductId = UUID.randomUUID();
        // Формируем корзину с этим товаром. Корзина будет содержать 1 товар с битым ID
        Map<UUID, Integer> basketContents = Collections.singletonMap(missingProductId, 1);
        // Настраиваем моки:
        // productBasket возвращает нашу тестовую корзину.
        // storageService возвращает Optional.empty() (имитируем отсутствие товара).
        when(productBasket.getProducts()).thenReturn(basketContents);
        when(storageService.getProductById(missingProductId)).thenReturn(Optional.empty());

        // Действие + Проверка
        // Проверяем что выбрасывается исключение типа IllegalArgumentException при вызове корзины с битым ID.
        assertThrows(IllegalArgumentException.class, () ->
                basketService.getUserBasket());
    }

    // Дополнительный тест 6: Проверка корректного подсчёта суммы
    @Test
    void getUserBasket_CalculatesTotalCorrectly() {
        // Подготовка
        // Создаём тестовый продукт
        Product product = createTestProduct("Молоко", 80);
        // Задаём переменную с количеством продукта
        int quantity = 5;
        // Формируем корзину (Ключ: ID товара, Значение: количество)
        Map<UUID, Integer> basketContents = Collections.singletonMap(product.getId(), quantity);

        // При вызове productBasket.getProducts() возвращаем нашу тестовую корзину.
        when(productBasket.getProducts()).thenReturn(basketContents);
        // При запросе товара по ID возвращаем наш тестовый объект product
        when(storageService.getProductById(product.getId())).thenReturn(Optional.of(product));

        // Действие
        UserBasket result = basketService.getUserBasket();

        // Проверка
        int expectedTotal = product.getCostOfProduct()*quantity;
        assertEquals(expectedTotal, result.getTotal(),
                "Общая стоимость должна быть " + expectedTotal);
    }
}


