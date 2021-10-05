package ru.sbsoft.system;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Класс предоставляет методы записи/чтения параметров <code>NAME, VALUE</code> в таблицу/(из таблицы) <code>SR_CONFIG</code>.
 * @author balandin
 * @since Mar 21, 2013 12:47:14 PM
 */
public class Parameters implements ISysParameters {

    private final EntityManager entityManager;

    public Parameters(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void set(String name, String value) {
        final Query query = entityManager.createNativeQuery("UPDATE SR_CONFIG SET VALUE = ? WHERE NAME = ?");
        query.setParameter(1, value);
        query.setParameter(2, name);

        final int n = query.executeUpdate();
        switch (n) {
            case 0:
                throw new RuntimeException("Parameter not exist" + name);
            case 1:
                return;
            default:
                throw new RuntimeException("Unconsisten update SR_CONFIG state" + n);
        }
    }

    @Override
    public String get(String name) {
        final Query query = entityManager.createNativeQuery("SELECT VALUE FROM SR_CONFIG WHERE NAME = ?");
        query.setParameter(1, name);
        final List result = query.getResultList();

        final int n = result.size();
        switch (n) {
            case 0:
                return null;
            case 1:
                return (String) result.get(0);
            default:
                throw new RuntimeException("Unconsistent select SR_CONFIG state" + n);
        }
    }

    @Override
    public String get(String name, String defValue) {
        final String value = get(name);
        if (value == null) {
            return defValue;
        }
        return value;
    }

    public void setInt(String name, Integer value) {
        set(name, intToStr(value));
    }

    @Override
    public int getInt(String name) throws ParameterException {
        return Parameters.this.getInt(name, 0);
    }

    @Override
    public int getInt(String name, Integer defValue) throws ParameterException {
        final String value = get(name);
        if (value == null) {
            return defValue;
        }
        return strToInt(value);
    }

    private Integer strToInt(String value) throws ParameterException {
        if (value == null) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new ParameterException("Unexpected integer value " + value);
        }
    }

    private String intToStr(Integer value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }
}
