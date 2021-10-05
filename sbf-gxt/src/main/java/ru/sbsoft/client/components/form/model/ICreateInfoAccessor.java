package ru.sbsoft.client.components.form.model;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
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
