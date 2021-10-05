package ru.sbsoft.shared.model;

/**
 * @author balandin
 * @since Mar 29, 2013 3:12:59 PM
 */
public enum FileFormat {

    DOCX("Microsoft Word (*.DOCX)"),
    RTF("Rich Text Format (*.RTF)"),
    ODT("Open Office (*.ODT)"),
    PDF("Adobe PDF (*.PDF)"),
    XLS("Excel XLS (*.XLS)"),
    XLSX("Excel XLSX (*.XLSX)"),
    XML("eXtensible Markup Language XML (*.XML)");
    //
    private final String description;

    private FileFormat(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }

    public String getExtension() {
        return name();
    }
}
