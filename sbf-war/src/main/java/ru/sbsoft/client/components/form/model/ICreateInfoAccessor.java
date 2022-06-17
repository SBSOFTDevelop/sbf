package ru.sbsoft.client.components.form.model;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.data.shared.PropertyAccess;
import java.util.Date;
import ru.sbsoft.shared.model.ICreateInfoModel;

/**
 *
 * @author vk
 */
public interface ICreateInfoAccessor<M extends ICreateInfoModel> extends PropertyAccess<M> {
    
    ValueProvider<ICreateInfoModel, Date> createDate();
    
    ValueProvider<ICreateInfoModel, String> createUser();
}
