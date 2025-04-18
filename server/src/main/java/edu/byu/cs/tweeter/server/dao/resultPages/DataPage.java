package edu.byu.cs.tweeter.server.dao.resultPages;

import java.util.ArrayList;
import java.util.List;

public class DataPage<T> {
    private List<T> values; // page of values returned by the database
    private boolean hasMorePages; // Indicates whether there are more pages of data available to be retrieved

    public DataPage() {
        setValues(new ArrayList<T>());
        setHasMorePages(false);
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public List<T> getValues() {
        return values;
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }
}
