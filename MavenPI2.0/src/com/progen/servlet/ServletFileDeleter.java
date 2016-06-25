/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.servlet;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 *
 * @author arun
 */
public class ServletFileDeleter implements HttpSessionBindingListener, Serializable {

    private static final long serialVersionUID = 2276765074889675L;
    private List<String> fileNames = new java.util.ArrayList<String>();

    public void addFile(String fileName) {
        this.fileNames.add(fileName);

    }

    public boolean isChartAvailable(String filename) {
        return (this.fileNames.contains(filename));
    }

    public void valueBound(HttpSessionBindingEvent event) {
        return;
    }

    public void valueUnbound(HttpSessionBindingEvent event) {

        Iterator iter = this.fileNames.listIterator();
        while (iter.hasNext()) {
            String filename = (String) iter.next();
            File file = new File(
                    System.getProperty("java.io.tmpdir"), filename);
            if (file.exists()) {
                file.delete();
            }
        }
        return;

    }
}
