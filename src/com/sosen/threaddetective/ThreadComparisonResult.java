package com.sosen.threaddetective;

import java.util.ArrayList;
import java.util.List;

public class ThreadComparisonResult {
    private List<ThreadComparisonResultItem> resultItems = new ArrayList<>();

    public List<ThreadComparisonResultItem> getResultItems() {
        return resultItems;
    }

    public void addResultItems(ThreadComparisonResultItem resultItem) {
        this.resultItems.add(resultItem);
    }

}
