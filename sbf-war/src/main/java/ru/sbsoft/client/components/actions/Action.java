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
     * своим {@link ru.sbsoft.svc.widget.core.client.Component}
     *
     * @see
     * ru.sbsoft.svc.widget.core.client.Component#getData(java.lang.String)
     */
    String LABEL = "action";

    /**
     * @return название действия для отображения в интерфейсе
     */
    String getCaption();

    /**
     * @return подсказка
     */
    String getToolTip();

    /**
     * @return иконка 16x16
     */
    ImageResource getIcon16();

    /**
     * @return иконка 24x24
     */
    ImageResource getIcon24();

    /**
     * @param smallIcon если true возвращается иконка 16x16 иначе - 24x24
     * @return иконка
     */
    ImageResource getIcon(boolean smallIcon);

    /**
     * проверяет и обновляет статусы операции (например, разрешено/запрещено)
     */
    void checkState();

    /**
     * @return true - операция разрешена
     */
    boolean isEnabled();

    /**
     * выполняет операцию
     */
    void perform();

    /**
     * @param handler слушатель. Вызывается, когда состояние разрешено/запрещено
     * изменилось.
     * @return 
     */
    HandlerRegistration addCheckStateHandler(CheckStateHandler handler);
    
    HandlerRegistration addEnabledChangeHandler(EnabledChangeHandler handler);

    HandlerRegistration addTextChangeHandler(TextChangeHandler handler);

    HandlerRegistration addToolTipChangeHandler(ToolTipChangeHandler handler);

    HandlerRegistration addIconChangeHandler(IconChangeHandler handler);
}
