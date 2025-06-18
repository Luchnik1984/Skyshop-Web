package org.skypro.skyshop.model.search;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public interface Searchable {
    @JsonIgnore
    String getSearchTerm();

    @JsonIgnore
    String getContentType();

    String getProductName();

    default String getStringRepresentation() {
        return getProductName() + " - " + getContentType();
    }

    UUID getId();
}
