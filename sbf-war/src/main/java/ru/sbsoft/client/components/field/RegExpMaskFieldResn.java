package ru.sbsoft.client.components.field;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Event;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.event.BlurEvent;
import ru.sbsoft.svc.widget.core.client.event.FocusEvent;
import ru.sbsoft.svc.widget.core.client.form.TextField;
import ru.sbsoft.svc.widget.core.client.form.Validator;
import ru.sbsoft.svc.widget.core.client.form.error.DefaultEditorError;
import ru.sbsoft.client.I18n;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;
import ru.sbsoft.shared.exceptions.ApplicationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Поле с маскированным вводом, маски которого основаны на регулярных выражениях.
 * <p>
 * В качестве выражения могут выступать как отдельные символы, так и группы символов. Например <code>[a-z]7[A-Z]</code>
 * будет принимать последовательность из трех символов, первым из которых будет строчная английская буква, вторым -
 * символ "7", а третим - прописная английская буква. Кроме явного перечисления символов можно использовать
 * предопределенные наборы \d ([0-9]), \D([^0-9]), \s([ ]), \S([^ ])
 * </p>
 * <p>
 * Помимо самих групп символов можно указывать количество их вхождения. Например <code>[0-9]{3}</code> принимает три
 * цифры, a <code>[a-z]{10}</code> - десять букв.
 * </p>
 * <p>
 * Также можно указывать переменное количество вхождений группы символов. Например <code>[0-9]{4,7}</code> будет
 * принимать от четырех до семи символов. Чтобы обозначить любое количество вхождений больше заданного, нужно не писать
 * поледнюю цифру, вот так: <code>[0-9]{4,}</code>. Существуют оговоренные количества вхождений, которые для упрощения
 * обозначаются одним символом. Это:
 * <ol>
 * <li><code>*</code> = <code>{0,}</code>
 * <li><code>+</code> = <code>{1,}</code>
 * <li><code>?</code> = <code>{0,1}</code>
 * </ol>
 * </p>
 * <p>
 * В одной группе символов могут быть перечислены как промежутки символов, так и отдельные символы. Например,
 * <code>[a-z135]</code>принимает строчные буквы и цифры 1, 3 и 5. Для указания русских букв можно использовать набор
 * [а-яёА-ЯЁ].
 * </p>
 *
 * <p>
 * По умолчанию используется маска ".*", она же применяется, если пытаться установить маску "" или null.
 * </p>
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class RegExpMaskFieldResn extends TextField {

    /**
     * Если значение устанавливается программно, и оно не подходит под маску, то маска отключается.
     */
    private boolean maskDisabled = false;

    private String mask = ".*";
    private String emptyFormat;
    private String placeholder = "_";

    private String prechangedValue;
    private boolean formattingMode = false;

    public RegExpMaskFieldResn() {
        //setAllowTextSelection(false);
        addKeyPressHandler(new RegExpMaskKeyPressHandler());
        addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        setValue(getText(), false, false);
                        validate();
                    }
                });

                if (maskDisabled) {
                    maskDisabled = !isFormatted(getText());
                    if (maskDisabled) {
                        return;
                    }
                }
                int cursor = getCursorPos();
                formattingMode = true;
                reload();
                formattingMode = false;
                setCursorPos(cursor);
                //setValue(getText());
            }
        });
        addValidator(new RegExpMaskFieldValidator());

        addFocusHandler(new FocusEvent.FocusHandler() {

            @Override
            public void onFocus(FocusEvent event) {
                prechangedValue = getText();
            }
        });

        addBlurHandler(new BlurEvent.BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                if (!prechangedValue.equals(getText())) {
                    ChangeEvent.fireNativeEvent(Document.get().createChangeEvent(), RegExpMaskFieldResn.this);
                }
            }
        });

        addAttachHandler(new AttachEvent.Handler() {

            @Override
            public void onAttachOrDetach(AttachEvent event) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        reload();
                    }
                });
            }
        });

        sinkEvents(Event.ONPASTE);
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        if (mask == null || mask.equals("")) {
            mask = ".*";
        }

        try {
            RegExp.compile(mask);
        } catch (RuntimeException ex) {
            throw new ApplicationException(I18n.get(SBFEditorStr.msgMaskNotCompile) + ": " + ex.getMessage());
        }

        setText(getText().replace(placeholder, ""));

        this.mask = cleanMask(mask);
        this.emptyFormat = formatValue("", this.mask, placeholder);

        //отформатируем имеющееся значение
        //reload();
        maskDisabled = !isFormatted(getText());
        if (maskDisabled) {
            return;
        }

        setValue(getText());
        clearInvalid();
    }

    private String cleanMask(String mask) {
        if (mask.startsWith("^")) {
            mask = mask.substring(1);
        }
        if (mask.endsWith("$")) {
            mask = mask.substring(0, mask.length() - 1);
        }
        return mask;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        this.emptyFormat = formatValue("", mask, placeholder);
        //отформатируем имеющееся значение
        //reload();
        setValue(getText());
        clearInvalid();
    }

    private void reload() {
        if (maskDisabled) {
            return;
        }
        try {
            getInputEl();
        } catch (JavaScriptException ex) {
            return;
        }

        String newText = getText();
        setText("");
        //backspace в начале строки для инициализации пустой маски
        DomEvent.fireNativeEvent(Document.get().createKeyPressEvent(false, false, false, false, 8), this);

        for (int i = 0; i < newText.length(); i++) {
            int code = newText.charAt(i);
            DomEvent.fireNativeEvent(Document.get().createKeyPressEvent(false, false, false, false, code), this);
        }
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        if (event.getTypeInt() == Event.ONPASTE) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    reload();
                }
            });
        }
    }

    private static boolean isControlKey(int code) {
        final List<Integer> controls = Arrays.asList(91, 92, 93, 144, 145);
        return (code < 47 && code != 32) || (code >= 91 && code <= 93) || (code >= 112 && code <= 145) || controls.contains(code);
    }

    //<editor-fold defaultstate="collapsed" desc="formatValue">
    //форматирует строку в соответствии с маской (добавляя необходимые плейсхолдеры)
    private static String formatValue(String value, String mask, String placeholder) {
        RegExp m = RegExp.compile("^" + mask + "$");
        if (m.test(value)) {
            return value;
        }

        int readCursor = 0;
        String result = "";

        RegExp maskRegExp = RegExp.compile("(\\[[^\\*\\+\\?\\]]+\\]|\\\\[\\(\\)dDsS]|[^\\*\\+\\?])(\\{[0-9]*,?[0-9]*\\}|[\\*\\+\\?]|)", "g");

        //Проверяем, можно ли вводить символ. Для этого проверим строку от начала до курсора.
        MatchResult groups;
        while ((groups = maskRegExp.exec(mask)) != null) {
            String groupAsIs = groups.getGroup(0);
            String ruleString = parseRule(groups.getGroup(1));
            String checkingCountString = groups.getGroup(2);

            if (checkingCountString.equals("")) {
                checkingCountString = "{1,1}";
            } else if (checkingCountString.equals("?")) {
                checkingCountString = "{0,1}";
            } else if (checkingCountString.equals("+")) {
                checkingCountString = "{1,}";
            } else if (checkingCountString.equals("*")) {
                checkingCountString = "{0,}";
            }

            MatchResult counts = RegExp.compile("\\{([0-9]*),?([0-9]*)\\}", "g").exec(checkingCountString);
            int minCount = counts.getGroup(1) == null ? 0 : Integer.parseInt(counts.getGroup(1));
            int maxCount = counts.getGroup(2) == null ? 0 : counts.getGroup(2).equals("") ? minCount : Integer.parseInt(counts.getGroup(2));

            String checkingPart = readCursor >= value.length() ? "" : value.substring(readCursor);
            //добавим подчеркивание, если выражение - набор символов.
            String groupCheckString = ruleString.startsWith("[") && !ruleString.startsWith("[^") && ruleString.endsWith("]") ? ruleString.substring(0, ruleString.length() - 1) + placeholder + "]{1," + maxCount + "}" : ruleString;
            groupCheckString += "$";
            RegExp groupCheckRegExp = RegExp.compile("^" + groupCheckString);

            while (checkingPart.length() > 0 && !groupCheckRegExp.test(checkingPart)) {
                checkingPart = checkingPart.substring(0, checkingPart.length() - 1);
            }

            readCursor += checkingPart.length();

            for (int i = checkingPart.length(); i < minCount; i++) {
                checkingPart += placeholder;
            }

            if (minCount == maxCount && minCount == 1) {
                //значение выражения постоянно, placeholder не нужен
                result += ruleString.startsWith("\\") ? ruleString.substring(1) : ruleString;
            } else {
                result += checkingPart;
            }
        }

        return result;
    }
    //</editor-fold>

    /**
     * Валидатор
     */
    private class RegExpMaskFieldValidator implements Validator<String> {

        @Override
        public List<EditorError> validate(Editor<String> editor, String value) {
            //Читаем самое актуальное значение, а то в метод передается какое-то устаревшее
            value = getText();
            if (value == null || "".equals(value)) {
                return null;
            }

            if (mask == null) {
                return null;
            }

            if (emptyFormat == null || emptyFormat.equals("")) {
                return null;
            }

            List<EditorError> errors = new ArrayList<EditorError>();
            if (emptyFormat.equals(value)) {
                if (isAllowBlank()) {
                    return null;
                } else {
                    errors.add(new DefaultEditorError(editor, I18n.get(SBFEditorStr.msgNeedToFill), value));
                    return errors;
                }
            }

            RegExp maskRegExp = RegExp.compile("^" + mask + "$");
            if (!maskRegExp.test(value)) {
                errors.add(new DefaultEditorError(editor, I18n.get(SBFEditorStr.msgNotMatchMask), value));
                return errors;
            }

            final String checkingValue = Strings.clean(value, false);
            final String[] childErrors = getErrors(checkingValue);
            if (childErrors == null) {
                return null;
            }

            for (String e : childErrors) {
                errors.add(new DefaultEditorError(editor, e, value));
            }
            return errors;
        }
    }

    protected String[] getErrors(String checkingValue) {
        return null;
    }

    private static String parseRule(String ruleString) {
        if (ruleString == null) {
            return null;
        }
        if (ruleString.equals("\\d")) {
            return "[0-9]";
        }
        if (ruleString.equals("\\D")) {
            return "[^0-9]";
        }
        if (ruleString.equals("\\s")) {
            return "[ ]";
        }
        if (ruleString.equals("\\S")) {
            return "[^ ]";
        }
//            if (ruleString.equals(".")) {
//                return "[a-zA-Zа-яёА-ЯЁ0-9\\\\ \\(\\)\\[\\]\\.\\,\\-]";
//            }

        return ruleString;
    }

    /**
     * Обработчик маскированного ввода.
     */
    //<editor-fold defaultstate="collapsed" desc="RmaskKeyDownHandler">
    private class RegExpMaskKeyPressHandler implements KeyPressHandler {

        @Override
        public void onKeyPress(KeyPressEvent event) {
            if (mask == null || maskDisabled) {
                return;
            }

            int code = event.getUnicodeCharCode();
            boolean isControl = false;

            String insertChar = String.valueOf((char) code);
            if (code == 0 || event.isAltKeyDown() || event.isControlKeyDown()) {
                code = event.getNativeEvent().getKeyCode();
                isControl = isControlKey(code) || event.isAltKeyDown() || event.isControlKeyDown();
                insertChar = String.valueOf((char) code);
            }

            int cursor = getCursorPos();

            boolean isBackspace = code == 8;

            if (isControl && !isBackspace) {
                return;
            }

            String oldValue = getText();
            if (oldValue != null) {
                cursor = Math.min(cursor, oldValue.length());
            }
            String checkingValue = oldValue.substring(0, cursor) + (isControl ? "" : insertChar);
            String newValue = oldValue;

            AnalyseResult result = analyseKeyAction(mask, placeholder, checkingValue, oldValue);

            if (result.allowedKey || isBackspace) {
                if (!isBackspace) {
                    newValue = checkingValue + newValue.substring(cursor + (result.overrides ? 1 : 0));
                } else {
                    newValue = newValue.substring(0, Math.max(0, cursor - 1)) + result.backspaceReplace + newValue.substring(cursor);
                }
            } else if (formattingMode) {
                int oldCursor = cursor;
                while (!result.allowedKey && checkingValue.length() <= oldValue.length() + 1) {
                    checkingValue = checkingValue.substring(0, cursor) + placeholder + insertChar;
                    cursor++;
                    result = analyseKeyAction(mask, placeholder, checkingValue, oldValue);
                }
                newValue = checkingValue + newValue.substring(oldCursor);
            }

            //делаем все за браузер и убиваем событие.
            String formatValue = formatValue(newValue, mask, placeholder);
            setText(formatValue);

            int newCursorPosition = Math.min(cursor + result.cursorOffset, formatValue.length());
            if (formattingMode && result.cursorOffset >= 2) {
                newCursorPosition = Math.min(cursor + 1, formatValue.length());
            }
            if (isBackspace) {
                newCursorPosition = Math.max(0, cursor - 1);
            }
            setCursorPos(newCursorPosition);
            event.preventDefault();

        }

    }
    //</editor-fold>

    @Override
    public void setValue(String value, boolean fireEvents, boolean redraw) {
        if (value != null) {
            value = value.replace(placeholder, "");
            value = Strings.clean(value);
        }
        super.setValue(value, fireEvents, redraw);
        maskDisabled = !isFormatted(value);
    }

    @Override
    public String getValue() {
        String v = super.getValue();
        if (Strings.equals(v, emptyFormat)) {
            v = null;
        }
        return v;
    }

    @Override
    public String getText() {
        return super.getText(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected XElement getInputEl() {
        return super.getInputEl(); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isFormatted(String value) {
        if (mask == null || value == null || value.equals("")) {
            return true;
        }
        return value.matches("^" + mask + "$");

    }

    //<editor-fold defaultstate="collapsed" desc="analyseKeyAction">
    private static AnalyseResult analyseKeyAction(String mask, String placeholder, String checkingValue, String oldValue) {
        AnalyseResult result = new AnalyseResult();
        result.allowedKey = false;
        result.overrides = false;
        result.oldValueValid = false;
        result.cursorOffset = 0;
        result.backspaceReplace = "";

        //RegExp maskRegExp = RegExp.compile("(\\[[^\\*\\+\\?\\]]+\\]|[^\\*\\+\\?\\\\]|\\\\\\(|\\\\\\))(\\{[0-9]*,?[0-9]*\\}|[\\*\\+\\?]|)", "g");
        RegExp maskRegExp = RegExp.compile("(\\[[^\\*\\+\\?\\]]+\\]|\\\\[\\(\\)dDsS]|[^\\*\\+\\?])(\\{[0-9]*,?[0-9]*\\}|[\\*\\+\\?]|)", "g");

        //Собирает выражение по итерациям с начала строки до проверяемой группы
        String checkingMask = "^";
        //Проверяем, можно ли вводить символ. Для этого проверим строку от начала до курсора.
        MatchResult groups;

        int readCursor = 0;

        while ((groups = maskRegExp.exec(mask)) != null) {
            //String groupAsIs = groups.getGroup(0);
            String ruleString = parseRule(groups.getGroup(1));
            String checkingCountString = groups.getGroup(2);

            //расшифровываем группы регулярных выражений
            if (checkingCountString.equals("")) {
                checkingCountString = "{1,1}";
            } else if (checkingCountString.equals("?")) {
                checkingCountString = "{0,1}";
            } else if (checkingCountString.equals("+")) {
                checkingCountString = "{1,}";
            } else if (checkingCountString.equals("*")) {
                checkingCountString = "{0,}";
            }

            //регулярное выражение для получения максимального и минимального количества вхождений
            //принимает строку формата {x,y}, где x-минимальное кол-во вхождений, y-максимальное.
            MatchResult counts = RegExp.compile("\\{([0-9]*),?([0-9]*)\\}", "g").exec(checkingCountString);
            Integer minCount = counts.getGroup(1) == null ? 0 : Integer.parseInt(counts.getGroup(1));
            Integer maxCount;
            if (checkingCountString.matches("\\{\\d+\\}")) {
                maxCount = minCount;
            } else if (checkingCountString.matches("\\{\\d+,\\}")) {
                maxCount = null;
            } else {
                maxCount = Integer.parseInt(counts.getGroup(2));
            }

            String maxCountString = (maxCount == null) ? "" : "" + maxCount;

            //регулярное выражение для текущей проверяемой группы (дополнительно с включением символа
            //подчеркивания), если группа принимает переменные значения
            String templateRule = ruleString.startsWith("[") && !ruleString.startsWith("[^") && ruleString.endsWith("]") ? ruleString.substring(0, ruleString.length() - 1) + placeholder + "]" : ruleString;

            if (!result.allowedKey) {
                //проверяем возможность ввода
                RegExp iterationRegExp = RegExp.compile(checkingMask + templateRule + "{1," + maxCountString + "}$");
                if (iterationRegExp.test(oldValue)) {
                    result.oldValueValid = true;
                }
                if (iterationRegExp.test(checkingValue)) {
                    result.allowedKey = true;
                    result.cursorOffset++;

                    if (oldValue.length() >= checkingValue.length()) {
                        //проверим, надо ли перезаписывать очередной символ
                        String sameLengthOldValue = oldValue.substring(0, checkingValue.length());
                        String formatGroupMask = templateRule + "{1," + maxCountString + "}";
                        RegExp oldValueRegExp = RegExp.compile(checkingMask + formatGroupMask + "$");
                        if (oldValueRegExp.test(sameLengthOldValue)) {
                            result.overrides = true;
                        }
                    }

                    //Заменять backspace by placeholder?
                    if (maxCount != null) {
                        RegExp groupRegExp = RegExp.compile(checkingMask + templateRule + "{" + (minCount + 1) + "," + Math.max(minCount + 1, maxCount) + "}$");
                        if (!groupRegExp.test(checkingValue)) {
                            result.backspaceReplace = maxCount.equals(minCount) && minCount == 1 ? ruleString : placeholder;
                        }

                        //имеет ли смысл пытаться переводить курсор через постоянные выражения? (имеет смысл только если группа заполнена полностью)
                        RegExp fullGroupRegExp = RegExp.compile(checkingMask + ruleString + "{" + maxCount + "," + maxCount + "}$");
                        if (!fullGroupRegExp.test(checkingValue)) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            } else if (minCount == 1 && minCount.equals(maxCount)) {
                //выполняется после проверки ввода.
                //
                //пробросим курсор через заполненные поля
                result.cursorOffset++;
            } else {
                break;
            }

            checkingMask += templateRule + "{" + minCount + "," + maxCount + "}";
        }

        return result;
    }
    //</editor-fold>

    /**
     * Содержит результаты анализа текущего ввода.
     */
    private static class AnalyseResult {

        //проверяемое значение соответствует маске
        boolean oldValueValid;
        //можно ли вводить этот символ?
        boolean allowedKey;
        //переписывает ли символ текущее значение?
        boolean overrides;
        //смещение курсора (в случае, если символ вставляется, либо если происходит удаление)
        int cursorOffset;
        //символ, вставляемый вместо удаляемого backspace'ом
        String backspaceReplace;
    }

}
