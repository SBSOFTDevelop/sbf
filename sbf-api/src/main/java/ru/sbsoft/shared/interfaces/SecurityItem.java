package ru.sbsoft.shared.interfaces;

/**
 * Объект, имеющий собственный код безопасности для поступа к нему
 * @author Kiselev
 */
public interface SecurityItem {
    String PERMIT_ALL_READ_SECURITY_ID = "PERMIT_ALL_READ_SECURITY_ID";
    String PERMIT_ALL_WRITE_SECURITY_ID = "PERMIT_ALL_WRITE_SECURITY_ID";
    
    String getSecurityId();
}
