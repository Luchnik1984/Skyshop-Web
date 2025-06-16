package org.skypro.skyshop.model.search;

import java.util.UUID;

public interface Searchable {
    String getSearchTerm();

    String getContentType();

    String getProductName();

    default String getStringRepresentation() {
        return getProductName() + " - " + getContentType();
    }

    UUID getId();
}
