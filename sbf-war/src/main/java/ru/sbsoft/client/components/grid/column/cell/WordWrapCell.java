package ru.sbsoft.client.components.grid.column.cell;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.shared.meta.ColumnWrapType;

/**
 *
 * @author sychugin
 */
public class WordWrapCell extends CustomCell<String> {

    private static final String WRAP_SYLE_PREFIX = "<div style=\"white-space: normal;";
    private static final String WRAP_SYLE_POSTFIX = "\" >";
    private static final String WORD_BREAK = "word-wrap: break-word; word-break: break-all;";

    private ColumnWrapType wrapType;

    public WordWrapCell(ColumnWrapType wrapType) {
        this.wrapType = wrapType;
    }

    @Override
    protected void applyText(SafeHtmlBuilder sb, String text) {
        String res;

        switch (wrapType) {
    // !!! ColumnWrapType.WORDWRAP and ColumnWrapType.WORDBREAK works incorrectly on LiveGridView because of different row height
//            case WORDBREAK:
//                res = WRAP_SYLE_PREFIX + WORD_BREAK + WRAP_SYLE_POSTFIX + text + "</div>";
//                break;
//            case WORDWRAP:
//                res = WRAP_SYLE_PREFIX + WRAP_SYLE_POSTFIX + text + "</div>";
//                break;

            case QTIP:
                res = "<span qtip=\"" + text.replace("\u00A0", "")  + "\">" + text + "</span>";
                break;
            default:
                res = text;
        }
        super.applyText(sb, res);
    }

}
