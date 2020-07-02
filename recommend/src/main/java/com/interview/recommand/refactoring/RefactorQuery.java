package com.interview.recommand.refactoring;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class RefactorQuery {

    private static final String DEFAULT_QUERY_SEPARATOR = ":";

    private static final String DEFAULT_QUERY_DECODING = "utf-8";

    private SearchQueryData decodeQuery(String query) {
        return decodeQuery(query, DEFAULT_QUERY_SEPARATOR, DEFAULT_QUERY_DECODING);
    }

    /**
     * Split query string to generate the decoded object {@link SearchQueryData}.
     *
     * @param query     The pattern is text:sort:key1:value1:key2:value2...
     * @param separator It's used to split query string.
     * @param decoding  It's used to decode query element.
     * @return If the query string does not meet the pattern, return null.
     */
    private SearchQueryData decodeQuery(String query, String separator, String decoding) {
        String[] elements = validateAndGetQueryElements(query, separator, decoding);
        // replace empty object with null, the caller is responsible for prevent NPE
        if (elements == null) {
            return null;
        }

        SearchQueryData searchQueryData = constructSearchQueryData(elements, decoding);
        return searchQueryData;
    }

    private String[] validateAndGetQueryElements(String query, String separator, String decoding) {
        if (StringUtils.isEmpty(query) || StringUtils.isEmpty(separator) || StringUtils.isEmpty(decoding)) {
            // TODO record the context log: query, separator, decoding
            return null;
        }

        String[] elements = query.split(separator);
        boolean lessThanMinimumNumber = elements.length < 4;
        boolean odd = elements.length % 2 != 0;
        if (lessThanMinimumNumber || odd) {
            // TODO record the context log: query, separator, elements.length
            return null;
        }

        for (int i = 0; i < elements.length; i++) {
            if (StringUtils.isEmpty(elements[i])) {
                // TODO record the context log: query, separator, i, elements[i]
                return null;
            }
        }
        return elements;
    }

    private SearchQueryData constructSearchQueryData(String[] elements, String decoding) {
        SearchQueryData searchQueryData = new SearchQueryData();
        searchQueryData.setTextSearch(elements[0]);
        searchQueryData.setSort(elements[1]);
        // specify container size
        int size = (elements.length - 2) / 2;
        List<SearchQueryTermData> searchQueryTermDates = new ArrayList<>(size);
        // replace for loop with while loop to make the logic clear
        int i = 2;
        while (i < elements.length) {
            SearchQueryTermData searchQueryTermData = new SearchQueryTermData();
            searchQueryTermData.setKey(elements[i]);
            try {
                searchQueryTermData.setValue(URLDecoder.decode(elements[i + 1], decoding));
            } catch (UnsupportedEncodingException e) {
                // the exception should be handled
                // TODO record the context log: elements, i, elements[i + 1], decoding, e
            }
            searchQueryTermDates.add(searchQueryTermData);
            i = i + 2;
        }
        searchQueryData.setQueryTerms(searchQueryTermDates);
        return searchQueryData;
    }

}
