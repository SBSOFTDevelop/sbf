package ru.sbsoft.client.components.form.model;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.data.shared.PropertyAccess;
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
