package search;

import java.util.List;

public class LinearSearch<T> implements Searcher<T> {

    public boolean search(T target, List<T> list) {
        for (T item : list) {
            if (item.equals(target)) {
                return true;
            }
        }
        return false;
    }
}
