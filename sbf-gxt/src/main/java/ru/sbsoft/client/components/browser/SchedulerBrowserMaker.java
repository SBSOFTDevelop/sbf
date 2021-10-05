package ru.sbsoft.client.components.browser;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.IBrowserMaker;
import ru.sbsoft.client.components.ManagedBrowser;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.consts.SBFGridEnum;

/**
 * Создает журнал планировщика.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class SchedulerBrowserMaker implements IBrowserMaker {

    private final FormContext formContext;

    /**
     * 
     * @param formContext контекст формы приложения.
     */
    public SchedulerBrowserMaker(FormContext formContext) {
        this.formContext = formContext;
    }

    @Override
    public String getIdBrowser() {
        return SBFGridEnum.SR_SCHEDULER.getCode();
    }
    
    @Override
    public String getTitleBrowser() {
        return SBFGridEnum.SR_SCHEDULER.getCode();
    }    

    @Override
    public String getSecurityId() {
        return SBFGridEnum.SR_SCHEDULER.getSecurityId();
    }

    @Override
    public ManagedBrowser createBrowser(String idBrowser, String titleBrowser) {
        final SchedulerGrid grid = new SchedulerGrid(formContext);
        final BaseBrowser browser = new BaseBrowser(idBrowser, titleBrowser, grid);
        browser.setShortName(I18n.get(SBFGeneralStr.labelJurnalPlan));
        return browser;
    }

}
