package ru.sbsoft.shared.model.provider;

/**
 * Используются в классах построителя интерфейса, которые в настоящий момент не используются.
 * @author rfa
 */
public interface IFormModelValueProvider<VALUE, MODEL> {

    VALUE getValue(MODEL model);

    void setValue(VALUE value, MODEL model);
}
