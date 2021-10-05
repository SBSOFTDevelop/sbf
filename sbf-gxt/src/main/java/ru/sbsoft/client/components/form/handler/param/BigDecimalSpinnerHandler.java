/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sbsoft.client.components.form.handler.param;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author sychugin
 */
public class BigDecimalSpinnerHandler extends ru.sbsoft.client.components.form.handler.BigDecimalSpinnerHandler<BigDecimalSpinnerHandler> {

    public BigDecimalSpinnerHandler(NamedItem param) {
        this(param.getCode(), I18n.get(param.getItemName()));
    }

    public BigDecimalSpinnerHandler(String name, String label) {
        super(name, label);
    }

}
