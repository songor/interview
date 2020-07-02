package com.interview.recommand.refactoring;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class Query {

    private SearchQueryData decodeQuery(String query) {
        SearchQueryData queryData = new SearchQueryData();
        if (null != query) {
            String[] split = query.split(":");
            if (split.length > 0) {
                queryData.setTextSearch(split[0]);
            }
            if (split.length > 1) {
                queryData.setSort(split[1]);
            }
            List<SearchQueryTermData> terms = new ArrayList<>();
            for (int i = 2; (i + 1) < split.length; i += 2) {
                SearchQueryTermData termData = new SearchQueryTermData();
                termData.setKey(split[i]);
                try {
                    termData.setValue(URLDecoder.decode(split[i + 1], "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    // the exception should be handled
                }
                terms.add(termData);
            }
            queryData.setQueryTerms(terms);
        }
        return queryData;
    }

}
