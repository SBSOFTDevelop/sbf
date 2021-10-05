package ru.sbsoft.client.components.actions;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.client.components.actions.event.CheckStateHandler;
import ru.sbsoft.client.components.actions.event.EnabledChangeHandler;
import ru.sbsoft.client.components.actions.event.IconChangeHandler;
import ru.sbsoft.client.components.actions.event.TextChangeHandler;
import ru.sbsoft.client.components.actions.event.ToolTipChangeHandler;

/**
 * Событие, возникающее в результате действий пользователя. Обычно, представляет
 * собой нажатие кнопок мыши на элементах пользовательского интерфейса.
 *
 * @author balandin
 */
public interface Action {

    /**
     * Для стандартного доступа к {@code Action} объекту сохраненному вместе со
     * своим {@link com.sencha.gxt.widget.core.client.Component}
     *
     * @see
     * com.sencha.gxt.widget.core.client.Component#getData(java.lang.String)
     */
    public static String LABEL = "action";

    /**
     * @return название действия для отображения в интерфейсе
     */
    public String getCaption();

    /**
     * @return подсказка
     */
    public String getToolTip();

    /**
     * @return иконка 16x16
     */
    public ImageResource getIcon16();

    /**
     * @return иконка 24x24
     */
    public ImageResource getIcon24();

    /**
     * @param smallIcon если true возвращается иконка 16x16 иначе - 24x24
     * @return иконка
     */
    public ImageResource getIcon(boolean smallIcon);

    /**
     * проверяет и обновляет статусы операции (например, разрешено/запрещено)
     */
    public void checkState();

    /**
     * @return true - операция разрешена
     */
    public boolean isEnabled();

    /**
     * выполняет операцию
     */
    public void perform();

    /**
     * @param handler слушатель. Вызывается, когда состояние разрешено/запрещено
     * изменилось.
     * @return 
     */
    public HandlerRegistration addCheckStateHandler(CheckStateHandler handler);
    
    public HandlerRegistration addEnabledChangeHandler(EnabledChangeHandler handler);

    public HandlerRegistration addTextChangeHandler(TextChangeHandler handler);

    public HandlerRegistration addToolTipChangeHandler(ToolTipChangeHandler handler);

    public HandlerRegistration addIconChangeHandler(IconChangeHandler handler);
}
