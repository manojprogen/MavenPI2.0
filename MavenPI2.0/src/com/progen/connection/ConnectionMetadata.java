/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.connection;

import java.util.Properties;

/**
 *
 * @author sreekanth
 */
public class ConnectionMetadata {

    private String serverName;
    private String userName;
    private String passWord;
    private String server;
    private String SID;
    private String dbType;
    private int port;
    private String dsnName;
    private String schemaName;
    private int maxActive;
    private int maxWait;
    private int connId;
    private String driverType;
    private String networkProtocol;
//    private String databaseName;
//    private int maxConnections;

    public ConnectionMetadata(Properties connProps) {
        dbType = connProps.getProperty("dbType");
        driverType = connProps.getProperty("driverType");
        networkProtocol = connProps.getProperty("networkProtocol");
        port = Integer.parseInt(connProps.getProperty("serverPort"));
        serverName = connProps.getProperty("serverName");
        dsnName = connProps.getProperty("databaseName");
        userName = connProps.getProperty("userName");
        passWord = connProps.getProperty("password");
        schemaName = connProps.getProperty("schemaName");
        maxActive = Integer.parseInt(connProps.getProperty("maxConnections"));

    }

    ConnectionMetadata(int connId, String serverName, String userName, String passWord, String server, String SID, String dbType, String schemaName, int port, String dsnName,
            int maxActive,
            int maxWait) {
        this.userName = userName;
        this.serverName = serverName;
        this.passWord = passWord;
        this.server = server;
        this.SID = SID;
        this.dbType = dbType;
        this.port = port;
        this.dsnName = dsnName;
        this.connId = connId;
        this.maxActive = maxActive;
        this.maxWait = maxWait;
        this.schemaName = schemaName;


    }

    public int getPort() {
        return this.port;
    }

    public String getServerName() {
        return this.serverName;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassWord() {
        return this.passWord;
    }

    public String getServer() {
        return this.server;
    }

    public String getSID() {
        return this.SID;
    }

    public String getDbType() {
        return this.dbType;
    }

    public String getDsnName() {
        return this.dsnName;
    }

    public String getSchemaName() {
        return this.schemaName;
    }

    public Integer getConnId() {
        return this.connId;
    }

    public int getMaxActive() {
        return this.maxActive;
    }

    public int getMaxWait() {
        return this.maxWait;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public String getNetworkProtocol() {
        return networkProtocol;
    }

    public void setNetworkProtocol(String networkProtocol) {
        this.networkProtocol = networkProtocol;
    }
}
