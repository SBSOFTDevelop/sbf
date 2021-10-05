package ru.sbsoft.client.components.browser.filter.lookup;

import ru.sbsoft.client.components.form.LookupField;
import ru.sbsoft.shared.LookupFieldConfigBean;

/**
 * @author balandin
 * @since Nov 3, 2015
 */
public interface LookupFieldBuilder {

    public LookupField newInstance(LookupFieldConfigBean config);

}
