package ru.sbsoft.client.components.field;

import com.google.gwt.dom.client.Document;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import ru.sbsoft.svc.widget.core.client.event.BlurEvent;
import ru.sbsoft.svc.widget.core.client.event.FocusEvent;
import ru.sbsoft.svc.widget.core.client.form.PropertyEditor;
import ru.sbsoft.svc.widget.core.client.form.TextField;
import ru.sbsoft.svc.widget.core.client.form.Validator;
import ru.sbsoft.svc.widget.core.client.form.error.DefaultEditorError;
import ru.sbsoft.svc.widget.core.client.menu.Menu;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;
import ru.sbsoft.shared.exceptions.ApplicationException;

/**
 * Поле с маскированным вводом, маски которого основаны на регулярных
 * выражениях.
 * <p>
 * В качестве выражения могут выступать как отдельные символы, так и группы
 * символов. Например <code>[a-z]7[A-Z]</code> будет принимать
 * последовательность из трех символов, первым из которых будет строчная
 * английская буква, вторым - символ "7", а третим - прописная английская буква.
 * Кроме явного перечисления символов можно использовать предопределенные наборы
 * \d ([0-9]), \D([^0-9]), \s([ ]), \S([^ ])
 * </p>
 * <p>
 * Помимо самих групп символов можно указывать количество их вхождения. Например
 * <code>[0-9]{3}</code> принимает три цифры, a <code>[a-z]{10}</code> - десять
 * букв.
 * </p>
 * <p>
 * В одной группе символов могут быть перечислены как промежутки символов, так и
 * отдельные символы. Например, <code>[a-z135]</code>принимает строчные буквы и
 * цифры 1, 3 и 5. Для указания русских букв можно использовать набор
 * [а-яёА-ЯЁ].
 * </p>
 *
 * @author vk
 */
public class RegExpMaskField extends TextField {

    private static final RegExp MASK_PARSE_REGEXP = RegExp.compile("((\\[[^\\\\\\[\\]\\{\\}]+\\])|(\\\\[dDsS]))(\\{(\\d)\\})?", "g");

    private RegExp maskRegExp;
    private String emptyFormat;
    private String placeholderChar = "_"; //must be one character
    private List<MaskPart> maskParts;
    private int textLen;
    private boolean allowBlank = true;
    private String prechangedValue;

    public RegExpMaskField() {
        super.addFocusHandler((FocusEvent event) -> {
            prechangedValue = getText();
        });
        super.addBlurHandler((BlurEvent event) -> {
            if (!Objects.equals(prechangedValue, getText())) {
                finishEditing();
                ChangeEvent.fireNativeEvent(Document.get().createChangeEvent(), RegExpMaskField.this);
            }
        });
    }

    public void setMask(String mask) {
        if (maskRegExp != null) {
            throw new IllegalStateException("RegExpMaskField: Mask can not be changed");
        }
        mask = trimBegEnd(mask);
        if (mask.isEmpty()) {
            throw new IllegalArgumentException("RegExpMaskField: Empty mask");
        }
        try {
            maskRegExp = RegExp.compile('^' + mask + '$');
        } catch (RuntimeException ex) {
            throw new ApplicationException(I18n.get(SBFEditorStr.msgMaskNotCompile) + ": " + ex.getMessage());
        }
        maskParts = parseMask(mask);
        if (maskParts.isEmpty()) {
            throw new ApplicationException(I18n.get(SBFEditorStr.msgMaskNotEditable));
        }
        emptyFormat = getText();
        textLen = emptyFormat.length();
        addValidator(new RegExpMaskFieldValidator());

        addKeyPressHandler(ev -> {
            String ch = String.valueOf(ev.getCharCode());
            moveRight(ch);
            ev.preventDefault();
        });
        addKeyDownHandler(ev -> {
            int code = ev.getNativeKeyCode();
            if ((isOnlyControlMetaDown(ev) && KeyCodes.KEY_C == code) || (!ev.isAnyModifierKeyDown() && (KeyCodes.KEY_TAB == code || KeyCodes.KEY_ENTER == code))) {
                return;
            }
            if (!isPrintable(ev)) {
                if (!ev.isAnyModifierKeyDown()) {
                    switch (code) {
                        case KeyCodes.KEY_BACKSPACE:
                            moveLeft(true);
                            break;
                        case KeyCodes.KEY_LEFT:
                            moveLeft(false);
                            break;
                        case KeyCodes.KEY_RIGHT:
                            moveRight(null);
                            break;
                        default:
                            break;
                    }
                }
                ev.preventDefault();
            }
        });
        super.setContextMenu(new Menu());
        super.addBeforeShowContextMenuHandler(ev -> {
            Menu menu = ev.getMenu();
            if (menu != null) {
                ev.setCancelled(true);
            }
        });
        setCursorPos(0);
        getCell().setPropertyEditor(new PropertyEditor<String>() {
            @Override
            public String render(String object) {
                if (object == null || emptyFormat.equals(object) || !maskRegExp.test(object)) {
                    return emptyFormat;
                }
                return object;
            }

            @Override
            public String parse(CharSequence text) throws ParseException {
                String s = text != null ? text.toString() : null;
                if (s == null || !maskRegExp.test(s)) {
                    return null;
                }
                return s;
            }
        });
    }

