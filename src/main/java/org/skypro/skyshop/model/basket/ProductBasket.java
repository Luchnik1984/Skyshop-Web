package org.skypro.skyshop.model.basket;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Collections;

@SessionScope
public class ProductBasket {
    private final Map<UUID,Integer> products = new HashMap<>();

    public void addProduct (UUID productId){
        if (productId==null){
            throw new IllegalArgumentException(" укажите корректный ID продукта");
        }
        products.merge(productId,1,Integer::sum);
    }

    public Map<UUID,  Integer> getProducts(){
        return Collections.unmodifiableMap(products);
    }

}
