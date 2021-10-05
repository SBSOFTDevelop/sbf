package ru.sbsoft.client.components;

/**
 * Элемент пользовательского интерфейса.
 *
 * @author Fedor Resnyanskiy
 */
public interface IElement {

    /**
     * Возвращает объект, представляющий компонент используемой реализации.
     *
     *
     * @return
     * @deprecated не рекомендуется использовать кроме как в системных нуждах
     * (внутри IWindow).
     */
    @Deprecated
    public Object getImpl();

    public void setVisible(boolean visible);

    public boolean isVisible();
    
    public void init();
}
