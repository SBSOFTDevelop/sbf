package ru.sbsoft.client.components.form;

//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.event.dom.client.HasClickHandlers;
//import com.google.gwt.uibinder.client.UiConstructor;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.Focusable;
//import com.google.gwt.user.client.ui.FormPanel;
//import com.google.gwt.user.client.ui.HasEnabled;
//import com.google.gwt.user.client.ui.HasText;
//import com.google.gwt.user.client.ui.Hidden;
//import com.google.gwt.user.client.ui.Widget;
//import gwtupload.client.BaseUploadStatus;
//import gwtupload.client.IFileInput.FileInputType;
//import gwtupload.client.IUploadStatus;
//import gwtupload.client.IUploadStatus.Status;
//import gwtupload.client.IUploader;
//import java.util.ArrayList;

/**
 * <p> Implementation of a single uploader panel with a submit button. </p>
 *
 * @author Manolo Carrasco MoГ±ino
 *
 * <p> When the user selects a file, the button changes its style so the she could realize that she has to push the
 * button. </p>
 *
 */
//TODO not used
public class SBFSingleUploader {} 
//extends SBFUploader {
//
//    protected Widget button;
//
//    private void setEnabledButton(boolean b) {
//        if (button != null) {
//            // HasEnabled is only available after gwt-2.1.x
//            if (button instanceof HasEnabled) {
//                ((HasEnabled) button).setEnabled(b);
//            } else if (button instanceof Button) {
//                ((Button) button).setEnabled(b);
//            }
//        }
//    }
//
//    /**
//     * Default constructor. Uses the standard browser input, a basic status widget, and creates a standard button to
//     * send the file
//     *
//     */
//    public SBFSingleUploader() {
//        this(FileInputType.BROWSER_INPUT);
//    }
//
//    /**
//     * Use a basic status widget, and creates a standard button to send the file
//     *
//     * @param type file input to use
//     */
//    @UiConstructor
//    public SBFSingleUploader(FileInputType type) {
//        this(type, null);
//    }
//
//    /**
//     * Creates a standard button to send the file
//     *
//     * @param type file input to use
//     * @param status Customized status widget to use
//     */
//    public SBFSingleUploader(FileInputType type, IUploadStatus status) {
//        this(type, status, new Button());
//    }
//
//    /**
//     * Constructor
//     *
//     * @param type file input to use
//     * @param status Customized status widget to use
//     * @param submitButton Customized button which submits the form
//     */
//    public SBFSingleUploader(FileInputType type, IUploadStatus status, Widget submitButton) {
//        this(type, status, submitButton, null);
//    }
//
//    /**
//     * This constructor allows to use an existing form panel.
//     *
//     * @param type file input to use
//     * @param status Customized status widget to use
//     * @param submitButton Customized button which submits the form
//     * @param form Customized form panel
//     */
//    public SBFSingleUploader(FileInputType type, IUploadStatus status, Widget submitButton, FormPanel form) {
//        super(type, form);
//
//        final SBFUploader thisInstance = this;
//
//        if (status == null) {
//            status = new BaseUploadStatus();
//            super.add(status.getWidget());
//        }
//        super.setStatusWidget(status);
//
//        this.button = submitButton;
//        if (submitButton != null) {
//            submitButton.addStyleName("submit");
//            if (submitButton instanceof HasClickHandlers) {
//                ((HasClickHandlers) submitButton).addClickHandler(new ClickHandler() {
//                    @Override
//                    public void onClick(ClickEvent event) {
//                        thisInstance.submit();
//                    }
//                });
//            }
//            if (submitButton instanceof HasText) {
//                ((HasText) submitButton).setText(I18N_CONSTANTS.uploaderSend());
//            }
//            // The user could have attached the button anywhere in the page.
//            if (!submitButton.isAttached()) {
//                super.add(submitButton);
//            }
//        }
//    }
//
//    /**
//     * Uses the standard browser input, customized status, and creates a standard button to send the file
//     *
//     * @param status Customized status widget to use
//     */
//    public SBFSingleUploader(IUploadStatus status) {
//        this(FileInputType.BROWSER_INPUT, status);
//    }
//
//    /**
//     *
//     * @param status Customized status widget to use
//     * @param submitButton Customized button which submits the form
//     */
//    public SBFSingleUploader(IUploadStatus status, Widget submitButton) {
//        this(FileInputType.BROWSER_INPUT, status, submitButton, null);
//    }
//
//    @Override
//    public void setEnabled(boolean b) {
//        super.setEnabled(b);
//        setEnabledButton(b);
//    }
//
//    /* (non-Javadoc)
//     * @see gwtupload.client.Uploader#setI18Constants(gwtupload.client.IUploader.UploaderConstants)
//     */
//    @Override
//    public void setI18Constants(IUploader.UploaderConstants strs) {
//        super.setI18Constants(strs);
//        if (button != null && button instanceof HasText) {
//            ((HasText) button).setText(strs.uploaderSend());
//        }
//    }
//
//    /* (non-Javadoc)
//     * @see gwtupload.client.Uploader#onChangeInput()
//     */
//    @Override
//    protected void onChangeInput() {
//        super.onChangeInput();
//        if (button != null) {
//            button.addStyleName("changed");
//            if (button instanceof Focusable) {
//                ((Focusable) button).setFocus(true);
//            }
//        }
//    }
//    ArrayList<Widget> formWidgets = new ArrayList<Widget>();
//
//    @Override
//    public void add(Widget w) {
//        formWidgets.add(w);
//        super.add(w);
//    }
//
//    @Override
//    public void add(Widget w, int index) {
//        formWidgets.add(w);
//        super.add(w, index);
//    }
//
//    @Override
//    public void clear() {
//        formWidgets.clear();
//        super.clear();
//    }
//
//
//    /* (non-Javadoc)
//     * @see gwtupload.client.Uploader#onFinishUpload()
//     */
//    @Override
//    protected void onFinishUpload() {
//        super.onFinishUpload();
//        if (getStatus() == Status.REPEATED) {
//            getStatusWidget().setError(getI18NConstants().uploaderAlreadyDone());
//        }
//        getStatusWidget().setStatus(Status.UNINITIALIZED);
//        reuse();
//        assignNewNameToFileInput();
//        for (Widget i : formWidgets) {
//            if (i instanceof Hidden) {
//                Hidden h = (Hidden) i;
//                if (h.getValue().startsWith(fileInputPrefix)) {
//                    h.setValue(getInputName());
//                }
//            }
//        }
//
//        getFileInput().getWidget().setVisible(true);
//        if (button != null) {
//            setEnabledButton(true);
//            button.removeStyleName("changed");
//            if (!autoSubmit) {
//                button.setVisible(true);
//            }
//        }
//        if (autoSubmit) {
//            getFileInput().setText(i18nStrs.uploaderBrowse());
//        }
//    }
//
//    /* (non-Javadoc)
//     * @see gwtupload.client.Uploader#onStartUpload()
//     */
//    @Override
//    protected void onStartUpload() {
//        super.onStartUpload();
//        if (button != null) {
//            setEnabledButton(false);
//            button.removeStyleName("changed");
//            button.setVisible(false);
//        }
//        getFileInput().getWidget().setVisible(false);
//    }
//
//    public void setAvoidRepeatFiles(boolean b) {
//        this.avoidRepeatFiles(b);
//    }
//
//    /* (non-Javadoc)
//     * @see gwtupload.client.Uploader#setAutoSubmit(boolean)
//     */
//    @Override
//    public void setAutoSubmit(boolean b) {
//        if (button != null) {
//            button.setVisible(!b);
//        }
//        super.setAutoSubmit(b);
//    }
//}
