package com.nd.gaea.odata.uri.parser;

import com.nd.gaea.odata.utility.Decoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriDecoder {

    static Pattern uriPattern = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");

    public static RawUri decodeUri(final String uri, final int scipSegments) {
        RawUri rawUri = new RawUri();

        Matcher m = uriPattern.matcher(uri);
        if (m.matches()) {
            rawUri.scheme = m.group(2);
            rawUri.authority = m.group(4);
            rawUri.path = m.group(5);
            rawUri.queryOptionString = m.group(7);
            rawUri.fragment = m.group(9);
        }

        splittPath(rawUri, scipSegments);
        splittOptions(rawUri);
        decode(rawUri);

        return rawUri;
    }

    private static void decode(final RawUri rawUri) {
        rawUri.pathSegmentListDecoded = new ArrayList<String>();
        for (String segment : rawUri.pathSegmentList) {
            rawUri.pathSegmentListDecoded.add(decode(segment));
        }

        rawUri.queryOptionListDecoded = new ArrayList<RawUri.QueryOption>();
        for (RawUri.QueryOption optionPair : rawUri.queryOptionList) {
            rawUri.queryOptionListDecoded.add(new RawUri.QueryOption(
                    decode(optionPair.name),
                    decode(optionPair.value)));
        }
    }

    private static void splittOptions(final RawUri rawUri) {
        rawUri.queryOptionList = new ArrayList<RawUri.QueryOption>();

        if (rawUri.queryOptionString == null) {
            return;
        }

        List<String> options = splitt(rawUri.queryOptionString, '&');

        for (String option : options) {
            if (option.length() != 0) {
                List<String> pair = splittFirst(option, '=');
                rawUri.queryOptionList.add(
                        new RawUri.QueryOption(pair.get(0), pair.get(1)));
            }
        }
    }

    private static List<String> splittFirst(final String input, final char c) {
        int pos = input.indexOf(c, 0);
        if (pos >= 0) {
            return Arrays.asList(input.substring(0, pos), input.substring(pos + 1));
        } else {
            return Arrays.asList(input, "");
        }
    }

    public static void splittPath(final RawUri rawUri, int scipSegments) {
        List<String> list = splitt(rawUri.path, '/');

        if (list.get(0).length() == 0) {
            scipSegments++;
        }

        if (scipSegments > 0) {
            rawUri.pathSegmentList = list.subList(scipSegments, list.size());
        } else {
            rawUri.pathSegmentList = list;
        }
    }

    public static List<String> splitt(final String input, final char c) {

        List<String> list = new LinkedList<String>();

        int start = 0;
        int end = -1;

        while ((end = input.indexOf(c, start)) >= 0) {
            list.add(input.substring(start, end));
            start = end + 1;
        }

        list.add(input.substring(start));

        return list;
    }

    public static String decode(final String encoded) {
        return Decoder.decode(encoded);
    }

}
