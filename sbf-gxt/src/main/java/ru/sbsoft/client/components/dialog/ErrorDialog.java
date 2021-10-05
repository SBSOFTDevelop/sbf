package ru.sbsoft.client.components.dialog;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextArea;
import ru.sbsoft.client.I18n;
import ru.sbsoft.sbf.gxt.components.VerticalFieldSet;
import ru.sbsoft.client.consts.SBFConfig;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.consts.SBFVariable;
import ru.sbsoft.client.schedule.i18n.SBFi18nLocale;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.HLD;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.EMailMessage;
import ru.sbsoft.shared.EMailResult;
import ru.sbsoft.shared.exceptions.ThrowableWrapper;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 * Диалог для отображения ошибок приложения. Используется в
 * {@link ru.sbsoft.client.utils.ClientUtils#alertException(java.lang.Throwable, ru.sbsoft.client.utils.ClientUtils.ErrorHandler)}
 *
 * @author balandin
 * @since May 17, 2013 4:30:13 PM
 */
public class ErrorDialog extends Window {

    private final VerticalLayoutContainer container;
    private final HTML log;
    private TextArea fieldMessage;
//    private static final IEMailServiceAsync EMAIL_RPC = SBFConst.EMAIL_SERVICE;

    public ErrorDialog() {
        super();

        setPixelSize(800, 400);
        setModal(true);
        setBlinkModal(true);
        setConstrain(false);
        setButtonAlign(BoxLayoutContainer.BoxLayoutPack.CENTER);

        getHeader().setIcon(SBFResources.GENERAL_ICONS.Error16());
        getHeader().setText(I18n.get(SBFGeneralStr.labelServerError));

        TextButton addButton = new TextButton(I18n.get(SBFGeneralStr.labelClose));
        addButton(addButton);
        addButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                hide();
            }
        });

        container = new VerticalLayoutContainer();
        setWidget(container);

        log = new HTML();

        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.setWidget(log);

        container.add(scrollPanel, new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(3)));

        if (Strings.clean(SBFConfig.readString(SBFVariable.EMAIL_DEVELOPER)) != null) {
            addSendDeveloper();
        }

    }

    private void addSendDeveloper() {
        VerticalFieldSet fsetDeveloper = new VerticalFieldSet(I18n.get(SBFGeneralStr.labelSendToDeveloper));

        SimpleContainer panel = new SimpleContainer();
        panel.setHeight(60);
        fsetDeveloper.add(panel);
        HorizontalLayoutContainer hContainer = new HorizontalLayoutContainer();
        HLD layoutText = new HLD(0.85, 1);
        HLD layoutButton = new HLD(0.15, 1);
        fieldMessage = new TextArea();
        TextButton btnSend = new TextButton(I18n.get(SBFGeneralStr.labelSend));
        btnSend.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                sendError();
            }
        });
        hContainer.add(fieldMessage, layoutText);
        VBoxLayoutContainer boxContainer = new VBoxLayoutContainer(VBoxLayoutContainer.VBoxLayoutAlign.CENTER);
        boxContainer.setPadding(new Padding(20, 0, 20, 0));
        boxContainer.add(btnSend);
        hContainer.add(boxContainer, layoutButton);
        panel.setWidget(hContainer);
        container.add(fsetDeveloper, VLC.CONST);

    }

    private void sendError() {
        SBFConst.EMAIL_SERVICE.sendMessage(SBFi18nLocale.getLocaleName(), createMessage(), new AsyncCallback<EMailResult>() {
            @Override
            public void onFailure(Throwable caught) {
                ClientUtils.alertException(caught);
            }

            @Override
            public void onSuccess(EMailResult result) {
                if (result.getResult() == EMailResult.ResultEnum.OK) {
                    ClientUtils.message(I18n.get(SBFGeneralStr.captQuery), I18n.get(SBFGeneralStr.msgEMailSended));
                } else {
                    ClientUtils.alertWarning(result.getError());
                }
            }
        });
    }

    private EMailMessage createMessage() {
        EMailMessage message = new EMailMessage();
        message.setAddressTo(SBFConfig.readString(SBFVariable.EMAIL_DEVELOPER));
        message.setSubject(I18n.get(SBFGeneralStr.msgEMailErrorSubject) + " " + SBFConfig.readString(SBFVariable.APPLICATION_TITLE));
        StringBuilder sb = new StringBuilder();
        if (null != Strings.clean(fieldMessage.getValue())) {
            sb.append(fieldMessage.getValue());
            sb.append("\n\n");
        }
        sb.append(addNextLine(log.getText()));
        message.setBody(sb.toString());
        message.setFlagHiPriority(false);
        message.setFlagRecept(false);
        message.setFlagRead(false);
        return message;
    }
    
    //заменяем не переносимые пробелы и пробелы на возврат каретки
    private String addNextLine(final String st) {
        if (null == st) {
            return "";
        }
        return st.replaceAll("\\xA0{6,}", "\n").replaceAll("\\s{6,}", "\n");
    }

    public void show(String message) {
        log.setHTML(message);
        super.show();
    }

    public void show(ThrowableWrapper throwableWrapper) {
        log.setHTML(throwableWrapper.generateTrace());
        super.show();
    }
}
