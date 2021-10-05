package ru.sbsoft.client.components.browser;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author sychugin
 */
public class ClipboardForm {
    
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
    
    public ClipboardForm(int maxRow, int maxCol) {
        
        this.maxRow = maxRow;
        this.maxCol = maxCol;
        clipboardText = new TextArea();
        
       
        
        
        clipboardText.addValidator((editor, value) -> {
            return new DumpValidator().validate(editor, value);
            
        });

        // clipboardText.addChangeHandler((event) -> {
        //     oklBt.setEnabled(verify());
        // });
        dlg = new Dialog();
        dlg.setModal(true);
        dlg.setPredefinedButtons(Dialog.PredefinedButton.CLOSE);
        dlg.addShowHandler((event) -> {
            clipboardText.focus();
        });
        //oklBt = dlg.getButton(Dialog.PredefinedButton.OK);
        // dlg.addButton(clipPasteBt);
        
         clipboardText.addValueChangeHandler((event) -> {
            oklBt.setEnabled(verify());
            dlg.getButtonBar().forceLayout();
            
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

        //clipPasteBt.addSelectHandler(pasteHandler);
        oklBt.addSelectHandler(pasteHandler);

//signBt.addSelectHandler(pasteHandler);
    }
    
    public void onClipboardRead(String text) {
        clipboardText.setText(text);
        
    }
    
    public static final native String[] split(String string, String separator) /*-{
    return string.split(separator);
}-*/;
    
    public native String pasteClipboardText() /*-{
            
            navigator.clipboard.readText().then(
                
                 function(value) {
                   this.@ru.sbsoft.client.components.browser.ClipboardForm::onClipboardRead(Ljava/lang/String;)(value);
                 }
                
                
                )
                
                
        }-*/;
    
    private boolean verify() {
        final String text = clipboardText.getText();
        
        return (text != null && !text.trim().isEmpty());
        
    }
    
    private class ParseDmp {
        
        public BigDecimal[][] parse(String text) {
            
            if (text == null || text.trim().isEmpty()) {
                return null;
            }
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
                            
                            bcols[j] = BigDecimal.valueOf(Double.valueOf(cols[j]));
                            
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

                if (value == null || (value = value.trim()).isEmpty()) {
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
