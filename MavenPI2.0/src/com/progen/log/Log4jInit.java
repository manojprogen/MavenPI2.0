/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * Document    : Log4jInit.java for Log4j intiliazation
 * Created on  : 30 Jan, 2016, 04:00 PM
 * Author      : Mohit Gupta
 * Organization: Progen Business Solution
 */
package com.progen.log;

import javax.servlet.http.HttpServlet;
import org.apache.log4j.PropertyConfigurator;

public class Log4jInit extends HttpServlet {

    public void init() {
        String prefix = getServletContext().getRealPath("/");
        String file = getInitParameter("log4j-init-file");
        if (file != null) {
            PropertyConfigurator.configure(prefix + file);
            System.out.println("Log4J Logging started: " + prefix + file);
        } else {
            System.out.println("Log4J Is not configured for your Application: " + prefix + file);
        }
        prefix = prefix.replaceAll("/", "\\\\");
        String str[] = prefix.split("\\\\");
        if (str[str.length - 1].equalsIgnoreCase("web")) {
            System.setProperty("logfilename", str[str.length - 3]);
            System.out.println("logfilename1:=========" + str[str.length - 3]);
        } else {
            System.setProperty("logfilename", str[str.length - 1]);
            System.out.println("logfilename2:=========" + str[str.length - 1]);

        }
    }
}
