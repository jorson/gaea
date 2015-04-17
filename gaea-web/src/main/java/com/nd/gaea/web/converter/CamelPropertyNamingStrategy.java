package com.nd.gaea.web.converter;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

public class CamelPropertyNamingStrategy extends PropertyNamingStrategy {

    @Override
    public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        return convert(defaultName);
    }

    @Override
    public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
        return convert(defaultName);
    }

    @Override
    public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        return convert(defaultName);
    }

    private String convert(String input) {
        if (isCamel(input)) {
            return c2l(input);
        }
        return l2c(input);
    }

    public static boolean isCamel(String input) {
        boolean hasUnderscores = input.contains("_");
        boolean isLowerCase = input.equals(input.toLowerCase());

        return !(hasUnderscores & isLowerCase);
    }

    public static String l2c(String input) {
        if (input == null)
            return input; // garbage in, garbage out
        int length = input.length();
        String[] tokens = input.split("_");
        StringBuilder result = new StringBuilder(length * 2);
        int resultLength = 0;

        if (tokens.length > 1) {
            result.append(tokens[0]);
            resultLength += tokens[0].length();
            for (int i = 1; i < tokens.length; i++) {

                tokens[i] = tokens[i].replaceFirst(tokens[i].substring(0, 1),
                        tokens[i].substring(0, 1).toUpperCase());
                result.append(tokens[i]);
                resultLength += tokens[i].length();
            }
        }
        return resultLength > 0 ? result.toString() : input;
    }

    public static String c2l(String input) {
        if (input == null)
            return input; // garbage in, garbage out
        int length = input.length();
        StringBuilder result = new StringBuilder(length * 2);
        int resultLength = 0;
        boolean wasPrevTranslated = false;
        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);
            if (i > 0 || c != '_') // skip first starting underscore
            {
                if (Character.isUpperCase(c)) {
                    if (!wasPrevTranslated && resultLength > 0
                            && result.charAt(resultLength - 1) != '_') {
                        result.append('_');
                        resultLength++;
                    }
                    c = Character.toLowerCase(c);
                    wasPrevTranslated = true;
                } else {
                    wasPrevTranslated = false;
                }
                result.append(c);
                resultLength++;
            }
        }
        return resultLength > 0 ? result.toString() : input;
    }
}
