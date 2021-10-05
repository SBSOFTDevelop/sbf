package ru.sbsoft.dao.entity;

import javax.ejb.SessionContext;

/**
 *
 * @author vk
 */
public interface IBaseEntity {

    /**
     * @return уникальный идентификатор сущности
     */
    Object getIdValue();

    /**
     * Метод для заполнения технологических полей сущности
     *
     * @param scontext сессионный контекст
     */
    default void checkTehnologyFields(final SessionContext scontext) {
        if (this instanceof ICreateInfoEntity) {
            CreateInfoFields f = ((ICreateInfoEntity) this).getCreateInfoFields();
            if (f == null) {
                f = new CreateInfoFields();
            }
            if(getIdValue() == null){
                f.setCreateUser(null);
                f.setCreateDate(null);
            }
            f.fillFrom(scontext);
            ((ICreateInfoEntity) this).setCreateInfoFields(f);
        }
        if (this instanceof IUpdateInfoEntity) {
            UpdateInfoFields f = ((IUpdateInfoEntity) this).getUpdateInfoFields();
            if (f == null) {
                f = new UpdateInfoFields();
            }
            f.fillFrom(scontext);
            ((IUpdateInfoEntity) this).setUpdateInfoFields(f);
        }
    }
    
    default void checkTehnologyFields(final String runUser) {
        if (this instanceof ICreateInfoEntity) {
            CreateInfoFields f = ((ICreateInfoEntity) this).getCreateInfoFields();
            if (f == null) {
                f = new CreateInfoFields();
            }
            if(getIdValue() == null){
                f.setCreateUser(null);
                f.setCreateDate(null);
            }
            f.fillFrom(runUser);
            ((ICreateInfoEntity) this).setCreateInfoFields(f);
        }
        if (this instanceof IUpdateInfoEntity) {
            UpdateInfoFields f = ((IUpdateInfoEntity) this).getUpdateInfoFields();
            if (f == null) {
                f = new UpdateInfoFields();
            }
            f.fillFrom(runUser);
            ((IUpdateInfoEntity) this).setUpdateInfoFields(f);
        }
    }
    
}
