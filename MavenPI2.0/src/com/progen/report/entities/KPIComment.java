/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author progen
 */
public class KPIComment implements Serializable {

    private String userId;
    private String elementId;
    private String comment;
    private String userName;
    private Date commentDate;
    private static final long serialVersionUID = 7526471155622L;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
