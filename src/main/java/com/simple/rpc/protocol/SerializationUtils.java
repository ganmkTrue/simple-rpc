package com.simple.rpc.protocol;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author yanhao
 * @date 2020/03/07
 * @description:
 */
public class SerializationUtils {

    public static byte[] serialize(Object object) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
        } catch (IOException ex) {
            throw new IllegalArgumentException(
                    "Failed to serialize object of type: " + object.getClass(), ex);
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (final IOException ex) {
            }
        }
        return baos.toByteArray();
    }


    public static <T> T deserialize(byte[] bytes) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            @SuppressWarnings("unchecked") final T obj = (T) ois.readObject();
            return obj;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to deserialize object", ex);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to deserialize object type", ex);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (final IOException ex) {
            }
        }
    }
}