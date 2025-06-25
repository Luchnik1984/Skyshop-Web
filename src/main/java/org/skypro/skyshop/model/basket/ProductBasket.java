package org.skypro.skyshop.model.basket;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Collections;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProductBasket {
    private final Map<UUID, Integer> products = new HashMap<>();

    public void addProduct(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException(" укажите корректный ID продукта");
        }
        products.merge(productId, 1, Integer::sum);
    }

    public Map<UUID, Integer> getProducts() {
        return Collections.unmodifiableMap(products);
    }

}
