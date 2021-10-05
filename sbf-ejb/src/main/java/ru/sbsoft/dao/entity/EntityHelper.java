package ru.sbsoft.dao.entity;

import java.util.Date;

/**
 * Вспомогательный класс для сущности, реализующей интерфейсы {@link ru.sbsoft.dao.entity.CreatableEntity} и {@link ru.sbsoft.dao.entity.UpdatableEntity}.
 * <p> устанавливает технологические поля,  используя методы:  
 * <ul>
 * <li>entity.setCreateUser(createUser);</li>
 * <li>entity.setCreateDate(createDate);</li> 
 * <p> На текущий момент в проектах не используется.
 * </ul>
 * @author rfa
 */
public class EntityHelper {

    private final String user;

    public EntityHelper(String user) {
        this.user = user;
    }

    public CreatableEntity setCreateFieldsIn(CreatableEntity entity) {
        return setCreateFieldsIn(entity, user);
    }

    public CreatableEntity setCreateFieldsIn(CreatableEntity entity, Date createDate) {
        return setCreateFieldsIn(entity, user, createDate);
    }

    public static CreatableEntity setCreateFieldsIn(CreatableEntity entity, String createUser) {
        return setCreateFieldsIn(entity, createUser, new Date());
    }

    public static CreatableEntity setCreateFieldsIn(CreatableEntity entity, String createUser, Date createDate) {
        entity.setCreateUser(createUser);
        entity.setCreateDate(createDate);
        return entity;
    }

    public UpdatableEntity setUpdateFieldsIn(UpdatableEntity entity) {
        return setUpdateFieldsIn(entity, user);
    }

    public UpdatableEntity setUpdateFieldsIn(UpdatableEntity entity, Date updateDate) {
        return setUpdateFieldsIn(entity, user, updateDate);
    }

    public static UpdatableEntity setUpdateFieldsIn(UpdatableEntity entity, String updateUser) {
        return setUpdateFieldsIn(entity, updateUser, new Date());
    }

    public static UpdatableEntity setUpdateFieldsIn(UpdatableEntity entity, String updateUser, Date updateDate) {
        entity.setUpdateUser(updateUser);
        entity.setUpdateDate(updateDate);
        return entity;
    }
}
