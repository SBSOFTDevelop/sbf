package ru.sbsoft.client.components.actions;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHTML;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.HasIcon;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import ru.sbsoft.common.Strings;

/**
 * Вспомогательный класс, предоставляющий сервис запуска операций прикрепленных
 * к своим пунктам меню
 *
 * @author balandin
 * @since Oct 3, 2013 12:47:33 PM
 */
public class ActionManager {

    private static final String BUTTON_DEFERRED_ENABLER = "button_deferred_enabler";

    public static enum ButtonSign {
        IconOrText, TextOrIcon, IconAndText
    }

    private final SelectEvent.SelectHandler selectHandler;
    private final SelectionHandler<Item> selectionHandler;
    private final Set<Action> actions = new HashSet<Action>();

    private boolean ctrlKey;
    private boolean altKey;
    private boolean shiftKey;

    public ActionManager() {
        super();

        selectHandler = new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                processSelectEvent(event.getSource());
            }
        };

        selectionHandler = new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                processSelectEvent(event.getSelectedItem());
            }
        };
    }

    public SelectHandler getSelectHandler() {
        return selectHandler;
    }

    public SelectionHandler<Item> getSelectionHandler() {
        return selectionHandler;
    }

    private void processSelectEvent(final Object source) {
        final Action action = ActionUtils.findAction(source);
        if (action != null) {
            action.perform();
            return;
        }
        throw new IllegalStateException();
    }

    public void updateState() {
        final Iterator<Action> iterator = actions.iterator();
        while (iterator.hasNext()) {
            final Action action = iterator.next();
            action.checkState();
        }
    }

    public void registerAction(Action action) {
        actions.add(action);
    }

    public void wasCtrlKey(boolean key) {
        this.ctrlKey = key;
    }

    public boolean wasCtrlKey() {
        boolean v = ctrlKey;
        ctrlKey = false;
        return v;
    }

    public void wasAltKey(boolean key) {
        this.altKey = key;
    }

    public boolean wasAltKey() {
        boolean v = altKey;
        altKey = false;
        return v;
    }

    public void wasShiftKey(boolean key) {
        this.shiftKey = key;
    }

    public boolean wasShiftKey() {
        boolean v = shiftKey;
        shiftKey = false;
        return v;
    }

    public void handleEvent(NativeEvent event) {
        wasCtrlKey(event.getCtrlKey());
        wasAltKey(event.getAltKey());
        wasShiftKey(event.getShiftKey());
    }

    public TextButton createButton(Action action) {
        return createButton(action, ButtonIconSize.large);
    }

    public TextButton createButton(Action action, ButtonIconSize iconSize) {
        return createButton(action, ButtonSign.IconOrText, iconSize);
    }

    public TextButton createButton(Action action, ButtonSign bs, ButtonIconSize iconSize) {
        if (action == null) {
            return null;
        }
        final TextButton button = new TextButton() {

            @Override
            public void onBrowserEvent(Event event) {
                handleEvent(event);
                super.onBrowserEvent(event);
            }
        };
        return bindButtonWithAction(button, action, bs, iconSize);
    }

    public MenuItem createMenuItem(Action action, ButtonSign bs, ButtonIconSize iconSize) {
        if (action == null) {
            return null;
        }
        final MenuItem menuItem = new MenuItem() {
            @Override
            protected void onClick(NativeEvent be) {
                handleEvent(be);
                super.onClick(be);
            }
        };
        return bindButtonWithAction(menuItem, action, bs, iconSize);
    }

    public <T extends Component> T bindButtonWithAction(T button, Action action, ButtonIconSize iconSize) {
        bindButtonWithAction(button, action, ButtonSign.IconOrText, iconSize);
        return (T) button;
    }

    public <T extends Component> T bindButtonWithAction(final T c, final Action a) {
        return bindButtonWithAction(c, a, null, null);
    }

    public <T extends Component> T bindButtonWithAction(final T c, final Action a, final ButtonSign bs, final ButtonIconSize iconSize) {
        registerAction(a);
        c.setTabIndex(-1);
        c.setData(Action.LABEL, a);
        c.setData("action", a);
        if (c instanceof SelectEvent.HasSelectHandlers) {
            ((SelectEvent.HasSelectHandlers) c).addSelectHandler(getSelectHandler());
        } else if (c instanceof HasSelectionHandlers) {
            ((HasSelectionHandlers) c).addSelectionHandler(getSelectionHandler());
        }
        final HasIcon hasIcon = c instanceof HasIcon ? (HasIcon) c : null;
        final HasHTML hasHtml = c instanceof HasHTML ? (HasHTML) c : null;
        c.setToolTip(getToolTip(a));
        if (bs != null) {
            updateButton(hasHtml, hasIcon, a, bs, iconSize);
        }
        a.addEnabledChangeHandler((e) -> {
            ButtonDeferredEnabler enabler = c.getData(BUTTON_DEFERRED_ENABLER);
            if (enabler != null) {
                enabler.setEnabled(a.isEnabled());
            } else {
                enabler = new ButtonDeferredEnabler(c, a.isEnabled());
                enabler.execute();
            }
        });
        c.addEnableHandler((e) -> {
            if(!a.isEnabled()){
                c.setEnabled(false);
            }
        });
        c.addDisableHandler((e) -> {
            if(a.isEnabled()){
                c.setEnabled(true);
            }
        });
        a.addToolTipChangeHandler((e) -> c.setToolTip(e.getValue()));
        if (bs != null) {
            if (hasHtml != null) {
                a.addTextChangeHandler((e) -> updateButton(hasHtml, hasIcon, a, bs, iconSize));
            }
            if (hasIcon != null) {
                a.addIconChangeHandler((e) -> {
                    if (e.getSize() == iconSize) {
                        updateButton(hasHtml, hasIcon, a, bs, iconSize);
                    }
                });
            }
        }
        return (T) c;
    }

    private void updateButton(final HasHTML hasHtml, final HasIcon hasIcon, Action a, final ButtonSign bs, final ButtonIconSize iconSize) {
        switch (bs) {
            case TextOrIcon:
                if (a.getCaption() != null && hasHtml != null) {
                    hasHtml.setText(a.getCaption());
                } else if (hasIcon != null) {
                    hasIcon.setIcon(getIcon(a, iconSize));
                }
                break;
            case IconAndText:
                if (hasIcon != null) {
                    hasIcon.setIcon(getIcon(a, iconSize));
                }
                if (hasHtml != null) {
                    hasHtml.setText(a.getCaption());
                }
                break;
            default:
                ImageResource ico = getIcon(a, iconSize);
                if (ico != null && hasIcon != null) {
                    hasIcon.setIcon(getIcon(a, iconSize));
                } else if (hasHtml != null) {
                    hasHtml.setText(a.getCaption());
                }
        }
    }

    private String getToolTip(Action a) {
        return Strings.coalesce(a.getToolTip(), a.getCaption());
    }

    private ImageResource getIcon(Action a, ButtonIconSize iconSize) {
        return a.getIcon(ButtonIconSize.small == iconSize);
    }

    private static class ButtonDeferredEnabler implements Scheduler.ScheduledCommand {

        private final Component component;
        private boolean enabled;

        public ButtonDeferredEnabler(Component component, boolean enabled) {
            this.component = component;
            this.enabled = enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public void execute() {
            if (component.isVisible(true)) {
                Scheduler.get().scheduleFinally(() -> {
                    component.setData(BUTTON_DEFERRED_ENABLER, null);
                    component.setEnabled(enabled);
                });
            } else {
                component.setData(BUTTON_DEFERRED_ENABLER, this);
                Scheduler.get().scheduleDeferred(this);
            }
        }
    }
}
