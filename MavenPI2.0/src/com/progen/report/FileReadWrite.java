package com.progen.report;

import com.google.gson.Gson;
import java.io.*;
import java.util.Map;
import org.apache.log4j.Logger;

public class FileReadWrite {

    public static Logger logger = Logger.getLogger(FileReadWrite.class);

    public void createDir(String path) {
        new File(path).mkdir();
    }

    public void writeToFile(String path, String data) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(file);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(data);
            bufferWritter.close();
        } catch (IOException ioe) {

            logger.error("Exception:", ioe);
        }
    }

    public void writeJson(Map map, String filePath) {
        Gson gson = new Gson();
        String content = gson.toJson(map);
        writeToFile(filePath, content);
    }

    public String loadJSON(String json_file) {
        InputStream is;
        String json = "{}";
        try {
            is = new FileInputStream(json_file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            json = new String(buffer, "ISO-8859-1");
            is.close();
        } catch (FileNotFoundException ex) {
//            logger.error("Exception:",ex);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } catch (NullPointerException ex) {
            logger.error("Exception:", ex);
        }
        return json;
    }

    public boolean deleteDirectory(File directory) {
        boolean status = false;
        if (directory.exists()) {
            for (File f : directory.listFiles()) {
                if (f.isDirectory()) {
                    f.delete();
                    deleteDirectory(f);
                } else {
                    f.delete();
                }
            }
            status = directory.delete();
        }
        return status;
    }

    public boolean deleteFile(String filePath) {
        boolean status = true;
        if (new File(filePath).exists()) {
            status = new File(filePath).delete();
        }
        return status;
    }
}