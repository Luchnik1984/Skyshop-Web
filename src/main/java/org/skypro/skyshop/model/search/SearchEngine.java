package org.skypro.skyshop.model.search;

import org.skypro.skyshop.model.exeptions.BestResultNotFound;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SearchEngine {
    private final Set<Searchable> searchables = new HashSet<>();


    public void add(Searchable searchable) {
        if (searchable == null) {
            throw new IllegalArgumentException("Нельзя добавить null-объект");
        }
        if (!searchables.add(searchable)) {
            throw new IllegalStateException("Объект '" + searchable.getSearchTerm() + "' уже существует. Добавление дубликата не произведено!");
        }
        searchables.add(searchable);
        System.out.println("Объект '" + searchable.getSearchTerm() + "' добавлен в поисковый движок");

    }


    public Set<Searchable> search(String searchTerm) {
        return searchables.stream()
                .filter(searchable -> searchable != null && searchable.getSearchTerm() != null &&
                        searchable.getSearchTerm().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toCollection(() -> new TreeSet<>(new SearchableComparator())));
    }


    public void printResults(Set<Searchable> results) {
        int count = 0;
        if (results.isEmpty()) {
            count++;
            System.out.println("Результат поиска №" + count + ": Продукт не найден!");
            return;
        }

        for (Searchable result : results) {
            count++;
            System.out.println("Результат поиска №" + count + ": " + result);
        }
    }


    public Searchable findBestSearchResult(String search) throws BestResultNotFound {
        Searchable bestSearchResult = null;
        int maxCount = 0;

        for (Searchable searchable : searchables) {
            if (searchable != null && searchable.getSearchTerm() != null) {
                int count = countOccurrences(searchable.getSearchTerm(), search);

                if (count > maxCount) {
                    maxCount = count;
                    bestSearchResult = searchable;
                }
            }
        }

        if (bestSearchResult == null) {
            throw new BestResultNotFound(search);
        }

        return bestSearchResult;
    }

    private int countOccurrences(String text, String searchTerm) {
        String str = text.toLowerCase();
        String substring = searchTerm.toLowerCase();
        int count = 0;
        int index = 0;

        while ((index = str.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }

        return count;
    }
}

