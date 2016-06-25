/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.userlayer.db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class LogReadWriter {

    public static String file_name = "";

    public void fileWriter(String content) throws IOException {
        File file;
        String str = System.getProperty("java.io.tmpdir") + "/" + file_name + ".txt";
//       file = new File("c:/" + file_name + ".txt");
        file = new File(str);
//       String dir = System.getProperty("java.io.tmpdir") + "/";       
//       file = new File(dir + file_name + ".txt");
        //the true will append the new data
        try {

            FileWriter fstream = new FileWriter(file, true);
            BufferedWriter fbw = new BufferedWriter(fstream);
            fbw.write(content);
            fbw.newLine();
            fbw.close();
        } catch (Exception e) {
        }
    }

    public void setLogFileName(String file_name) {
        Format formatter = new SimpleDateFormat("dd-MMM");
        Date date = new Date();
        String s = formatter.format(date);
        this.file_name = file_name + s;
    }

    public String getLogFileName() {
        return this.file_name;
    }
//added by mohit for dynamic file

    public void fileWriterWithFileName(String content, String file_name) throws IOException {
        File file;
        String str = System.getProperty("java.io.tmpdir") + "/" + file_name + ".txt";
//       file = new File("c:/" + file_name + ".txt");
        file = new File(str);
//       String dir = System.getProperty("java.io.tmpdir") + "/";
//       file = new File(dir + file_name + ".txt");
        //the true will append the new data
        try {

            FileWriter fstream = new FileWriter(file, true);
            BufferedWriter fbw = new BufferedWriter(fstream);
            fbw.write(content);
            fbw.newLine();
            fbw.close();
        } catch (Exception e) {
        }
    }
}
