package com.nd.gaea.odata.uri.parser;

import java.util.List;

public class RawUri {
    public String uri;
    public String scheme;
    public String authority;
    public String path;
    public String queryOptionString;
    public String fragment;
    public List<QueryOption> queryOptionList;
    public List<QueryOption> queryOptionListDecoded;

    public List<String> pathSegmentList;
    public List<String> pathSegmentListDecoded;

    public static class QueryOption {
        public String name;
        public String value;

        QueryOption(final String name, final String value) {
            this.name = name;
            this.value = value;
        }

    }
}