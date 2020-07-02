package com.interview.recommand.refactoring;

import java.util.List;

public class SearchQueryData {

    private String textSearch;

    private String sort;

    private List<SearchQueryTermData> queryTerms;

    public String getTextSearch() {
        return textSearch;
    }

    public void setTextSearch(String textSearch) {
        this.textSearch = textSearch;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<SearchQueryTermData> getQueryTerms() {
        return queryTerms;
    }

    public void setQueryTerms(List<SearchQueryTermData> queryTerms) {
        this.queryTerms = queryTerms;
    }
}
