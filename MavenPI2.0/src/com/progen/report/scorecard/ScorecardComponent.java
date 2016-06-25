/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard;

import com.progen.report.scorecard.query.ScorecardQueryDetails;
import java.util.List;
import java.util.Map;

/**
 *
 * @author progen
 */
public interface ScorecardComponent {

    Score getScore();

    Iterable<ScorecardQueryDetails> getQueryDetails();

    void setScore(Score score);

    double getContribution();

    void addRule(ScoreCardMemberRule rule);

    List<ScoreCardMemberRule> getRuleList();

    Map<String, Double> getHistoricScores();

    void setHistoricScores(Map<String, Double> histScores);
}