    private boolean isOnlyControlMetaDown(KeyDownEvent ev) {
        return (ev.isControlKeyDown() || ev.isMetaKeyDown()) && !ev.isAltKeyDown() && !ev.isShiftKeyDown();
    }

    public char getPlaceholderChar() {
        return placeholderChar.charAt(0);
    }

    public void setPlaceholderChar(char placeholderChar) {
        char old = getPlaceholderChar();
        if (old != placeholderChar) {
            String newPh = String.valueOf(placeholderChar);
            if (maskRegExp != null) {
                boolean empty = emptyFormat.equals(getText());
                emptyFormat = emptyFormat.replace(getPlaceholderChar(), placeholderChar);
                if (empty) {
                    super.setText(emptyFormat);
                } else {
                    StringBuilder buf = new StringBuilder(getText());
                    boolean changed = false;
                    for (MaskPart p : maskParts) {
                        for (int i = p.startPos; i < p.endPos; i++) {
                            if (buf.charAt(i) == old) {
                                buf.replace(i, i + 1, newPh);
                                changed = true;
                            }
                        }
                    }
                    if (changed) {
                        super.setText(buf.toString());
                    }
                }
            }
            this.placeholderChar = newPh;
        }
    }

    @Override
    public boolean isAllowBlank() {
        return allowBlank;
    }

    @Override
    public void setAllowBlank(boolean allowBlank) {
        this.allowBlank = allowBlank;
    }

    private boolean fitMask(String value) {
        return maskRegExp == null || maskRegExp.test(value);
    }

    private void moveRight(String ch) {
        int curs = getCursorPos();
        MaskPart mp = getMaskPart(curs);
        if (mp == null) {
            mp = getNextMaskPart(curs);
            if (mp != null) {
                curs = mp.getStartPos();
                setCursorPos(curs);
            }
        }
        if (mp != null) {
            if (ch == null || mp.getCharRegExp().test(ch)) {
                if (ch != null) {
                    replaceChar(curs, ch);
                }
                if (++curs == mp.getEndPos() && curs < textLen) {
                    MaskPart nextMp = getNextMaskPart(curs);
                    if (nextMp != null) {
                        curs = nextMp.getStartPos();
                    }
                }
                setCursorPos(curs);
            }
        }
    }

    private void moveLeft(boolean delete) {
        int curs = getCursorPos();
        if (curs > 0) {
            MaskPart mp = getMaskPart(curs);
            if (mp == null || mp.getStartPos() == curs) {
                mp = getPrevMaskPart(curs);
                if (mp != null) {
                    curs = mp.getEndPos();
                    setCursorPos(curs);
                }
            }
            if (mp != null) {
                curs--;
                if (delete) {
                    replaceChar(curs, placeholderChar);
                }
                setCursorPos(curs);
            }
        }
    }

    private void replaceChar(int pos, String ch) {
        StringBuilder buf = new StringBuilder(getText());
        buf.replace(pos, pos + 1, ch);
        super.setText(buf.toString());
    }

    private boolean isPrintable(KeyDownEvent ev) {
        int keycode = ev.getNativeKeyCode();
        return !(ev.isAltKeyDown() || ev.isControlKeyDown() || ev.isMetaKeyDown())
                && ((keycode > 47 && keycode < 58) // number keys
                || keycode == 32 || keycode == 13 // spacebar & return key(s) (if you want to allow carriage returns)
                || (keycode > 64 && keycode < 91) // letter keys
                || (keycode > 95 && keycode < 112) // numpad keys
                || (keycode > 185 && keycode < 193) // ;=,-./` (in order)
                || (keycode > 218 && keycode < 223) // [\]' (in order)
                );
    }

