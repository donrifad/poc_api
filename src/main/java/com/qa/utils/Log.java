package com.qa.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;


public class Log {
    Log() {
    }

    public Log(String s) {
    }

    public static void logException(Exception e) {
        Logger logger = Logger.getLogger("logger");
        logger.info("Error: " + e.getMessage());
    }

    public String readFromFile(String fileName, String key) throws IOException {
        FileInputStream rdr = null;
        int size = 0;
        byte[] data = null;

        try {
            rdr = new FileInputStream(fileName);
            int len = rdr.read();
            data = new byte[len + 1];
            size = rdr.read(data);
        } catch (FileNotFoundException ex) {
            logException(ex);
        } finally {
            if (rdr != null)
                rdr.close();
        }
        if (size != 0) {
            String content = new String(data, "UTF-8").trim();
            for (String s : content.split("\n")) {
                if (s.contains(key))
                    return s.split(":")[1];
            }
            return "";
        } else
            return "";
    }

    public void writeToFile(String fileName, String content) throws IOException {
        java.nio.file.Path path = Paths.get("./" + fileName);

        Files.write(path, content.getBytes(), StandardOpenOption.APPEND);
        Files.write(path, "\n".getBytes(), StandardOpenOption.APPEND);
    }

    public void cleanFile(String fileName) throws IOException {
        Files.write(Paths.get("./" + fileName), "".getBytes());
    }
}
