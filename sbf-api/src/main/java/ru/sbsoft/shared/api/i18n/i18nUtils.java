package ru.sbsoft.shared.api.i18n;

/**
 * Содержит общие функции по преобразованию данных локализации.
 *
 * @author Sokoloff
 */
public class i18nUtils {
    
    private i18nUtils() {
    }

    /**
     * Подставляет параметры в строку сообщения. Формат представления параметра в сообщении: {номер параметра}.
     * Нумерация начинается с 1.
     *
     * @param message
     * @param parameters
     * @return
     */
    public static String fillParameters(String message, String[] parameters) {
        if (parameters == null || message == null) {
            return message;
        }
        for (int i = 0; i < parameters.length; i++) {
            message = message.replaceAll("\\{" + (i + 1) + "\\}", parameters[i]);
        }
        return message;
    }

    public static ILocalizedString[] convertStringParams(String... params) {
        if (null == params) {
            return null;
        }
        ILocalizedString[] resultParams = new ILocalizedString[params.length];
        for (int i = 0; i < params.length; i++) {
            resultParams[i] = new NonLocalizedString(params[i]);
        }
        return resultParams;
    }
    
    public static String localizedStringToString(ILocalizedString value) {
        if (null == value) {
            return null;
        }
        if (value.getResourceInfo() != null) {
            return value.getResourceInfo().getLib().getName() + "&|&" + value.getResourceInfo().getKey();
        }
        return value.getDefaultName();
    }
    
    public static ILocalizedString stringToLocalizedString(String value) {
        if (null == value) {
            return null;
        }
        if (value.contains("&|&")) {
            String[] values = value.split("&\\|&");
            return new LocalizedString(new I18nResourceInfoObject(values[0], values[1]));
        }
        return null;
    }
    
}
