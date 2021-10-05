package ru.sbsoft.shared.model;

/**
 *
 * @author vlki
 */
public final class SecurityHelper {

    public static String getBrowserSecurityCode(String browserCode, BrowserSecurityType securityType) {
        if (browserCode == null || (browserCode = browserCode.trim()).isEmpty()) {
            throw new IllegalArgumentException("Browser code must be set");
        }
        if (securityType == null) {
            throw new IllegalArgumentException("Security type must be set");
        }
        return new StringBuilder(browserCode.length() + securityType.name().length() + 1).append(browserCode).append('.').append(securityType.name()).toString();
    }

    public static String getOperationSecurityCode(String operationCode) {
        if (operationCode == null || (operationCode = operationCode.trim()).isEmpty()) {
            throw new IllegalArgumentException("Operation code must be set");
        }
        return "OPERATION." + operationCode;
    }

    public static String getAppSecurityCode(String applicationCode) {
        if (applicationCode == null || (applicationCode = applicationCode.trim()).isEmpty()) {
            throw new IllegalArgumentException("Application code must be set");
        }
        return "APP." + applicationCode;
    }

    private SecurityHelper() {
    }
}
