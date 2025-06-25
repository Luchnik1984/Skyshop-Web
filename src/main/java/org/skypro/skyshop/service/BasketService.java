package org.skypro.skyshop.service;

import org.skypro.skyshop.model.basket.*;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasketService {
    private final ProductBasket productBasket;
    private final StorageService storageService;

    public BasketService(ProductBasket productBasket, StorageService storageService) {
        this.productBasket = productBasket;
        this.storageService = storageService;
    }

    public void addToBasket(UUID productId) {
        if (storageService.getProductById(productId).isEmpty()) {
            throw new IllegalArgumentException("Продукт с ID "
                    + productId + " не найден");
        }
        productBasket.addProduct(productId);
    }

    public UserBasket getUserBasket() {
        Map<UUID, Integer> basketContents = productBasket.getProducts();
        List<BasketItem> items = basketContents.entrySet().stream()
                .map(entry -> {
                    Product product = storageService.getProductById(entry.getKey())
                            .orElseThrow(() -> new IllegalArgumentException("Продукт " + entry.getKey() + " не найден в корзине"));
                    return new BasketItem(product, entry.getValue());
                })
                .sorted(Comparator.comparing(
                        item -> item.getProduct().getProductName(),
                        String.CASE_INSENSITIVE_ORDER
                ))
                .collect(Collectors.toList());
        return new UserBasket(items);

    }
}


