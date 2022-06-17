package ru.sbsoft.client.schedule.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import ru.sbsoft.svc.widget.core.client.box.AlertMessageBox;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.i18n.GwTi18nUnit;
import ru.sbsoft.client.schedule.SchedulerChainCommand;
import ru.sbsoft.shared.api.i18n.i18nModuleInfo;
import ru.sbsoft.shared.consts.I18nSection;

/**
 * Команда асинхронной загрузки интернационализации с сервера.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class i18nCommand extends SchedulerChainCommand {

    private final I18nSection[] files;

    public i18nCommand(I18nSection[] files) {
        this.files = files;
    }

    @Override
    public void execute() {
        final String localeName = SBFi18nLocale.getLocaleName();
        loaDi18n(localeName);
    }

    private void next() {
        getChainManager().next();
    }

    private void loaDi18n(String locale) {
        SBFConst.I18N_SERVICE.geTi18n(locale, files, new AsyncCallback<i18nModuleInfo>() {

            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Cannot load i18n info", caught);
                showUpdateWindow();
            }

            @Override
            public void onSuccess(i18nModuleInfo result) {
                GwTi18nUnit.getInstance().addModuleInfo(result);
                next();
            }

        });
    }

    private void showUpdateWindow() {
        final int[] counter = {2};
        final AlertMessageBox alert = new AlertMessageBox("Cannot load i18n", "Cannot load i18n");
        
        final Button updateButton = new Button("update (" + counter[0] + ")");
        updateButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                update();
            }
        });

        final Button okButton = new Button("close");
        okButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                alert.hide();
            }
        });
        final Button continueButton = new Button("continue");
        continueButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                next();
                alert.hide();
            }
        });

        alert.setPredefinedButtons();
        alert.addButton(updateButton);
        alert.addButton(okButton);
        alert.addButton(continueButton);
        
        
        alert.show();

        Scheduler.get().scheduleFixedPeriod(new Scheduler.RepeatingCommand() {
            @Override
            public boolean execute() {
                updateButton.setHTML("update (" + counter[0] + ")");
                counter[0]--;
                final boolean exec = counter[0] < 0;
                if (exec) {
                    update();
                }
                return !exec;
            }
        }, 1000);
    }

    public void update() {
        Window.Location.reload();
    }
}
