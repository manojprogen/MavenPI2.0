/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.log;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author arun
 */
public class ProgenLog {

    public static Level ALL = Level.ALL;
    public static Level SEVERE = Level.SEVERE;
    public static Level FINE = Level.FINE;
    public static Level FINER = Level.FINER;
    public static Level FINEST = Level.FINEST;
    public static Level INFO = Level.INFO;
    public static Level WARNING = Level.WARNING;

    public static void log(Level logLevel, Object caller, String methodName, String msg) {
        Logger logger = Logger.getLogger(caller.getClass().getName());
        LogRecord logRecord = new LogRecord(logLevel, msg);
        logRecord.setSourceClassName(caller.getClass().getName());
        logRecord.setSourceMethodName(methodName);
        logRecord.setMillis(System.currentTimeMillis());
        //logger.log(logLevel,msg);
        logger.log(logRecord);
    }
}
