package ru.sbsoft.shared.model.provider;

/**
 * Используются в классах построителя интерфейса, которые в настоящий момент не используются.
 * @author rfa
 */
public interface IFormModelValueProvider<VALUE, MODEL> {

    public abstract VALUE getValue(MODEL model);

    public abstract void setValue(VALUE value, MODEL model);
}
