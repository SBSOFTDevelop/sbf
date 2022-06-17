package ru.sbsoft.client.components.browser;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.widget.core.client.event.BeforeCloseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.IBrowserMaker;
import ru.sbsoft.client.components.ManagedBrowser;
import ru.sbsoft.client.components.actions.event.KeyActionController;
import ru.sbsoft.client.components.actions.event.StdActKey;
import ru.sbsoft.client.components.exception.BrowserException;
import ru.sbsoft.client.components.grid.CurrentDateRequired;
import ru.sbsoft.client.utils.CliUtil;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.client.utils.WidgetCloseValidalidator;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.ApplicationInfo;
import ru.sbsoft.shared.MenuTypeEnum;
import ru.sbsoft.shared.model.MenuItemModel;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.svc.widget.core.client.container.CardLayoutContainer;

/**
 * Управляет браузерами в приложении. Регистрирует их и перенаправляет запросы
 * на их отображение. Базовый абстрактный класс для всех браузер-менеджеров
 * приложений, использующих фрамеворк SBF.
 * <p>
 * Приложение на основе фрамеворка SBF должно содержать класс расширяющий
 * {@code BaseBrowserManager}.
 * <p>
 * Обычно класс-наследник создается как singleton object (один экземпляр длоя
 * одного svc приложения) и перепопределяется метод {@link #registrBrowsers}, в
 * котором добавляются браузеры используемые в этом приложении.
 * <p>
 * Например:
 * <pre>
 * public class BrowserManager extends BaseBrowserManager {
 *
 *   private final static BrowserManager INSTANCE = new BrowserManager();
 *
 *   public static BrowserManager getInstance() {
 *       return INSTANCE;
 *   }
 *
 *   @Override
 *   protected void registrBrowsers() throws BrowserException {
 *       // Справочники
 *       addInstance(new AccountTypeBrowserMaker());
 *       addInstance(new AccountVidBrowserMaker());
 *        ....
 *   }
 * }
 * </pre> GWT приложение на основе фрамеворка SBF расширяет класс
 * {@link ru.sbsoft.shared.model.ApplicationMenuTreeModel.SBFEntryPoint}. В
 * конструкторе класса-наследника необходимо проинициализировать поле
 * {@code protected BaseBrowserManager browserManager} экземпляром
 * {@code BaseBrowserManager}.
 * <p>
 * Например:
 * <pre>
 * public class nsigwt extends SBFEntryPointImpl {
 *
 *   public nsigwt() {
 *       super();
 *       this.TITLE_TEXT = "Нормативно справочная информация";
 *       this.ABOUT_TEXT = "НСИ версия 1.00.01";
 *       //
 *       this.browserManager = BrowserManager.getInstance();
 *       ...
 *   }
 * </pre>
 *
 * @author panarin
 * @see BaseBrowserTabPanel
 */
public abstract class BaseBrowserManager extends CardLayoutContainer {

    private static BaseBrowserManager instance = null;

    public static BaseBrowserManager getBrowserManager() {
        if (instance == null) {
            instance = findBrowserManager(RootPanel.get());
        }
        return instance;
    }

    private static BaseBrowserManager findBrowserManager(HasWidgets ws) {
        if (ws != null) {
            Iterator<Widget> iter = ws.iterator();
            while (iter.hasNext()) {
                Widget w = iter.next();
                if (isBrowserManager(w)) {
                    return (BaseBrowserManager) w;
                }
            }
            iter = ws.iterator();
            while (iter.hasNext()) {
                Widget w = iter.next();
                if (w instanceof HasWidgets) {
                    BaseBrowserManager bm = findBrowserManager((HasWidgets) w);
                    if (bm != null) {
                        return bm;
                    }
                }
            }
        }
        return null;
    }

    private static boolean isBrowserManager(Widget w) {
        if (w != null) {
            Class bmc = BaseBrowserManager.class;
            Class wc = w.getClass();
            while (wc != null) {
                if (wc.equals(bmc)) {
                    return true;
                }
                wc = wc.getSuperclass();
            }
        }
        return false;
    }

    private static final String defaultLogoUrl = "./logo.png";
    private final Map<String, IBrowserMaker> registrBrowsers = new HashMap<String, IBrowserMaker>();
    //
    private final LinkedList<BaseBrowserTabPanel> tabPanels = new LinkedList<BaseBrowserTabPanel>();
    private SelectionHandler<Widget> selectionHandler;
    //private ApplicationPanel applicationPanel;
    private final BeforeCloseEvent.BeforeCloseHandler<Widget> closeHandler;
    //
    protected HTML emptyPage;

    private final KeyActionController keyActionController;

    /*
    public interface Renderer extends XTemplates {

        @XTemplate(source = "AboutInfoTemplate.html")
        SafeHtml render(ApplicationInfo info, ApplicationLabel label, SafeUri logo);
    }
     */
    public BaseBrowserManager() {
        super();
        closeHandler = createCloseHandler();
        getElement().getStyle().setBackgroundColor("AntiqueWhite");
        keyActionController = new KeyActionController(this);
        keyActionController.addAction(StdActKey.SWITCH_TAB, () -> CliUtil.switchNextTab(getActiveApplicationPanel()));
    }

    public void initManager() throws BrowserException {
//        this.applicationPanel = applicationPanel;
        registrBrowsers();
    }

    protected abstract void registrBrowsers() throws BrowserException;

    public void addInstance(final IBrowserMaker maker) throws BrowserException {
        if (registrBrowsers.containsKey(maker.getIdBrowser())) {
            throw new BrowserException(I18n.get(SBFBrowserStr.msgBrowserExist, maker.getIdBrowser()));
        }
        registrBrowsers.put(maker.getIdBrowser(), maker);
    }