    private MaskPart getMaskPart(int curPos) {
        for (MaskPart p : maskParts) {
            if (curPos >= p.getStartPos() && curPos < p.getEndPos()) {
                return p;
            }
        }
        return null;
    }

    private MaskPart getNextMaskPart(int curPos) {
        for (MaskPart p : maskParts) {
            if (curPos <= p.getStartPos()) {
                return p;
            }
        }
        return null;
    }

    private MaskPart getPrevMaskPart(int curPos) {
        for (int i = maskParts.size() - 1; i >= 0; i--) {
            MaskPart p = maskParts.get(i);
            if (curPos >= p.getEndPos()) {
                return p;
            }
        }
        return null;
    }

    private String trimBegEnd(String mask) {
        if (mask == null || mask.isEmpty()) {
            return "";
        }
        int start = 0;
        while (start < mask.length() && mask.charAt(start) == '^') {
            start++;
        }
        int stop = mask.length();
        while (stop > 0 && mask.charAt(stop - 1) == '$') {
            stop--;
        }
        if (start == stop) {
            return "";
        } else if (start > 0 || stop < mask.length()) {
            return mask.substring(start, stop);
        } else {
            return mask;
        }
    }

    // (\[[^\*\+\?\]]+\]|\\[\(\)dDsS]|[^\*\+\?])(\{[0-9]*,?[0-9]*\}|[\*\+\?]|) resn
    // ((\[[^\\\[\]\{\}]+\])|(\\[dDsS]))(\{(\d)\})? my
    private List<MaskPart> parseMask(String mask) {
        List<MaskPart> parts = new ArrayList<>();
        StringBuilder txt = new StringBuilder();
        int lastMaskPos = 0;
        MatchResult groups;
        while ((groups = MASK_PARSE_REGEXP.exec(mask)) != null) {
            int maskStart = groups.getIndex();
            int maskLen = groups.getGroup(0).length();
            RegExp charRegexp = RegExp.compile(groups.getGroup(1));
            String partLenStr = groups.getGroup(5);
            int partLen = partLenStr != null && !partLenStr.isEmpty() ? Integer.valueOf(partLenStr) : 1;
            if (lastMaskPos < maskStart) {
                txt.append(mask,lastMaskPos, maskStart);
            }
            parts.add(new MaskPart(txt.length(), charRegexp, partLen));
            lastMaskPos = maskStart + maskLen;
            for (int i = 0; i < partLen; i++) {
                txt.append(placeholderChar);
            }
        }
        if (lastMaskPos < mask.length()) {
            txt.append(mask.substring(lastMaskPos));
        }
        super.setText(txt.toString());
        return parts;
    }

    private static class MaskPart {

        private final int startPos;
        private final RegExp charRegExp;
        private final int endPos;

        public MaskPart(int startPos, RegExp charRegExp, int length) {
            this.startPos = startPos;
            this.charRegExp = charRegExp;
            this.endPos = startPos + length;
        }

        public int getStartPos() {
            return startPos;
        }

        public RegExp getCharRegExp() {
            return charRegExp;
        }

        public int getEndPos() {
            return endPos;
        }
    }

    private class RegExpMaskFieldValidator implements Validator<String> {

        @Override
        public List<EditorError> validate(Editor<String> editor, String value) {
            //Читаем самое актуальное значение, а то в метод передается какое-то устаревшее
            value = getText();
            if (value == null || "".equals(value)) {
                return null;
            }
            if (emptyFormat == null || emptyFormat.equals("")) {
                return null;
            }
            List<EditorError> errors = new ArrayList<>();
            if (emptyFormat.equals(value)) {
                if (!isAllowBlank()) {
                    errors.add(new DefaultEditorError(editor, I18n.get(SBFEditorStr.msgNeedToFill), value));
                    return errors;
                }
            } else if (!fitMask(value)) {
                errors.add(new DefaultEditorError(editor, I18n.get(SBFEditorStr.msgNotMatchMask), value));
                return errors;
            }
            return null;
        }
    }
}
