/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.timesetup;

import java.util.Calendar;

/**
 *
 * @author progen
 */
public abstract class PriorNextHelper {

    public abstract String[] findPrior(Calendar startCalendar, Calendar endCalendar);

    public abstract String[] findNext(Calendar startCalendar, Calendar endCalendar);
}
