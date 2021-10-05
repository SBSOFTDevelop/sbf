package ru.sbsoft.operation;

/**
 *
 * @author Kiselev
 */
public interface IFileContainer {

    byte[] getFileBody();

    void setFileBody(byte[] body);

    String getFileName();

    void setFileName(String name);

}
