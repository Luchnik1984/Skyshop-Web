package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.FixPriceProduct;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.springframework.stereotype.Service;
import java.util.*;

import java.util.UUID;

@Service
public class StorageService {
  private final Map<UUID, Product> products;
  private final Map<UUID, Article> articles;

    public StorageService() {
        this.products = new HashMap<>();
        this.articles = new HashMap<>();
        initializeData();

    }

    private void initializeData(){
        products.put(UUID.randomUUID(), new SimpleProduct(UUID.randomUUID(),"яблоки", 150));
        products.put(UUID.randomUUID(), new SimpleProduct(UUID.randomUUID(),"бананы", 100));
        products.put(UUID.randomUUID(), new SimpleProduct(UUID.randomUUID(),"Хлеб", 70));
        products.put(UUID.randomUUID(), new DiscountedProduct(UUID.randomUUID(),"колбаса", 800, 20));
        products.put(UUID.randomUUID(), new  FixPriceProduct(UUID.randomUUID(),"яйца"));
        products.put(UUID.randomUUID(), new FixPriceProduct(UUID.randomUUID(),"молоко"));

        articles.put(UUID.randomUUID(), new Article(UUID.randomUUID(),"Яблоки сорта Гольден",
                "Жёлтые яблоки с мягким, сладким вкусом и сочной текстурой"));
        articles.put(UUID.randomUUID(), new Article(UUID.randomUUID(),"Колбаса - Салями Миланская",
                "Салями Миланская - отличный вариант для сервировки мясной нарезки праздничного стола"));
            }

    public Collection<Product> getAllProducts() {
        return Collections.unmodifiableCollection(products.values());
    }

    public Collection<Article> getAllArticles() {
        return Collections.unmodifiableCollection(articles.values());
    }
}
