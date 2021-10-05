package ru.sbsoft.client.components.form.handler;

import ru.sbsoft.shared.model.LookupInfoModel;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class CommonLookupHandler<SelfType extends CommonLookupHandler<SelfType>> extends BaseLookupHandler<LookupInfoModel, SelfType> {

    public CommonLookupHandler(String name, String label) {
        super(name, label);
    }
}
