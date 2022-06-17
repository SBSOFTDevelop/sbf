/*
 * Copyright (c) 2021 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.client.components.dialog;

import ru.sbsoft.svc.widget.core.client.Window;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.container.MarginData;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent;
import ru.sbsoft.svc.widget.core.client.form.FieldLabel;
import ru.sbsoft.svc.widget.core.client.form.FieldSet;
import ru.sbsoft.svc.widget.core.client.form.TextField;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.SaveAppInfoHandler;
import ru.sbsoft.client.components.ImageLoaderComponent;
import ru.sbsoft.client.components.form.TrimTextField;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.sbf.svc.components.VerticalFieldSet;
import ru.sbsoft.shared.ApplicationInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 *
 * @author sokolov
 */
public class AppSettingsForm extends Window {

    private final List<SaveAppInfoHandler> saveInfoHandlers = new ArrayList<>();
    private final TextField titleField;
    private final ImageLoaderComponent imageLc;

    private ApplicationInfo model;

    public AppSettingsForm() {
        super();

        setHeading(I18n.get(SBFGeneralStr.settingsCaption));
        setPixelSize(600, 460);
        setBlinkModal(true);
        setConstrain(false);
        setResizable(false);
        setClosable(false);

        final VerticalLayoutContainer vc = new VerticalLayoutContainer();

        final VerticalFieldSet fsTitle = new VerticalFieldSet(I18n.get(SBFGeneralStr.settingsProperty));
        titleField = new TrimTextField();
        titleField.setAllowBlank(false);
        fsTitle.add(new FieldLabel(titleField, I18n.get(SBFGeneralStr.settingsName)));
        vc.add(fsTitle);

        final FieldSet fsImage = new FieldSet();
        fsImage.setHeading(I18n.get(SBFGeneralStr.settingsLogo));
        imageLc = new ImageLoaderComponent(200, 200);
        fsImage.add(imageLc.asWidget(), new MarginData(0, 0, 0, 180));
        vc.add(fsImage, VLC.FILL);

        add(vc);

        TextButton cancelButton = new TextButton(I18n.get(SBFGeneralStr.labelCancelOperation));
        addButton(cancelButton);
        cancelButton.addSelectHandler((SelectEvent event) -> {
            hide();
        });
        
        TextButton applyButton = new TextButton(I18n.get(SBFGeneralStr.labelApply));
        addButton(applyButton);
        applyButton.addSelectHandler((SelectEvent event) -> {
            if (!isValidData()) {
                return;
            }
            mask();
            formToModel();
            SBFConst.SERVICE_SERVICE.saveApplicationInfo(model, new DefaultAsyncCallback<Void>(AppSettingsForm.this) {
                @Override
                public void onResult(Void result) {
                    super.onResult(result);
                    saveInfoHandlers.forEach(h -> h.onSave(model));
                    hide();
                }
            });

        });

    }

    @Override
    public void show() {
        super.show();
        mask();
        SBFConst.SERVICE_SERVICE.getApplicationInfo(new DefaultAsyncCallback<ApplicationInfo>(this) {
            @Override
            public void onResult(ApplicationInfo result) {
                super.onResult(result);
                model = result;
                modelToForm();
            }
        });
    }
    
    public void addSaveInfoHandler(SaveAppInfoHandler handler) {
        saveInfoHandlers.add(handler);
    }

    private void modelToForm() {
        titleField.setValue(model.getApplication());
        imageLc.setStringImage(model.getLogo());
        isValidData();
    }

    private void formToModel() {
        model.setApplication(titleField.getValue());
        model.setLogo(imageLc.getStringImage());
    }

    private boolean isValidData() {
        return titleField.validate();
    }
    
}
