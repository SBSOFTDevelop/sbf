package ru.sbsoft.client;

import ru.sbsoft.client.schedule.MaskCommand;
import ru.sbsoft.client.schedule.UnmaskCommand;
import ru.sbsoft.client.schedule.MaskI18nCommand;
import ru.sbsoft.client.schedule.SyncSchedulerChainCommand;
import ru.sbsoft.client.schedule.SchedulerChainManager;
import ru.sbsoft.client.schedule.SchedulerChainCommand;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.container.Viewport;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.appmenu.AppStruct;
import ru.sbsoft.client.components.browser.AboutPanel;
import ru.sbsoft.client.components.browser.BaseBrowserManager;
import ru.sbsoft.client.components.exception.BrowserException;
import ru.sbsoft.client.components.operation.BaseOperationManager;
import ru.sbsoft.client.components.treemenu.ApplicationContainer;
import ru.sbsoft.client.components.treemenu.ApplicationPanel;
import ru.sbsoft.client.components.treemenu.IMenuExecutor;
import ru.sbsoft.client.components.treemenu.MainMenuTree;
import ru.sbsoft.client.consts.SBFConfig;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFVariable;
import ru.sbsoft.client.schedule.i18n.SBFMessages;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.client.utils.RoleCheker;
import ru.sbsoft.shared.ApplicationInfo;
import ru.sbsoft.shared.model.ApplicationMenuTreeModel;
import ru.sbsoft.shared.renderer.RendererManager;
import ru.sbsoft.client.schedule.i18n.i18nCommand;

import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.consts.I18nSection;
import ru.sbsoft.shared.consts.I18nType;
import ru.sbsoft.shared.model.MenuItemModel;

import ru.sbsoft.client.utils.GetUserEnvCommand;

/**
 * Базовый класс для "точки входа" gwt приложения. Каждое приложение(модуль)
 * содержит свою собственную "точку входа" унаследованную от этого класса.
 * Обеспечивает общее поведение, сервисные функции.
 *
 * @author balandin
 * @since Apr 18, 2013 12:02:47 PM
 */
public abstract class SBFEntryPoint implements IsWidget, EntryPoint {

    private Viewport currentViewport = new Viewport();
    protected ApplicationPanel applicationPanel;
    protected ApplicationContainer applicationContainer;
    protected MainMenuTree mainMenu;
    //
    protected BaseBrowserManager browserManager;
    protected BaseOperationManager operationManager;
    protected IMenuExecutor menuItemExecution;
    protected String contextRootPath = ".";
    //
    protected int menuAlign = ApplicationContainer.ALIGN_RIGHT;
    protected int menuWidth = 210;
    //
    protected static RendererManager rendererManager;

