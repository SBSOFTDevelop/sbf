package ru.sbsoft.shared.renderer;

/**
 * Позволяет Enumeration быть источником значеий для рендерера значения в столбще таблицы браузера.
 * @see EnumRenderer
 * @see RendererManager
 */
public interface RenderableEnum<T> {

    T getKey();

    /**
     * @param extended - получить более полный вариант для отображения в комбо боксах
     * @return Строка для отображения в интерфейсе
     */
    String getRenderString(boolean extended);
}
