package ru.sbsoft.shared;

import java.io.Serializable;
import ru.sbsoft.shared.param.DTO;

/**
 * Класс представляет объект параметра операции.
 * @see ru.sbsoft.dao.AbstractMultiOperationManager#executeOperation(Long operationId)
 * @author rfa
 */
public class OperationObject implements Serializable {

    private String name;
    private DTO value;

    public OperationObject() {
    }

    public OperationObject(String name, DTO dto) {
        this.name = name;
        this.value = dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DTO getDTO() {
        return value;
    }

    public void setDTO(DTO value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "OperationObject{" + "name=" + name + ", value=" + value.toString() + '}';
    }

}
