package org.skypro.skyshop.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.lang.NonNull;

import java.util.Objects;
import java.util.UUID;

public abstract class Product implements Searchable, Comparable<Product> {

    private final String productName;
    private final UUID id;


    public Product(UUID id, String productName) {
        this.id = id;
        this.productName = productName;
        if (productName == null) {
            throw new IllegalArgumentException("Продукт без названия или продукт отсутствует!");
        }
        if (productName.isBlank()) {
            throw new IllegalArgumentException("Название продукта не может быть пустой строкой!");
        }
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    public String getProductName() {
        return productName;
    }

    public abstract int getCostOfProduct();

    public abstract boolean isSpecial();

    @JsonIgnore
    @Override
    public String getSearchTerm() {
        return productName;
    }

    @JsonIgnore
    @Override
    public String getContentType() {
        return " PRODUCT ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public int compareTo(Product other) {
        if (other == null) return -1;
        if (other.productName == null) return -1;
        int lengthCompare = Integer.compare(other.productName.length(), this.productName.length());
        if (lengthCompare != 0) {
            return lengthCompare;
        }
        return this.productName.compareTo(other.productName);
    }
}