    public ManagedBrowser getRegisteredBrowser(final String idBrowser, final String titleBrowser) throws BrowserException {
        final IBrowserMaker maker = registrBrowsers.get(idBrowser);
        if (null == maker) {
            throw new BrowserException(I18n.get(SBFBrowserStr.msgBrowserNotRegistr, idBrowser));
        }

        return createBrowser(maker, idBrowser, titleBrowser);
    }

    /*    
    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
     */
    public ManagedBrowser createBrowser(IBrowserMaker maker) {
        return createBrowser(maker, maker.getIdBrowser(), maker.getTitleBrowser());
    }

    public ManagedBrowser createBrowser(IBrowserMaker maker, String idBrowser, String titleBrowser) {
        if (null == maker) {
            throw new IllegalArgumentException("Argument browser can't be null");
        }
        final ManagedBrowser browser = maker.createBrowser(idBrowser, titleBrowser);
        if (browser != null) {
            browser.setIdBrowser(idBrowser);
            if (browser instanceof BaseBrowser) {
                BaseBrowser baseBrowser = (BaseBrowser) browser;
                if (maker instanceof CurrentDateRequired) {
                    baseBrowser.enableCurrentDate();
                }
            }
        }
        return browser;
    }

    public Map<String, IBrowserMaker> getRegistrBrowsers() {
        return registrBrowsers;
    }

    public AbstractContainer openBrowser(final String appCode, final MenuItemModel model) throws BrowserException {
        if (MenuTypeEnum.Browser != model.getMenuType()) {
            throw new BrowserException(I18n.get(SBFGeneralStr.msgCommandDifferent, model.getMenuName()));
        }
        return selectApplicationPanel(appCode, true).openBrowser(model);
    }

    public void setSelectionHandler(SelectionHandler<Widget> selectionHandler) {
        this.selectionHandler = selectionHandler;
    }

    public SelectionHandler<Widget> getSelectionHandler() {
        return selectionHandler;
    }

    private BeforeCloseEvent.BeforeCloseHandler<Widget> createCloseHandler() {
        return new BeforeCloseEvent.BeforeCloseHandler<Widget>() {
            @Override
            public void onBeforeClose(BeforeCloseEvent<Widget> event) {
                event.setCancelled(true);
                removeBrowser((AbstractContainer) event.getItem());
            }
        };
    }

    private BaseBrowserTabPanel findTabPanel(String appCode) {
        for (BaseBrowserTabPanel p : tabPanels) {
            if (Strings.equals(p.getApplicationCode(), appCode)) {
                return p;
            }
        }
        return null;
    }

    public BaseBrowserTabPanel selectApplicationPanel(final String appCode, final boolean createIfNotExist) {
        BaseBrowserTabPanel p = findTabPanel(appCode);
        if (p == null && createIfNotExist) {
            p = new BaseBrowserTabPanel(this, appCode);
            p.setData("h", p.addBeforeCloseHandler(closeHandler));
            tabPanels.add(p);
        }
//        if (p != null) {
//            tabPanels.remove(p);
//            tabPanels.add(p);
//        }
        setActiveWidget(p);
        return p;
    }

    @Override
    public void setActiveWidget(Widget w) {
        if (w == null) {
            w = emptyPage;
        }
        /*        
        if (w instanceof BaseBrowserTabPanel) {
            BaseBrowserTabPanel panel = (BaseBrowserTabPanel) w;
            if (applicationPanel != null) {
                applicationPanel.setCaption(panel.getApplicationCode());
            }
        }
         */
        super.setActiveWidget(w);
    }

    public void setApplicationInfo(ApplicationInfo info) {
        emptyPage = buildMainPage(info);
        emptyPage.getElement().getStyle().setOpacity(0.5);
        setActiveWidget(emptyPage);
    }

    public void removeBrowser(final AbstractContainer container) {
        WidgetCloseValidalidator.confirmClose(container, new DefaultAsyncCallback<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                if (Boolean.TRUE.equals(result)) {
                    final BaseBrowserTabPanel panel = getActiveApplicationPanel();
                    panel.remove(container);
                    if (panel.getWidgetCount() == 0) {
                        HandlerRegistration h = (HandlerRegistration) panel.getData("h");
                        h.removeHandler();

                        tabPanels.remove(panel);
                        setActiveWidget(null);
                        remove(panel);

                        if (!tabPanels.isEmpty()) {
                            selectApplicationPanel(tabPanels.getLast().getApplicationCode(), false);
                        }
                    }
                }
            }
        });
    }

    private BaseBrowserTabPanel getActiveApplicationPanel() {
        return (BaseBrowserTabPanel) getActiveWidget();
    }

    private HTML buildMainPage(ApplicationInfo info) {
        ApplicationLabel label = new ApplicationLabel();
        StringBuilder sb = new StringBuilder("<div style=\"padding:50px;font-family: tahoma,arial,verdana,sans-serif;font-size: 12px; background:none;\">\n");
        sb.append("<table><tr>\n");
        sb.append("<td><img src=\"");
        if (info.getLogo() != null && !info.getLogo().isEmpty()) {
            sb.append(info.getLogo());
        } else {
            sb.append(defaultLogoUrl);
        }
        sb.append("\" style=\"border:0;\"></td>\n"
                + "<td>\n"
                + "<ul style=\"list-style:disc; margin:0px 0px 5px 15px; padding-left:0px;\">\n");
        sb.append("<li>");
        sb.append(info.getApplication());
        sb.append("</li>\n");
        sb.append("<li>");
        sb.append(label.getVersion());
        sb.append(": ");
        sb.append(info.getVersion());
        sb.append("</li>\n");
        sb.append("\n</ul>\n</td>\n</tr></table>\n</div>");
        return new HTML(sb.toString());
    }
}
