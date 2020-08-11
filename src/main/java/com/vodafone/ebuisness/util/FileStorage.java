package com.vodafone.ebuisness.util;

import java.io.IOException;
import java.nio.file.*;

public class FileStorage {

    public static void saveFile(byte[] file, String location, String fileName) throws IOException {

        Path path = Paths.get(location + fileName);
        Files.createDirectories(Paths.get(location));
        Files.write(path, file, StandardOpenOption.TRUNCATE_EXISTING);

    }

}
