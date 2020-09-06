package com.vodafone.ebuisness.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ImageValidatorTest {

    private ImageValidator imageValidator = new ImageValidator();

    @Test
    void checkIfFileIsImage_ifFileIsImageWithImageExtension() {

        Boolean isValid =  imageValidator.isImage(loadFile("src/test/resources/image" +
                " validator/pic.png"));
        Assertions.assertTrue(isValid);

    }

    @Test
    void checkIfFileIsImage_ifFileIsImageWitNonImageExtension() {

        Boolean isValid =  imageValidator.isImage(loadFile("src/test/resources/image" +
                " validator/pic.zip"));
        Assertions.assertTrue(isValid);

    }

    @Test
    void checkIfFileIsImage_ifFileIsNotImageWitNonImageExtension() {

        Boolean isValid =  imageValidator.isImage(loadFile("src/test/resources/image" +
                " validator/notImage.txt"));
        Assertions.assertFalse(isValid);

    }

    @Test
    void checkIfFileIsImage_ifFileIsNotImageWitImageExtension() {

        Boolean isValid =  imageValidator.isImage(loadFile("src/test/resources/image " +
                "validator/notImage.png"));
        Assertions.assertFalse(isValid);

    }

    private byte[] loadFile(String location) {

        File file = new File(location);
        FileInputStream fin = null;
        byte[] fileContent = null;
        try {
            // create FileInputStream object
            fin = new FileInputStream(file);

            fileContent = new byte[(int) file.length()];

            // Reads up to certain bytes of data from this input stream into an array of bytes.
            fin.read(fileContent);

        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading file " + ioe);
        } finally {
            // close the streams using close method
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
        }
        return fileContent;

    }

}