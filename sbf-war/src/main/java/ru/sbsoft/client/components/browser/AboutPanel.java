package ru.sbsoft.client.components.browser;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Frame;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.container.SimpleContainer;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.IBrowserMaker;
import ru.sbsoft.client.components.ManagedBrowser;
import ru.sbsoft.client.components.exception.BrowserException;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 * Информационная панель 'О приложении'.
 *
 * @author balandin
 * @since Jun 30, 2014 3:37:12 PM
 */
public class AboutPanel extends SimpleContainer implements ManagedBrowser {

    private String isBrowser;
    private static Maker instance;

    public static Maker createMakerInstance(BaseBrowserManager browserManager) throws BrowserException {
        if (instance != null) {
            throw new IllegalStateException("AboutPanel.Maker already exsists");
        }
        browserManager.addInstance(instance = new Maker(browserManager));
        return instance;
    }

    public static class Maker implements IBrowserMaker {

        private final String ID_BROWSER = "ABOUT_BROWSER";
        private final String TITLE_BROWSER = I18n.get(SBFGeneralStr.hintAbout);
        private final BaseBrowserManager browserManager;
        //
        private AboutPanel aboutPanel;
        private String url;
        private TextButton button;

        private Maker(BaseBrowserManager browserManager) {
            this.browserManager = browserManager;
        }

        @Override
        public ManagedBrowser createBrowser(String idBrowser, final String titleBrowser) {
            if (aboutPanel == null) {
                aboutPanel = new AboutPanel();
            }
            return aboutPanel;
        }

        @Override
        public String getIdBrowser() {
            return ID_BROWSER;
        }

        @Override
        public String getTitleBrowser() {
            return TITLE_BROWSER;
        }

        @Override
        public String getSecurityId() {
            return ID_BROWSER;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public TextButton getButton() {
            if (button == null) {
                button = new TextButton();
                button.setIcon(SBFResources.GENERAL_ICONS.Help());
                button.setToolTip(I18n.get(SBFGeneralStr.hintAbout));
                button.setData("svc-menutext", I18n.get(SBFGeneralStr.menuAbout));

                button.addSelectHandler(event -> showAboutPanel());
            }
            return button;
        }

        private void showAboutPanel() {
            try {
                browserManager.selectApplicationPanel(ID_BROWSER, true).openBrowser(ID_BROWSER, TITLE_BROWSER);
                if (url != null) {
                    aboutPanel.setUrl(url);
                }
            } catch (BrowserException ex) {
                ClientUtils.showError(ex.getMessage());
            }
        }
    }

    private final Frame frame;

    public AboutPanel() {
        frame = new Frame();
        frame.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        setWidget(frame);
    }

    public void setUrl(final String url) {
        frame.setUrl(url);
    }

    @Override
    public String getShortName() {
        return I18n.get(SBFGeneralStr.hintAbout);
    }

    @Override
    public void setShortName(String shortName) {
    }

    @Override
    public String getCaption() {
        return null;
    }

    @Override
    public void setCaption(String caption) {
    }

    @Override
    public void setIdBrowser(String value) {
        this.isBrowser = value;
    }

    @Override
    public String getIdBrowser() {
        return isBrowser;
    }
}
