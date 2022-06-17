package ru.sbsoft.client.components.browser.filter;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Системная ошибка формирования фильтров
 * HandledFilterException - обрабатываемая ошибка
 *
 * @author balandin
 * @since Jun 23, 2015
 */
public class FilterSetupException extends Exception {

    private final List<Object> params;

    public FilterSetupException(String message, Object... params) {
        super(I18n.get(SBFBrowserStr.msgSystemError) + ": " + message);
        //this.params = new ArrayList<Object>(Arrays.asList(ClientUtils.coalesce(params, Collections.EMPTY_LIST)));
        this.params = new ArrayList<Object>(Collections.singletonList(ClientUtils.coalesce(params, Collections.EMPTY_LIST)));
    }

    public List<Object> getParams() {
        return params;
    }

    public static class Handled extends FilterSetupException {

        public Handled(String message, Object... params) {
            super(message, params);
        }
    }
}
