package ru.sbsoft.client.components.actions;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.event.CheckStateEvent;
import ru.sbsoft.client.components.actions.event.CheckStateHandler;
import ru.sbsoft.client.components.actions.event.EnabledChangeEvent;
import ru.sbsoft.client.components.actions.event.EnabledChangeHandler;
import ru.sbsoft.client.components.actions.event.IconChangeEvent;
import ru.sbsoft.client.components.actions.event.IconChangeHandler;
import ru.sbsoft.client.components.actions.event.TextChangeEvent;
import ru.sbsoft.client.components.actions.event.TextChangeHandler;
import ru.sbsoft.client.components.actions.event.ToolTipChangeEvent;
import ru.sbsoft.client.components.actions.event.ToolTipChangeHandler;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;

/**
 * Пользовательское действие для меню формы или таблицы.
 *
 * @author balandin
 */
public abstract class AbstractAction implements Action {

    private boolean enabled = true;
    //
    private String caption;
    private String toolTip;
    private ImageResource icon16;
    private ImageResource icon24;
    //
    private final HandlerManager handlerManager = new HandlerManager(this);
    
    private List<Supplier<Boolean>> enableChecks = null;

    public AbstractAction() {
        super();
    }

    public AbstractAction(ILocalizedString caption, ImageResource icon16, ImageResource icon24) {
        this(caption, caption, icon16, icon24);
    }

    public AbstractAction(ILocalizedString caption, ILocalizedString toolTip, ImageResource icon16, ImageResource icon24) {
        this(I18n.get(caption), I18n.get(toolTip), icon16, icon24);    }
    
    public AbstractAction(I18nResourceInfo caption, ImageResource icon16, ImageResource icon24) {
        this(caption, caption, icon16, icon24);
    }

    public AbstractAction(I18nResourceInfo caption, I18nResourceInfo toolTip, ImageResource icon16, ImageResource icon24) {
        this(I18n.get(caption), I18n.get(toolTip), icon16, icon24);
    }

    public AbstractAction(String caption, String toolTip, ImageResource icon16, ImageResource icon24) {
        this.caption = caption;
        this.toolTip = toolTip;
        this.icon16 = icon16;
        this.icon24 = icon24;
    }
    
    public void addEnableCheck(Supplier<Boolean> check){
        if(enableChecks == null){
            enableChecks = new ArrayList<>();
        }
        enableChecks.add(check);
    }
    
    private boolean isChecksOk(){
        boolean res = true;
        if(enableChecks != null){
            for(Supplier<Boolean> check : enableChecks){
                if(!check.get()){
                    res = false;
                    break;
                }
            }
        }
        return res;
    }

    @Override
    public void checkState() {
        final boolean newEnabledState = checkEnabled() && isChecksOk();
        if (enabled != newEnabledState) {
            enabled = newEnabledState;
            fireEnabledChange();
        }
        fireCheckState();
    }
    
    protected void forceEnabledChange(){
        handlerManager.fireEvent(new EnabledChangeEvent(enabled));
    }

    private void fireCheckState() {
        handlerManager.fireEvent(new CheckStateEvent());
    }

    private void fireEnabledChange() {
        handlerManager.fireEvent(new EnabledChangeEvent(enabled));
    }

    private void fireTextChange() {
        handlerManager.fireEvent(new TextChangeEvent(caption));
    }

    private void fireToolTipChange() {
        handlerManager.fireEvent(new ToolTipChangeEvent(toolTip));
    }

    private void fireIconChange(ButtonIconSize size) {
        final ImageResource ico;
        switch (size) {
            case large:
                ico = icon24;
                break;
            case small:
                ico = icon16;
                break;
            default:
                ico = null;
        }
        if (ico != null) {
            handlerManager.fireEvent(new IconChangeEvent(ico, size));
        }
    }

    @Override
    public HandlerRegistration addCheckStateHandler(CheckStateHandler handler) {
        return handlerManager.addHandler(CheckStateEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addEnabledChangeHandler(EnabledChangeHandler handler) {
        return handlerManager.addHandler(EnabledChangeEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addTextChangeHandler(TextChangeHandler handler) {
        return handlerManager.addHandler(TextChangeEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addToolTipChangeHandler(ToolTipChangeHandler handler) {
        return handlerManager.addHandler(ToolTipChangeEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addIconChangeHandler(IconChangeHandler handler) {
        return handlerManager.addHandler(IconChangeEvent.TYPE, handler);
    }

    public boolean checkEnabled() {
        return true;
    }

    @Override
    public void perform() {
        if (enabled) {
            onExecute();
        }
    }

    protected abstract void onExecute();

    @Override
    public String getCaption() {
        return caption;
    }

    public final AbstractAction setCaption(ILocalizedString locStr) {
        return setCaption(I18n.get(locStr));
    }
    
    public final AbstractAction setCaption(I18nResourceInfo rinf) {
        return setCaption(I18n.get(rinf));
    }

    public final AbstractAction setCaption(String caption) {
        this.caption = caption;
        fireTextChange();
        return this;
    }

    @Override
    public String getToolTip() {
        return toolTip;
    }

    public final AbstractAction setToolTip(ILocalizedString locStr) {
        return setToolTip(I18n.get(locStr));
    }

    public final AbstractAction setToolTip(I18nResourceInfo rinf) {
        return setToolTip(I18n.get(rinf));
    }

    public final AbstractAction setToolTip(String toolTip) {
        this.toolTip = toolTip;
        fireToolTipChange();
        return this;
    }

    @Override
    public ImageResource getIcon16() {
        return icon16;
    }

    public final AbstractAction setIcon16(ImageResource icon16) {
        this.icon16 = icon16;
        fireIconChange(ButtonIconSize.small);
        return this;
    }

    @Override
    public ImageResource getIcon24() {
        return icon24;
    }

    public final AbstractAction setIcon24(ImageResource icon24) {
        this.icon24 = icon24;
        fireIconChange(ButtonIconSize.large);
        return this;
    }

    @Override
    public ImageResource getIcon(boolean smallIcons) {
        return smallIcons ? icon16 : icon24;
    }

    @Override
    public final boolean isEnabled() {
        return enabled;
    }
}
