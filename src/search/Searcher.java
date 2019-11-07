package search;

import java.util.List;

public interface Searcher<T> {
    boolean search(T target, List<T> list) throws InterruptedException;
}
