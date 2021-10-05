package ru.sbsoft.common;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.sbsoft.common.jdbc.QueryParam;
import ru.sbsoft.common.jdbc.QueryParamImpl;

/**
 * Обрабатывает строку, заменяя все вхождения параметров (:parameter) на "?", сообщая об этом
 * слушателю. Используется парсерами SQL,которые регистрируют параметры SQL у себя. их на "?".
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class SQLParameterProcessor {

    private static Pattern parameterPattern = Pattern.compile("(:(?<!::)([a-zA-Z0-9_]+))");

    public static CharSequence process(final String input, final ParameterListener listener) {
        final Matcher parameterMatcher = parameterPattern.matcher(input);

        StringBuilder result = new StringBuilder();
        int cursor = 0;
        while (parameterMatcher.find()) {
            //Начало и окончание всего, что связано с параметром в запросе
            final int start = parameterMatcher.start(1);
            final int end = parameterMatcher.end(1);
            //Начало и окончание самого параметра
            final int key_start = parameterMatcher.start(2);
            final int key_end = end;

            final String key = input.substring(key_start, key_end);

            final Object value = listener.getParameterValue(key);
            final String placeholders;
            if (value instanceof List) {
                final List values = (List) value;
                placeholders = Strings.replicate("?", values.size(), ", ");

                for (Object elementValue : values) {
                    listener.onListElement(new QueryParamImpl(elementValue));
                }
            } else {
                placeholders = "?";
                listener.onElement(key);
            }

            result.append(input.substring(cursor, start)).append(placeholders);
            cursor = end;
        }
        result.append(input.substring(cursor));

        return result;
    }

    public static interface ParameterListener {

        public Object getParameterValue(String key);

        public void onListElement(QueryParam param);

        public void onElement(String key);

    }
}
