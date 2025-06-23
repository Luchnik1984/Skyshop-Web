package org.skypro.skyshop.service;

import org.apache.el.stream.Optional;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.FixPriceProduct;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.Searchable;
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

    private void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    private void addArticle(Article article) {
        articles.put(article.getId(), article);
    }

    private void initializeData() {
        addProduct(new SimpleProduct(UUID.randomUUID(), "яблоки", 150));
        addProduct(new SimpleProduct(UUID.randomUUID(), "бананы", 100));
        addProduct(new SimpleProduct(UUID.randomUUID(), "Хлеб", 70));
        addProduct(new DiscountedProduct(UUID.randomUUID(), "колбаса", 800, 20));
        addProduct(new FixPriceProduct(UUID.randomUUID(), "яйца"));
        addProduct(new FixPriceProduct(UUID.randomUUID(), "молоко"));

        addArticle(new Article(UUID.randomUUID(), "Яблоки сорта Гольден",
                "Жёлтые яблоки с мягким, сладким вкусом и сочной текстурой"));
        addArticle(new Article(UUID.randomUUID(), "Колбаса - Салями Миланская",
                "Салями Миланская - отличный вариант для сервировки мясной нарезки праздничного стола"));
    }

    public Collection<Product> getAllProducts() {
        return Collections.unmodifiableCollection(products.values());
    }

    public Collection<Article> getAllArticles() {
        return Collections.unmodifiableCollection(articles.values());
    }

    public Collection<Searchable> getAllSearchables() {
        Collection<Searchable> result = new ArrayList<>();
        result.addAll(products.values());
        result.addAll(articles.values());
        return result;
    }

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }
}
