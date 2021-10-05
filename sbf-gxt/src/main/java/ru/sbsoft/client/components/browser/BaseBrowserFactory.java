package ru.sbsoft.client.components.browser;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.Initializable;
import ru.sbsoft.client.components.browser.actions.GridDeleteAction;
import ru.sbsoft.client.components.browser.actions.GridInsertAction;
import ru.sbsoft.client.components.browser.actions.GridShowAction;
import ru.sbsoft.client.components.browser.actions.GridUpdateAction;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.components.form.IFormFactory;
import ru.sbsoft.client.components.form.IMultiFormFactory;
import ru.sbsoft.client.components.form.IOwnerGridAcceptor;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.GridMode;
import ru.sbsoft.client.components.grid.ChangeFiltersEvent;
import ru.sbsoft.client.components.grid.ContextGrid;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.Modifier;
import ru.sbsoft.shared.consts.BrowserMode;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.interfaces.NamedGridType;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public abstract class BaseBrowserFactory<SelfType extends BaseBrowserFactory> {

    protected final NamedGridType gridType;
    private String context = null;
    private IFormFactory formFactory = null;
    private final Set<Modifier> gridModifiers = new HashSet();
    private final Set<GridMode> browserFlags = EnumSet.of(GridMode.SINGLE_SELECTION);
    private FilterInfo parentFilter;
    private String caption;

    protected BaseBrowserFactory(NamedGridType gridType) {
        this.gridType = gridType;
        this.caption = I18n.get(gridType.getItemName());
    }

    public NamedGridType getGridType() {
        return gridType;
    }

    public String getContext() {
        String typeCode = getGridType().getCode();
        if (context == null || context.isEmpty()) {
            return typeCode;
        }
        if (!context.contains(typeCode)) {
            return new StringBuilder(typeCode.length() + context.length() + 1).append(typeCode).append('_').append(context).toString();
        }
        return context;
    }

    public SelfType setContext(String context) {
        this.context = context != null ? context.trim() : null;
        return (SelfType) this;
    }

    public SelfType setForm(IFormFactory formFactory) {
        this.formFactory = formFactory;
        return (SelfType) this;
    }

    public Modifier[] getGridModifiers() {
        return gridModifiers.toArray(new Modifier[gridModifiers.size()]);
    }

    public SelfType addGridModifiers(Modifier... modifiers) {
        if (modifiers != null && modifiers.length > 0) {
            this.gridModifiers.addAll(Arrays.asList(modifiers));
        }
        return (SelfType) this;
    }

    public GridMode[] getBrowserFlags() {
        return browserFlags.toArray(new GridMode[browserFlags.size()]);
    }

    public SelfType addBrowserFlags(GridMode... browserFlags) {
        if (browserFlags != null && browserFlags.length > 0) {
            this.browserFlags.addAll(Arrays.asList(browserFlags));
        }
        return (SelfType) this;
    }

    public SelfType setParentFilter(FilterInfo parentFilter) {
        this.parentFilter = parentFilter;
        return (SelfType) this;
    }

    public SelfType setMultiselect() {
        return setMultiselect(true);
    }

    public SelfType setMultiselect(boolean b) {
        if (b) {
            browserFlags.remove(GridMode.SINGLE_SELECTION);
        } else {
            browserFlags.add(GridMode.SINGLE_SELECTION);
        }
        return (SelfType) this;
    }

    public String getCaption() {
        return caption;
    }

    public SelfType setCaption(String caption) {
        this.caption = caption;
        return (SelfType) this;
    }

    public final CommonGrid createGrid() {
        CommonGrid res = createGridInstance();
        res.setFormFactory(formFactory);
        prepareGrid(res);
        return res;
    }

    protected CommonGrid createGridInstance() {
        return new CommonGrid();
    }

    protected void prepareGrid(CommonGrid g) {
    }

    public boolean isMode(Modifier... modes) {
        for (Modifier mode : modes) {
            if (gridModifiers.contains(mode)) {
                return true;
            }
        }
        return false;
    }

    protected GridDeleteAction createDeleteAction(CommonGrid g) {
        return null;
    }

    protected GridInsertAction createInsertAction(CommonGrid g) {
        return null;
    }

    protected GridShowAction createShowAction(CommonGrid g) {
        return null;
    }

    protected GridUpdateAction createUpdateAction(CommonGrid g) {
        return null;
    }

    public class CommonGrid extends ContextGrid {

        private IFormFactory formFactory = null;
        private HandlerRegistration formFactoryChangeFiltersReg = null;

        public CommonGrid() {
            super(gridType, getContext());
            if (gridModifiers != null && !gridModifiers.isEmpty()) {
                getGridContext().setModifiers(gridModifiers);
            }
            if (isMode(BrowserMode.HISTORY)) {
                setClonable(true);
            }
            if (BaseBrowserFactory.this.parentFilter != null) {
                setParentFilter(BaseBrowserFactory.this.parentFilter);
            }
        }

        public IFormFactory getFormFactory() {
            return formFactory;
        }

        public void setFormFactory(IFormFactory formFactory) {
            if (this.formFactory != null) {
                freeFormFactory();
            }
            if (formFactory != null) {
                if (formFactory instanceof IOwnerGridAcceptor) {
                    ((IOwnerGridAcceptor) formFactory).setOwnerGrid(this);
                }
                if (formFactory instanceof ChangeFiltersEvent.ChangeFiltersHandler) {
                    formFactoryChangeFiltersReg = addChangeFiltersHandler((ChangeFiltersEvent.ChangeFiltersHandler) formFactory);
                }
                if (formFactory instanceof Initializable) {
                    addInitializable((Initializable) formFactory);
                }

            }
            this.formFactory = formFactory;
        }

        private void freeFormFactory() {
            if (formFactory != null) {
                if (formFactory instanceof IOwnerGridAcceptor) {
                    ((IOwnerGridAcceptor) formFactory).setOwnerGrid(null);
                }
                if (formFactoryChangeFiltersReg != null) {
                    formFactoryChangeFiltersReg.removeHandler();
                }
                if (formFactory instanceof Initializable) {
                    removeInitializable((Initializable) formFactory);
                }
            }
            formFactory = null;
        }

        @Override
        protected void createEditForm(Row model, AsyncCallback<BaseForm> callback) {
            if(formFactory != null){
                formFactory.createEditForm(model, callback);
            }else{
                super.createEditForm(model, callback);
            }
        }

        @Override
        protected FormContext getFormContext(Row model) {
            return formFactory != null ? formFactory.getFormContext(model) : super.getFormContext(model);
        }

        @Override
        public String createFormKeyFromModel(Row selectedModel) {
            if (formFactory != null && (formFactory instanceof IMultiFormFactory)) {
                return ((IMultiFormFactory) formFactory).createFormKeyFromModel(selectedModel);
            } else {
                return super.createFormKeyFromModel(selectedModel);
            }
        }

        @Override
        protected GridDeleteAction createDeleteAction() {
            GridDeleteAction a = BaseBrowserFactory.this.createDeleteAction(this);
            return a != null ? a : super.createDeleteAction();
        }

        @Override
        protected GridInsertAction createInsertAction() {
            GridInsertAction a = BaseBrowserFactory.this.createInsertAction(this);
            return a != null ? a : super.createInsertAction();
        }

        @Override
        protected GridShowAction createShowAction() {
            GridShowAction a = BaseBrowserFactory.this.createShowAction(this);
            return a != null ? a : super.createShowAction();
        }

        @Override
        protected GridUpdateAction createUpdateAction() {
            GridUpdateAction a = BaseBrowserFactory.this.createUpdateAction(this);
            return a != null ? a : super.createUpdateAction();
        }

    }

}
