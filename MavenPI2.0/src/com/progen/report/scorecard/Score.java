/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard;

/**
 *
 * @author progen
 */
public interface Score {

    Double getContribution();

    Double getCurrentScore();

    Double getPriorScore();

    String getLightIcon();

    Double getTargetScore();

    Double getTargetDev();

    Double getTargetDevPercent();
}
