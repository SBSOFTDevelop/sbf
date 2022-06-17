package ru.sbsoft.shared.exceptions;

import java.io.Serializable;

/**
 * @author balandin
 * @since May 16, 2013 4:58:32 PM
 */
public class StackTraceElementModel implements Serializable {

    private String declaringClass;
    private String methodName;
    private String fileName;
    private int lineNumber;

    public StackTraceElementModel() {
    }

    public StackTraceElementModel(StackTraceElement element) {
        this.declaringClass = element.getClassName();
        this.methodName = element.getMethodName();
        this.fileName = element.getFileName();
        this.lineNumber = element.getLineNumber();
    }

    public String getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(String declaringClass) {
        this.declaringClass = declaringClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public boolean isNativeMethod() {
        return lineNumber == -2;
    }

    @Override
    public String toString() {
        return getDeclaringClass() + "." + methodName
                + (isNativeMethod() ? "(Native Method)"
                : (fileName != null && lineNumber >= 0
                ? "(" + fileName + ":" + lineNumber + ")"
                : (fileName != null ? "(" + fileName + ")" : "(Unknown Source)")));
    }
}