    public SBFEntryPoint() {
        final BaseBrowserManager bm = new BaseBrowserManager() {
            @Override
            protected void registrBrowsers() throws BrowserException {
            }
        };
        final BaseOperationManager om = new BaseOperationManager() {
            @Override
            protected void registrOperations() throws BrowserException {
            }
        };
        this.browserManager = bm;
        this.operationManager = om;
        this.menuItemExecution = new IMenuExecutor() {
            @Override
            public void executeItem(String appCode, MenuItemModel item) throws BrowserException {
                switch (item.getMenuType()) {
                    case Browser:
                        browserManager.openBrowser(appCode, item);
                        break;
                    case Report:
                    case Operation:
                        om.openOperation(item);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onModuleLoad() {
        // устанавливаем до построения интерфейса пустой Viewport для привязки маски
        RootPanel.get().clear();
        RootPanel.get().add(currentViewport);

        SchedulerChainManager scheduler = new SchedulerChainManager(getCommandList());
        scheduler.next();

    }

    protected List<SchedulerChainCommand> getCommandList() {
        List<SchedulerChainCommand> commandList = new ArrayList<SchedulerChainCommand>();

        commandList.add(new MaskCommand(currentViewport, "loading local resources..."));
        commandList.add(new i18nCommand(getFilesI18n()));
//проверка нужно ли менять пароль
        commandList.add(new GetUserEnvCommand(currentViewport));

        commandList.add(new LoadXMessagesCommand());
        commandList.add(new MaskI18nCommand(currentViewport, SBFGeneralStr.msgSetExceptionHandler));
        commandList.add(new ExceptionHandlerCommand());
        commandList.add(new MaskI18nCommand(currentViewport, SBFGeneralStr.msgBuildingGUI));
        commandList.add(new BuildGUICommand(this));
        commandList.add(new UnmaskCommand(currentViewport));
        commandList.add(new SchedulerChainCommand() {
            @Override
            public void execute() {
                init();
            }
        });
        return commandList;
    }

    /**
     * Определяется список строковых ресурсов локализации
     *
     * @return Возвращает массив I18nSection информации о ресурсах локализации
     */
    protected I18nSection[] getFilesI18n() {
        return I18nType.values();
    }

    @Override
    public Widget asWidget() {
        currentViewport = new SBFViewport(this);
        return currentViewport;
    }

    private void init() {

        try {
            beforeInit();
            browserManager.initManager(getApplicationPanel());
            // TODO убрать
            if (operationManager != null) {
                operationManager.initManager();
            }
            refreshAppTitle();
            initApplicationsList();
            onInit();

//To Do объединить все одноразовые стартовые сервисы в один!!!!
            RoleCheker userEnv = RoleCheker.getInstance();
            getApplicationPanel().setUser(userEnv.getUserName());

            setApplicationInfo(userEnv.getApplicationInfo());
        } catch (BrowserException error) {
            GWT.log(SBFEntryPoint.class.getName(), error);
            final HTML errorControl = new HTML(error.getClass().getName() + "<br>" + error.getMessage());
            currentViewport.setWidget(errorControl);
        }

    }

    protected void setApplicationInfo(ApplicationInfo result) {
        if (result == null) {
            result = new ApplicationInfo();
            result.setVersion(SBFConfig.readString(SBFVariable.APPLICATION_ABOUT));
            //result.setTimestamp("n/a");
            result.setRevision("n/a");

        }
        if (SBFConfig.readString(SBFVariable.APPLICATION_TITLE) != null) {
            result.setApplication(SBFConfig.readString(SBFVariable.APPLICATION_TITLE));
        }
        browserManager.setApplicationInfo(result, contextRootPath);
    }

    public BaseBrowserManager getBrowserManager() {
        return browserManager;
    }

    protected MainMenuTree getMainMenu() {
        if (mainMenu == null) {
            mainMenu = new MainMenuTree(browserManager, menuItemExecution, null);
        }
        return mainMenu;
    }

    public ApplicationPanel getApplicationPanel() {
        if (applicationPanel == null) {
            applicationContainer = new ApplicationContainer(browserManager, getMainMenu(), menuAlign, menuWidth);
            applicationPanel = new ApplicationPanel(applicationContainer, SBFConfig.readString(SBFVariable.APPLICATION_TITLE));
            applicationPanel.setWidget(applicationContainer);
            applicationPanel.setTabIndex(-1);
        }
        return applicationPanel;
    }

    public static RendererManager getRendererManager() {
        return rendererManager;
    }

    private void creatNewMenu(AppStruct appStruct) {
        Scheduler.get().scheduleDeferred(new NewMenuCreateStarter(appStruct));
    }

    private class NewMenuCreateStarter implements Scheduler.ScheduledCommand {

        private final AppStruct appStruct;

        public NewMenuCreateStarter(AppStruct appStruct) {
            this.appStruct = appStruct;
        }

        @Override
        public void execute() {
            if (RoleCheker.getInstance().isInint()) {
                getMainMenu().init(appStruct.getApplicationList(getAppNames()), appStruct);
            } else {
                creatNewMenu(appStruct);
            }
        }
    }

    protected void fillAppMenu(AppStruct menu) {
    }

    private void refreshAppTitle() {
        if (null == applicationPanel.getCaption()) {
            applicationPanel.setCaption(SBFConfig.readString(SBFVariable.APPLICATION_TITLE));
        }
    }

    private void initApplicationsList() {
        AppStruct appStruct = new AppStruct();
        fillAppMenu(appStruct);
        if (!appStruct.isEmpty()) {
            creatNewMenu(appStruct);
        } else {
            SBFConst.DB_STRUCT_SERVICE.getApplicationList(getAppNames(), new DefaultAsyncCallback<List<ApplicationMenuTreeModel>>() {
                @Override
                public void onResult(List<ApplicationMenuTreeModel> result) {
                    getMainMenu().init(result);
                }
            });
        }
    }

    protected List<String> getAppNames() {
        return null;
    }

    protected void beforeInit() {
    }

    protected void onInit() {
    }

    protected void initAboutWindow(final String url) {
        try {
            final AboutPanel.Maker maker = AboutPanel.createMakerInstance(browserManager);
            maker.setUrl(url);
            getMainMenu().getToolBar().insert(maker.getButton(), 2);
            getMainMenu().getToolBar().forceLayout();
        } catch (BrowserException ex) {
            ClientUtils.showError(ex.getMessage());
        }

//        SBFConst.SERVICE_SERVICE.getCurrentUserName(new DefaultAsyncCallback<String>() {
//
//            @Override
//            public void onResult(String result) {
//                try {
//                    final AboutPanel.Maker maker = AboutPanel.createMakerInstance(browserManager);
//                    maker.setUrl(url);
//                    getMainMenu().getToolBar().insert(maker.getButton(), 2);
//                    getMainMenu().getToolBar().forceLayout();
//                } catch (BrowserException ex) {
//                    ClientUtils.showError(ex.getMessage());
//                }
//            }
//        });
    }

    private static class BuildGUICommand extends SyncSchedulerChainCommand {

        private final SBFEntryPoint entryPoint;

        public BuildGUICommand(SBFEntryPoint entryPoint) {
            this.entryPoint = entryPoint;
        }

        @Override
        protected void onCommand() {
            RootPanel.get().clear();
            RootPanel.get().add(entryPoint);
        }

    }

    private static class LoadXMessagesCommand extends SyncSchedulerChainCommand {

        @Override
        protected void onCommand() {
            DefaultMessages.setMessages(new SBFMessages());
        }
    }

    private static class ExceptionHandlerCommand extends SyncSchedulerChainCommand {

        @Override
        protected void onCommand() {
            GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
                @Override
                public void onUncaughtException(Throwable e) {
                    if (e instanceof UmbrellaException) {
                        e = e.getCause();
                    }
                    if (e instanceof StatusCodeException) {
                        final StatusCodeException statusCodeException = (StatusCodeException) e;
                        if (statusCodeException.getStatusCode() == 0) {
                            return;
                        }
                    }
                    ClientUtils.alertException(e);
                }
            });
        }

    }

}
