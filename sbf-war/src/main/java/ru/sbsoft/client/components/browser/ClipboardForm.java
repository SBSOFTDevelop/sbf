package ru.sbsoft.client.components.browser;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.user.client.Timer;
import ru.sbsoft.svc.widget.core.client.Dialog;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.container.MarginData;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent;
import ru.sbsoft.svc.widget.core.client.event.ShowEvent;
import ru.sbsoft.svc.widget.core.client.form.TextArea;
import ru.sbsoft.svc.widget.core.client.form.Validator;
import ru.sbsoft.svc.widget.core.client.form.error.DefaultEditorError;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * @author sychugin
 */
public class ClipboardForm {

    private void onShow(ShowEvent event) {
        clipboardText.focus();
    }

    public interface Callback {

        void paste(BigDecimal[][] dump);

    }

    private Callback callback;

    private final int maxRow;
    private final int maxCol;

    private final TextArea clipboardText;

    private final Dialog dlg;
    // private final TextButton cancelBt;
    private final TextButton oklBt = new TextButton("Вставить");
    private final PasteHandler pasteHandler = new PasteHandler();

    private class BtntUpdater extends Timer {

        @Override
        public void run() {
            update();
        }
    }

    private void update() {
        oklBt.setEnabled(verify());
    }

    public ClipboardForm(int maxRow, int maxCol) {

        this.maxRow = maxRow;
        this.maxCol = maxCol;
        clipboardText = new TextArea();

        //clipboardText.addValidator((editor, value) -> {
        //     return new DumpValidator().validate(editor, value);
        //  });
        dlg = new Dialog();
        dlg.setModal(true);
        dlg.setPredefinedButtons(Dialog.PredefinedButton.CLOSE);
        dlg.addShowHandler((event) ->
                clipboardText.focus());


        clipboardText.addValueChangeHandler((event) -> {
            oklBt.setEnabled(verify());
            dlg.getButtonBar().forceLayout();

        });

        clipboardText.addKeyDownHandler((KeyDownEvent event) -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && verify()) {
                pasteHandler.onSelect(null);

            }

        });

        dlg.addButton(oklBt);
        oklBt.setEnabled(false);
        dlg.setBodyStyleName("pad-text");
        dlg.getBody().addClassName("pad-text");
        //dlg.setHideOnButtonClick(true);

        dlg.setClosable(false);
        dlg.setPixelSize(800, 600);
        dlg.add(clipboardText, new MarginData(5));
        dlg.setHeading("Вставить текст из clipboard");

        oklBt.addSelectHandler(pasteHandler);

        BtntUpdater updater = new BtntUpdater();
        dlg.addShowHandler((event) -> {

            clipboardText.selectAll();
            // pasteClipboardText();
            if (updater.isRunning()) {
                updater.cancel();
            }

            updater.scheduleRepeating(500);

        });

        dlg.addBeforeHideHandler((event) -> {
            if (updater.isRunning()) {
                updater.cancel();
            }

        });

    }

    public void onClipboardRead(String text) {
        clipboardText.setText(text);

    }

    public static native String[] split(String string, String separator) /*-{
        return string.split(separator);
    }-*/;

    public void focus() {

        clipboardText.focus();

    }

    public native void pasteClipboardText() /*-{
        if (navigator.clipboard) {
            var that = this;
            this.@ru.sbsoft.client.components.browser.ClipboardForm::focus();

            navigator.clipboard.readText().then(
                function (value) {

                    that.@ru.sbsoft.client.components.browser.ClipboardForm::onClipboardRead(Ljava/lang/String;)(value);
                }
            )
        } else {
            alert("navigator.clipboard not supported")

        }

    }-*/;

    private boolean verify() {
        final String text = clipboardText.getText();

        return (text != null && !text.trim().isEmpty());

    }


    private String lnTrim(String val) {
        for (int i = val.length() - 1; i >= 0; i--) {
            char c = val.charAt(i);
            if (c != '\n' && c != '\r') {
                return val.substring(0, i + 1);
            }
        }
        return "";
    }


    private class ParseDmp {

        public BigDecimal[][] parse(String text) {

            if (text == null) {
                return null;
            }

            text = lnTrim(text.trim());

            if (text.trim().isEmpty()) return null;


            int colsCount = 0;
            String[] lines = split(text, "\n");
            BigDecimal[][] rows = new BigDecimal[Math.min(lines.length, maxRow)][];

            for (int i = 0; i < rows.length; i++) {

                if (lines[i].trim().isEmpty()) {
                    rows[i] = null;
                } else {
                    String[] cols = split(lines[i], "\t");
                    BigDecimal[] bcols = new BigDecimal[Math.min(cols.length, maxCol)];
                    rows[i] = bcols;

                    if (colsCount == 0) {
                        colsCount = bcols.length;
                    }
                    for (int j = 0; j < bcols.length; j++) {
                        try {

                            bcols[j] = BigDecimal.valueOf(Double.parseDouble(cols[j]));

                        } catch (NumberFormatException ex) {
                            bcols[j] = null;
                        }
                    }

                }

            }

            return normalizeRows(rows, colsCount);

            //return rows;
        }

    }

    private BigDecimal[][] normalizeRows(BigDecimal[][] rows, int cols) {

        for (int i = 0; i < rows.length; i++) {

            if (rows[i] == null) {
                rows[i] = new BigDecimal[cols];
            }

        }
        return rows;
    }

    private class PasteHandler implements SelectEvent.SelectHandler {

        @Override
        public void onSelect(SelectEvent event) {

            dlg.hide();
            if (!verify()) {
                clipboardText.setText(null);

            }
            if (callback != null) {

                callback.paste(new ParseDmp().parse(clipboardText.getText()));
            }

        }

    }

    private static class DumpValidator implements Validator<String> {

        @Override
        public List<EditorError> validate(Editor<String> editor, String value) {

            if (value == null || value.trim().isEmpty()) {
                return Collections.singletonList(new DefaultEditorError(editor, "Не задано значение", value));
            }

            return null;

        }

    }

    public void getClipboard(Callback callback) {
        this.callback = callback;
        dlg.show();

    }
}
