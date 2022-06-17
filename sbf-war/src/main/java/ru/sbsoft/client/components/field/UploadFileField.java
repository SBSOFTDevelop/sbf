package ru.sbsoft.client.components.field;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import gwtupload.client.IFileInput;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.Uploader;
import ru.sbsoft.client.components.AllowBlankControl;
import ru.sbsoft.client.components.form.CustomField;
import ru.sbsoft.client.components.form.SBFUploaderConstants;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.sbf.svc.components.FieldsContainer;
import ru.sbsoft.sbf.svc.utils.FieldUtils;

/**
 *
 * @author sokolov
 */
public class UploadFileField extends CustomField<String> implements HasValueChangeHandlers<String>, AllowBlankControl {
    
    private final FileLnkField fileLnkField;
    private final Uploader uploader;
    private final PushButton clearBtn;
    private final PushButton uploaderButton;

    public UploadFileField() {
        this.fileLnkField = new FileLnkField();

        clearBtn = new PushButton(new Image(SBFResources.APP_ICONS.Delete24()));
        clearBtn.setTitle("Стереть");

        clearBtn.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        clearBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String oldVal = fileLnkField.getValue();
                fileLnkField.setValue(null);

                if (oldVal != null) {
                    ValueChangeEvent.fire(UploadFileField.this, (String) null);
                }

            }
        });
        FieldsContainer container = new FieldsContainer();

        //Image uploaderButtonImage = new Image(SBFResources.APP_ICONS.OpenFile24());
        uploaderButton = new PushButton(new Image(SBFResources.APP_ICONS.OpenFile24()));
        uploaderButton.setTitle("Загрузить");
        uploader = new Uploader(IFileInput.FileInputType.CUSTOM.with(uploaderButton));
        uploader.setI18Constants(new SBFUploaderConstants());
        //uploader.setMultipleSelection(false);
        uploader.setMultipleSelection(true);
        uploader.setAutoSubmit(true);
        //uploader.setAvoidRepeatFiles(false);
        uploader.addOnFinishUploadHandler(new IUploader.OnFinishUploaderHandler() {
            @Override
            public void onFinish(final IUploader uploader) {
                switch (uploader.getStatus()) {
                    case CANCELED:
                    case ERROR:
                    case INVALID:
                        //setStatusText(I18n.get(SBFGeneralStr.msgDownloadInterrupted));
                        break;
                    case SUCCESS:
                    case DONE:
                        //setStatusText(I18n.get(SBFGeneralStr.msgFileHosted));
                        break;
                }
            }
        });
        uploader.addOnCancelUploadHandler(new IUploader.OnCancelUploaderHandler() {
            @Override
            public void onCancel(IUploader parUploader) {
                //setStatusText(I18n.get(SBFGeneralStr.msgDownloadInterrupted));
                final IUploadStatus status = uploader.getStatusWidget();
                status.setVisible(false);
                final IFileInput fileInput = uploader.getFileInput();
                fileInput.setVisible(true);
                uploader.reset();
            }
        });
        uploader.addOnStartUploadHandler(new IUploader.OnStartUploaderHandler() {
            @Override
            public void onStart(IUploader uploader) {
                uploader.getFileInput().setVisible(false);
                //setStatusText("");
            }
        });

        container.add(uploader, HLC.CONST);
        container.add(clearBtn, HLC.CONST);
        container.add(FieldUtils.createSeparator(4), HLC.CONST);
        container.add(fileLnkField, HLC.FILL);
        setWidget(container);

    }
    
    public HandlerRegistration addOnStartUploadHandler(final IUploader.OnStartUploaderHandler handler) {
        return uploader.addOnStartUploadHandler(handler);
    }

    public Uploader getUploader() {

        return uploader;
    }

    @Override
    public void setValue(String value) {
        fileLnkField.setValue(value);
    }

    @Override
    public String getValue() {
        return fileLnkField.getValue();
    }
        
    @Override
    public boolean isReadOnly() {
        return fileLnkField.isReadOnly();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        uploader.setEnabled(enabled);
    }    

    @Override
    public void setReadOnly(boolean readOnly) {
        uploader.setEnabled(!readOnly);
        uploaderButton.setEnabled(!readOnly);
        clearBtn.setEnabled(!readOnly);
        if (readOnly) {
            uploaderButton.addStyleName(disabledStyle);
            clearBtn.addStyleName(disabledStyle);
        } else {
            clearBtn.removeStyleName(disabledStyle);
            uploaderButton.removeStyleName(disabledStyle);
        }
        fileLnkField.setReadOnly(readOnly);
    }

    @Override
    public void setAllowBlank(boolean value) {

    }

    @Override
    public boolean isAllowBlank() {
        return true;
    }

    
}
