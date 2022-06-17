package ru.sbsoft.shared.consts;

/**
 *
 * @author Kiselev
 */
public enum I18nType implements I18nSection {

    GENERAL("GeneralContent"),
    BROWSER("BrowserContent"),
    EDITOR("EditorContent"),
    FORM("FormContent"),
    UPLOAD("UploaderConstants"),
    EXCEPTION("ExceptionContent"),
    XMESSAGES("XMessages"),
    REPORT("ReportContent");

    private String code;

    I18nType(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return code;
    }

}
