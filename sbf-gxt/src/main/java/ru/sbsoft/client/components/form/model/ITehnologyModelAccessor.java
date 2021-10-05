package ru.sbsoft.client.components.form.model;

import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.model.ICreateInfoModel;
import ru.sbsoft.shared.model.IUpdateInfoModel;

/**
 *
 * @author vk
 */
public interface ITehnologyModelAccessor<M extends ICreateInfoModel & IUpdateInfoModel & IFormModel> extends ICreateInfoAccessor<M>, IUpdateInfoAccessor<M>, IFormModelAccessor<M> {
    
}
