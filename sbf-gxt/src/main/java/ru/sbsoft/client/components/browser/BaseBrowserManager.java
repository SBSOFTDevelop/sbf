package ru.sbsoft.client.components.browser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.XTemplate;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.CloseEvent;
import com.sencha.gxt.widget.core.client.event.CloseEvent.CloseHandler;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.IBrowserMaker;
import ru.sbsoft.client.components.ManagedBrowser;
import ru.sbsoft.client.components.exception.BrowserException;
import ru.sbsoft.client.components.grid.CurrentDateRequired;
import ru.sbsoft.client.components.treemenu.ApplicationPanel;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.ApplicationInfo;
import ru.sbsoft.shared.MenuTypeEnum;
import ru.sbsoft.shared.model.MenuItemModel;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 * Управляет браузерами в приложении. Регистрирует их и перенаправляет запросы
 * на их отображение. Базовый абстрактный класс для всех браузер-менеджеров
 * приложений, использующих фрамеворк SBF.
 * <p />Приложение на основе фрамеворка SBF должно содержать класс расширяющий
 * {@code BaseBrowserManager}.
 * <p>
 * Обычно класс-наследник создается как singleton object (один экземпляр длоя
 * одного gxt приложения) и перепопределяется метод {@link #registrBrowsers}, в
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
public abstract class BaseBrowserManager extends SimpleContainer {

    private final Map<String, IBrowserMaker> registrBrowsers = new HashMap<String, IBrowserMaker>();
    //
    private final LinkedList<BaseBrowserTabPanel> tabPanels = new LinkedList<BaseBrowserTabPanel>();
    private SelectionHandler<Widget> selectionHandler;
    private ApplicationPanel applicationPanel;
    private final CloseHandler<Widget> closeHandler;
    //
    protected HTML emptyPage;

    private String logoUrl = "/logo.png";

    public interface Renderer extends XTemplates {

        @XTemplate(source = "AboutInfoTemplate.html")
        public SafeHtml render(ApplicationInfo info, ApplicationLabel label, SafeUri logo);
    }

    public BaseBrowserManager() {
        super();
        closeHandler = createCloseHandler();
        getElement().getStyle().setBackgroundColor("AntiqueWhite");
    }

    public void initManager(ApplicationPanel applicationPanel) throws BrowserException {
        this.applicationPanel = applicationPanel;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

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

    private CloseHandler<Widget> createCloseHandler() {
        return new CloseHandler<Widget>() {

            public void onClose(CloseEvent<Widget> event) {
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
            p.setData("h", p.addCloseHandler(closeHandler));
        }
        if (p != null) {
            tabPanels.remove(p);
            tabPanels.add(p);
        }
        setWidget(p);
        return p;
    }

    @Override
    public void setWidget(Widget w) {
        if (w == null) {
            w = emptyPage;
        }
        if (w instanceof BaseBrowserTabPanel) {
            BaseBrowserTabPanel panel = (BaseBrowserTabPanel) w;
            if (applicationPanel != null) {
                 applicationPanel.setCaption(panel == null ? null : panel.getApplicationCode());
            }
        }
        super.setWidget(w);
        doLayout();
    }

    public void setApplicationInfo(ApplicationInfo info, String contextRootPath) {
        Renderer renderer = (Renderer) GWT.create(Renderer.class);

        emptyPage = new HTML(renderer.render(info, new ApplicationLabel(), UriUtils.fromString(contextRootPath + logoUrl)));
        emptyPage.getElement().getStyle().setOpacity(0.5);

        if (getWidget() == null) {
            setWidget(emptyPage);
            doLayout();
        }
    }

    public boolean removeBrowser(final AbstractContainer container) {
        final BaseBrowserTabPanel panel = getActiveApplicationPanel();
        final boolean result = panel.remove(container);
        if (panel.getWidgetCount() == 0) {
            HandlerRegistration h = (HandlerRegistration) panel.getData("h");
            h.removeHandler();

            tabPanels.remove(panel);
            setWidget(null);

            if (!tabPanels.isEmpty()) {
                selectApplicationPanel(tabPanels.getLast().getApplicationCode(), false);
            }
        }
        return result;
    }

    private BaseBrowserTabPanel getActiveApplicationPanel() {
        return (BaseBrowserTabPanel) getWidget();
    }
}
