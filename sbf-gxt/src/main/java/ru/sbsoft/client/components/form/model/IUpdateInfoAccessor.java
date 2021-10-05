package ru.sbsoft.client.components.form.model;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import java.util.Date;
import ru.sbsoft.shared.model.IUpdateInfoModel;

/**
 *
 * @author vk
 */
public interface IUpdateInfoAccessor<M extends IUpdateInfoModel> extends PropertyAccess<M> {
    
    ValueProvider<IUpdateInfoModel, Date> updateDate();
    
    ValueProvider<IUpdateInfoModel, String> updateUser();
}
