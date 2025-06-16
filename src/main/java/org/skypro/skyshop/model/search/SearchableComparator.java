package org.skypro.skyshop.model.search;

import java.util.Comparator;

public class SearchableComparator implements Comparator<Searchable> {
    @Override
    public int compare(Searchable o1, Searchable o2) {
        if (o1 == null && o2 == null) return 0;
        if (o1 == null) return 1;
        if (o2 == null) return -1;

        String name1 = o1.getProductName();
        String name2 = o2.getProductName();

        if (name1 == null && name2 == null) return 0;
        if (name1 == null) return 1;
        if (name2 == null) return -1;
        int lengthCompare = Integer.compare(name2.length(), name1.length());
        if (lengthCompare != 0) {
            return lengthCompare;
        }
        return name1.compareTo(name2);
    }
}
