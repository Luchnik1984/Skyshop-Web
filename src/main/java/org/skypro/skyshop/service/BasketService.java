package org.skypro.skyshop.service;

import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class BasketService {
    private final ProductBasket productBasket;
    private final StorageService storageService;

    public BasketService(ProductBasket productBasket, StorageService storageService) {
        this.productBasket = productBasket;
        this.storageService = storageService;
    }

    public void addToBasket(UUID productId) {
        Product product = storageService.getProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Продукт с ID "
                        + productId + " не найден"));
        productBasket.addProduct(productId);
    }

    public UserBasket getUserBasket() {
        Map<UUID, Integer> basketContents = productBasket.getProducts();
        List<BasketItem> items = basketContents.entrySet().stream()
                .map(entry -> {
                    Product product = storageService.getProductById(entry.getKey())
                            .orElseThrow(() -> new IllegalArgumentException("Продукт не найден в корзине"));
                    return new BasketItem(product, entry.getValue());
                })
                .collect(Collectors.toList());
        return new UserBasket(items);

    }
}


