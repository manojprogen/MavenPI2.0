/*
 * PbMailParams.java
 *
 * Created on May 23, 2009, 12:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package prg.util;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class PbMailParams {

    /**
     * Creates a new instance of PbMailParams
     */
    public PbMailParams() {
    }
    private String hostName = null;
    private String toAddr = null;
    private String subject = null;
    private String bodyTxt = null;
    private String fileUrl = null;
    private boolean hasAttach = false;
    private boolean isZip = false;
    private boolean isAutoMail = false;
    private String bodyMessage = "";
    //added by Dinanath for header logo
    private String isHeaderLogoOn = "";
    private String isFooterLogoOn = "";
    private boolean includeURLMessage = true;

    public void setIsHeaderLogoOn(String isHeaderLogoOn) {
        this.isHeaderLogoOn = isHeaderLogoOn;
    }

    public String getIsHeaderLogoOn() {
        return this.isHeaderLogoOn;
    }

    public void setIsFooterLogoOn(String isFooterLogoOn) {
        this.isFooterLogoOn = isFooterLogoOn;
    }

    public String getIsFooterLogoOn() {
        return this.isFooterLogoOn;
    }

    public void setIncludeURLMessage(boolean includeURLMessage) {
        this.includeURLMessage = includeURLMessage;
    }

    public boolean getIncludeURLMessage() {
        return this.includeURLMessage;
    }
    //private String attachFile = null;
    private ArrayList<String> attachFile = null;

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostName() {
        return this.hostName;
    }

    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }

    public String getToAddr() {
        return this.toAddr;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setBodyText(String bodyTxt) {
        this.bodyTxt = bodyTxt;
    }

    public String getBodyText() {
        return this.bodyTxt;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setHasAttach(boolean attach) {
        this.hasAttach = attach;
    }

    public boolean getHasAttach() {
        return this.hasAttach;
    }

    public ArrayList<String> getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(ArrayList<String> attachFile) {
        this.attachFile = attachFile;
    }

    public void createdZipFile(boolean zip) {
        this.isZip = zip;
    }

    public boolean getZipFile() {
        return this.isZip;
    }

    public void setAutoMail(boolean autoMail) {
        this.isAutoMail = autoMail;
    }

    public boolean getAutoMail() {
        return this.isAutoMail;
    }
    private String filepath = null;

    public void setFilePath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilePath() {
        return this.filepath;
    }

    public void setBodyMessage(String usrMessage) {
        this.bodyMessage = usrMessage;
    }

    public String getBodyMessage() {
        return this.bodyMessage;
    }
//    public void setAttachFile(String fileName) {
//        this.attachFile = fileName;
//    }
//    public String getAttachFile() {
//        return this.attachFile;
//    }
}
