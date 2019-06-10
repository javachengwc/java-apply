package com.pseudocode.cloud.zuul.util;

import java.io.*;

public class DeepCopy {

    public static Object copy(Object orig) throws NotSerializableException {
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();
            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream( new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        } catch (NotSerializableException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }
}
