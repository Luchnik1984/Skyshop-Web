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

    private Product createTestProduct(String name, int costOfProduct) {
        return new SimpleProduct(UUID.randomUUID(), name, costOfProduct);
    }

    @Test
    void addToBasket_WhenProductNotExists_ThrowsException() {

        UUID nonExistentId = UUID.randomUUID();

        when(storageService.getProductOrThrow(nonExistentId))
                .thenThrow(new NoSuchProductException(nonExistentId));

        assertThrows(NoSuchProductException.class, () ->
                basketService.addToBasket(nonExistentId));

        verify(productBasket, never()).addProduct(any());
    }

    @Test
    void addToBasket_WhenProductExists_CallsAddToBasket() {

        UUID productId = UUID.randomUUID();
        Product product = createTestProduct("Яблоки", 100);
        when(storageService.getProductOrThrow(productId)).thenReturn(product);

        basketService.addToBasket(productId);

        verify(productBasket, times(1)).addProduct(productId);
    }

    @Test
    void getUserBasket_WhenBasketEmpty_ReturnsEmptyBasket() {

        when(productBasket.getProducts()).thenReturn(Collections.emptyMap());

        UserBasket result = basketService.getUserBasket();

        assertTrue(result.getItems().isEmpty());
        assertEquals(0, result.getTotal());

        verify(storageService, never()).getProductById(any());
    }

    @Test
    void getUserBasket_WhenBasketHasItems_ReturnsCorrectBasket() {

        Product product1 = createTestProduct("Яблоки", 100);
        Product product2 = createTestProduct("Хлеб", 50);

        Map<UUID, Integer> basketContents = Map.of(
                product1.getId(), 2, // 2 яблока
                product2.getId(), 1   // 1 хлеб
        );

        when(productBasket.getProducts()).thenReturn(basketContents);

        when(storageService.getProductById(any(UUID.class))).thenAnswer(invocation -> {
            UUID productId = invocation.getArgument(0);
            return productId.equals(product1.getId()) ? Optional.of(product1) : Optional.of(product2);
        });

        UserBasket result = basketService.getUserBasket();

        assertEquals(basketContents.size(), result.getItems().size());


        int expectedTotal = basketContents.entrySet().stream()

                .mapToInt(entry -> {

                    Product p = entry.getKey().equals(product1.getId()) ? product1 : product2;

                    return p.getCostOfProduct() * entry.getValue();
                })

                .sum();

        assertEquals(expectedTotal, result.getTotal());

        List<BasketItem> items = new ArrayList<>(result.getItems());

        assertTrue(
                items.get(0).getProduct().getProductName()

                        .compareToIgnoreCase(items.get(1).getProduct().getProductName()) < 0,
                "Товары должны быть отсортированы по алфавиту"
        );
    }

    @Test
    void getUserBasket_WhenProductMissingInStorage_ThrowsException() {
        UUID missingProductId = UUID.randomUUID();

        Map<UUID, Integer> basketContents = Collections.singletonMap(missingProductId, 1);
        when(productBasket.getProducts()).thenReturn(basketContents);
        when(storageService.getProductById(missingProductId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                basketService.getUserBasket());
    }

    @Test
    void getUserBasket_CalculatesTotalCorrectly() {
        Product product = createTestProduct("Молоко", 80);
        int quantity = 5;
        Map<UUID, Integer> basketContents = Collections.singletonMap(product.getId(), quantity);

        when(productBasket.getProducts()).thenReturn(basketContents);
        when(storageService.getProductById(product.getId())).thenReturn(Optional.of(product));

        UserBasket result = basketService.getUserBasket();

        int expectedTotal = product.getCostOfProduct() * quantity;
        assertEquals(expectedTotal, result.getTotal(),
                "Общая стоимость должна быть " + expectedTotal);
    }
}


