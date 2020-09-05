package com.vodafone.ebuisness.util;

import javax.activation.MimetypesFileTypeMap;
import java.awt.*;
import java.io.*;
import java.net.URLConnection;

public class ImageValidator {

    public static Boolean isImage(byte[] bytes) {

        File file = new File("../temp");

        InputStream is = new BufferedInputStream(new ByteArrayInputStream(bytes));
        String mimeType = null;
        try {
            mimeType = URLConnection.guessContentTypeFromStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mimeType = mimeType == null ? "no mime" : mimeType;

        String type = mimeType.split("/")[0];
        if (type.equals("image")) {
            return true;
        } else {
            return false;
        }
    }

}



