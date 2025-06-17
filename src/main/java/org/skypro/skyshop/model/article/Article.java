package org.skypro.skyshop.model.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.skypro.skyshop.model.search.Searchable;
import java.util.Objects;
import java.util.UUID;

public class Article implements Searchable {

    private final String articleTitle;
    private final String articleContent;
    private final UUID id;


    public Article(UUID id, String articleTitle, String articleContent) {
        this.id = id;
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @JsonIgnore
    @Override
    public String getContentType() {
        return "ARTICLE";
    }

    @JsonIgnore
    @Override
    public String getSearchTerm() {
        return this.toString();
    }

    @Override
    public String getProductName() {
        return articleTitle;
    }

    @Override
    public String toString() {
        return
                "< " + articleTitle + " : " +
                        articleContent + " > ";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    }

