package ru.sbsoft.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import ru.sbsoft.shared.model.MarkModel;

public class BeanInfo<M extends MarkModel> {

    public static <BEAN> BEAN cloneBean(BEAN source) throws CloneException {
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final ObjectOutputStream objectOutputStream;
            try {
                objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(source);
            } catch (IOException ex) {
                throw new CloneException("Cannot write to stream", ex);
            }

            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
            final ObjectInputStream objectInputStream;
            try {
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                return (BEAN) objectInputStream.readObject();
            } catch (IOException ex) {
                throw new CloneException("Cannot read from stream", ex);
            }
        } catch (ClassNotFoundException ex) {
            throw new CloneException("Cannot load data to new element", ex);
        }
    }

    public static class CloneException extends Exception {

        public CloneException() {
        }

        public CloneException(String message) {
            super(message);
        }

        public CloneException(String message, Throwable cause) {
            super(message, cause);
        }

        public CloneException(Throwable cause) {
            super(cause);
        }
    }
}
