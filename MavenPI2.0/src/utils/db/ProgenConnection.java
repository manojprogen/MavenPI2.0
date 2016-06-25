/*
 * ProgenConnection.java
 *
 * Created on April 20, 2008, 8:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package utils.db;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.progen.connection.ConnectionDAO;
import com.progen.connection.ConnectionMetadata;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import javax.sql.ConnectionPoolDataSource;
import net.sourceforge.jtds.jdbcx.JtdsDataSource;
import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import org.apache.log4j.Logger;
import org.apache.tomcat.dbcp.dbcp.ConnectionFactory;
import org.apache.tomcat.dbcp.dbcp.DriverManagerConnectionFactory;
import org.apache.tomcat.dbcp.dbcp.PoolableConnectionFactory;
import org.apache.tomcat.dbcp.dbcp.PoolingDataSource;
import org.apache.tomcat.dbcp.pool.impl.GenericObjectPool;

/**
 *
 * @author achandak
 */
public class ProgenConnection {

    public static Logger logger = Logger.getLogger(ProgenConnection.class);
    private static volatile ProgenConnection uniqueInstance;
    private int maxConnections = 100;     // number of connections
    private volatile JDBCConnectionPool pool;
    private volatile HashMap externalConnectionPoolsMap;
    private volatile JDBCConnectionPool externalConnectionPool;
    private volatile HashMap<Integer, PoolingDataSource> extConnectionPool;
    public static final String SQL_SERVER = "SqlServer";
    public static final String ORACLE = "oracle";
    public static final String MYSQL = "mysql";
    private static volatile ConnectionMetadata metadataConn = null;
    private String dbType;// = null;
//    private GenericObjectPool genericPool;
//    private int connCount;

    public static synchronized void setConnectionMetadata(ConnectionMetadata metadataConn) {
        ProgenConnection.metadataConn = metadataConn;
    }

    public static ProgenConnection getInstance() {
        if (uniqueInstance == null) {
            synchronized (ProgenConnection.class) {
                if (uniqueInstance == null) {
                    try {
//                        ProgenLog.log(ProgenLog.FINE,ProgenConnection.class, "getInstance", "Create a ProgenConnection");
                        logger.info("Create a ProgenConnection");
                        uniqueInstance = new ProgenConnection();
                    } catch (SQLException ex) {
//                        ProgenLog.log(ProgenLog.SEVERE,ProgenConnection.class, "getInstance", "Exception "+ex.getMessage());
                        logger.error("Exception:", ex);
                        uniqueInstance = null;
                    } catch (PGConnectionException ex) {
//                        ProgenLog.log(ProgenLog.SEVERE,ProgenConnection.class, "getInstance", "Exception "+ex.getMessage());
                        logger.error("Exception:", ex);
                        uniqueInstance = null;
                    }
                }
            }

        }
//        ////.println("uq"+uniqueInstance);
        return uniqueInstance;
    }

    /**
     * Creates a new instance of ProgenConnection
     */
    private ProgenConnection() throws SQLException, PGConnectionException {
        if (pool == null) {

            ConnectionPoolDataSource dataSource = null;
            if (metadataConn != null) {
                this.dbType = metadataConn.getDbType();
                this.maxConnections = metadataConn.getMaxActive();

                if (this.dbType.equalsIgnoreCase(ProgenConnection.ORACLE)) {
                    dataSource = new OracleConnectionPoolDataSource();
                    ((OracleConnectionPoolDataSource) dataSource).setNetworkProtocol(metadataConn.getNetworkProtocol());
                    ((OracleConnectionPoolDataSource) dataSource).setDriverType(metadataConn.getDriverType());
                    ((OracleConnectionPoolDataSource) dataSource).setPortNumber(metadataConn.getPort());
                    ((OracleConnectionPoolDataSource) dataSource).setServerName(metadataConn.getServerName());
                    ((OracleConnectionPoolDataSource) dataSource).setDatabaseName(metadataConn.getDsnName());
                    ((OracleConnectionPoolDataSource) dataSource).setUser(metadataConn.getUserName());
                    ((OracleConnectionPoolDataSource) dataSource).setPassword(metadataConn.getPassWord());
                } else if (this.dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                    dataSource = new JtdsDataSource();
                    ((JtdsDataSource) dataSource).setPortNumber(metadataConn.getPort());
                    ((JtdsDataSource) dataSource).setServerName(metadataConn.getServerName());
                    ((JtdsDataSource) dataSource).setDatabaseName(metadataConn.getDsnName());
                    ((JtdsDataSource) dataSource).setUser(metadataConn.getUserName());
                    ((JtdsDataSource) dataSource).setPassword(metadataConn.getPassWord());
                } else if (this.dbType.equalsIgnoreCase(ProgenConnection.MYSQL)) {
                    dataSource = new MysqlConnectionPoolDataSource();
                    ((MysqlConnectionPoolDataSource) dataSource).setPortNumber(metadataConn.getPort());
                    ((MysqlConnectionPoolDataSource) dataSource).setServerName(metadataConn.getServerName());
                    ((MysqlConnectionPoolDataSource) dataSource).setDatabaseName(metadataConn.getDsnName());
                    ((MysqlConnectionPoolDataSource) dataSource).setUser(metadataConn.getUserName());
                    ((MysqlConnectionPoolDataSource) dataSource).setPassword(metadataConn.getPassWord());


                }
                if (dataSource != null) {
                    pool = new JDBCConnectionPool(dataSource, maxConnections);
                } else {
                    throw new PGConnectionException();
                }

            } else {
                this.dbType = ProgenConnection.ORACLE;
                dataSource = new OracleConnectionPoolDataSource();
                ((OracleConnectionPoolDataSource) dataSource).setDriverType("thin");
                ((OracleConnectionPoolDataSource) dataSource).setNetworkProtocol("tcp");
                ((OracleConnectionPoolDataSource) dataSource).setPortNumber(1521);
                ((OracleConnectionPoolDataSource) dataSource).setServerName("192.168.0.111");
                ((OracleConnectionPoolDataSource) dataSource).setDatabaseName("XE");
                ((OracleConnectionPoolDataSource) dataSource).setUser("metaminda");
                ((OracleConnectionPoolDataSource) dataSource).setPassword("metaminda");

//            JtdsDataSource dataSource = new JtdsDataSource();
//
//            dataSource.setPortNumber(1433);
//            dataSource.setServerName("192.168.0.102");
//            dataSource.setDatabaseName("metadata1");
//            dataSource.setUser("sa");
//            dataSource.setPassword("welcome");
//
//            this.dbType = ProgenConnection.SQL_SERVER;
                //
                pool = new JDBCConnectionPool(dataSource, maxConnections);
//                ProgenLog.log(ProgenLog.FINE,ProgenConnection.class, "Constructor", "Create a Metadata Pool");
                logger.info("Create a Metadata Pool");
            }
        }

    }

    private ProgenConnection(String driverType, String networkProtocol, String serverName, int portNumber, String dataBaseName, String user, String password, String connectionId) throws SQLException {
        if (externalConnectionPoolsMap == null) {
            externalConnectionPoolsMap = new HashMap();
            OracleConnectionPoolDataSource dataSource = new OracleConnectionPoolDataSource();
            dataSource.setDriverType(driverType);
            dataSource.setNetworkProtocol(networkProtocol);
            dataSource.setServerName(serverName);
            dataSource.setPortNumber(portNumber);
            dataSource.setDatabaseName(dataBaseName);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            externalConnectionPool = new JDBCConnectionPool(dataSource, maxConnections);
            externalConnectionPoolsMap.put(connectionId, externalConnectionPool);
        } else if (externalConnectionPoolsMap.get(connectionId) == null) {

            OracleConnectionPoolDataSource dataSource = new OracleConnectionPoolDataSource();
            dataSource.setDriverType(driverType);
            dataSource.setNetworkProtocol(networkProtocol);
            dataSource.setServerName(serverName);
            dataSource.setPortNumber(portNumber);
            dataSource.setDatabaseName(dataBaseName);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            externalConnectionPool = new JDBCConnectionPool(dataSource, maxConnections);
            externalConnectionPoolsMap.put(connectionId, externalConnectionPool);
        }
    }

    public String getDatabaseType() {
        return this.dbType;
    }

    public Connection getCustomerConn() {
        try {
//            ProgenConnection pg = new ProgenConnection("thin", "tcp", "localhost", 1521, "XE", "dataminda", "dataminda", "customer");
            ProgenConnection pg = new ProgenConnection("thin", "tcp", "192.168.0.101", 1521, "XE", "dataminda", "dataminda ", "customer");
//            ProgenConnection pg = new ProgenConnection("thin", "tcp", "localhost", 1521, "XE","prprogenscenario", "prprogenscenario", "customer");
//           ProgenConnection pg = new ProgenConnection("thin", "tcp", "localhost", 1521, "ORCL", "datamerit", "datamerit", "customer");
//            ProgenConnection pg = new ProgenConnection("thin", "tcp", "localhost", 1521, "XE", "datamenda", "datamenda", "customer");

            return pg.getExternalConnection("customer");
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
            return null;
        }
    }

    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    public Connection getExternalConnection(String connectionId) throws SQLException {
        return ((JDBCConnectionPool) externalConnectionPoolsMap.get(connectionId)).getConnection();
    }

    public int getconncount() throws SQLException {
        pool.dispose();
        return pool.getActiveConnections();
    }

    public static Connection postgresqlConnection(String server, String port, String databasename, String username, String password) {


        ////////.println("-------- PostgreSQL JDBC Connection Testing ------------");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            ////////.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
            logger.error("Exception:", e);
            return null;
        }
        ////////.println("PostgreSQL JDBC Driver Registered!");
        Connection connection = null;
        try {
            ////////.println("server"+server);
            ////////.println("port"+port);
            ////////.println("databasename"+databasename);
            ////////.println("username"+username);
            ////////.println("password"+password);
            connection = DriverManager.getConnection("jdbc:postgresql://" + server + ":" + port + "/" + databasename, username, password);
            ////////.println("connection===" + connection);
        } catch (SQLException e) {
            ////////.println("Connection Failed! Check output console");
            logger.error("Exception:", e);
            return null;
        }
        if (connection != null) {
            ////////.println("You made it, take control your database now!");
            return connection;
        } else {
            ////////.println("Failed to make connection!");
            return null;
        }
    }

    /*
     * public Connection getBugConn() { try { // ProgenConnection pg = new
     * ProgenConnection("thin", "tcp", "localhost", 1521, "ORCL", "progenbug",
     * "progenbug", "bug"); //ProgenConnection pg = new ProgenConnection("thin",
     * "tcp", "localhost", 1521, "XE", "progenbug", "progenbug", "bug");
     * ProgenConnection pg = new ProgenConnection("thin", "tcp", "localhost",
     * 1521, "XE", "dataindicus", "dataindicus", "customer"); //ProgenConnection
     * pg = new ProgenConnection("thin", "tcp", "122.169.241.230", 1521, "ORCL",
     * "progenbug", "progenbug", "bug"); return pg.getExternalConnection("bug");
     * } catch (SQLException ex) { logger.error("Exception:",ex); return null; }
     * }
     *
     * public static Connection postgresqlConnection(String server, String port,
     * String databasename, String username, String password) {
     *
     *
     * ////////.println("-------- PostgreSQL JDBC Connection Testing
     * ------------"); try { Class.forName("org.postgresql.Driver"); } catch
     * (ClassNotFoundException e) { ////////.println("Where is your PostgreSQL
     * JDBC Driver? Include in your library path!");
     * logger.error("Exception:",e); return null; } ////////.println("PostgreSQL
     * JDBC Driver Registered!"); Connection connection = null; try {
     * ////////.println("server"+server); ////////.println("port"+port);
     * ////////.println("databasename"+databasename);
     * ////////.println("username"+username);
     * ////////.println("password"+password); connection =
     * DriverManager.getConnection("jdbc:postgresql://" + server + ":" + port +
     * "/" + databasename, username, password); ////////.println("connection==="
     * + connection); } catch (SQLException e) { ////////.println("Connection
     * Failed! Check output console"); logger.error("Exception:",e); return
     * null; } if (connection != null) { ////////.println("You made it, take
     * control your database now!"); return connection; } else {
     * ////////.println("Failed to make connection!"); return null; } }
     *
     * public Connection getmySQLConnection() { Connection connection = null;
     * try { Class.forName("com.mysql.jdbc.Driver"); connection =
     * DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root",
     * ""); } catch (Exception e) { logger.error("Exception:",e); } return
     * connection; }
     *
     * public Connection getmySQLConnection(String dbname, String server, String
     * port, String username, String password) { Connection connection = null;
     * try { Class.forName("com.mysql.jdbc.Driver"); connection =
     * DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" +
     * dbname, username, password); } catch (Exception e) {
     * logger.error("Exception:",e); } return connection; }
     *
     * public Connection getCustomerConn1() { try { //ProgenConnection pg = new
     * ProgenConnection("thin", "tcp", "192.168.0.107", 1521, "ORCL",
     * "dataindicus", "dataindicus", "customer"); ProgenConnection pg = new
     * ProgenConnection("thin", "tcp", "localhost", 1521, "XE", "prprogen",
     * "prprogen", "customer"); //ProgenConnection pg = new
     * ProgenConnection("thin", "tcp", "122.169.241.230", 1521, "ORCL",
     * "dataspjain", "dataspjain", "customer"); return
     * pg.getExternalConnection("customer"); } catch (SQLException ex) {
     * logger.error("Exception:",ex); return null; }
    }
     */
    public Connection getConnectionForElement(String element) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        ConnectionMetadata conMetadata;
        conMetadata = connectionDAO.getConnectionForElement(element);
        return getConnection(conMetadata);
    }

    public Connection getConnectionByTable(String tabID) {

        ConnectionDAO connectionDAO = new ConnectionDAO();
        ConnectionMetadata conMetadata;
        conMetadata = connectionDAO.getConnectionByTable(tabID);
        return getConnection(conMetadata);
    }

    public Connection getConnectionByGroupId(String grpID) {

        ConnectionDAO connectionDAO = new ConnectionDAO();
        ConnectionMetadata conMetadata;
        conMetadata = connectionDAO.getConnectionByGroupId(grpID);
        return getConnection(conMetadata);
    }

    public Connection getConnectionByConId(String connId) {

        ConnectionDAO connectionDAO = new ConnectionDAO();
        ConnectionMetadata conMetadata;
        conMetadata = connectionDAO.getConnectionByConId(connId);
        return getConnection(conMetadata);
    }

    public Connection getConnectionByConIdToPostgres(String connId) {
        ConnectionDAO connectionDAO = new ConnectionDAO();
        ConnectionMetadata conMetadata;
        conMetadata = connectionDAO.getConnectionByConId(connId);
        return ProgenConnection.postgresqlConnection(conMetadata.getServer(), Integer.toString(conMetadata.getPort()), conMetadata.getSchemaName(), conMetadata.getUserName(), conMetadata.getPassWord());
    }

    public Connection getConnectionByTableToPostgres(String tabID) {

        ConnectionDAO connectionDAO = new ConnectionDAO();
        ConnectionMetadata conMetadata;
        conMetadata = connectionDAO.getConnectionByTable(tabID);
        return ProgenConnection.postgresqlConnection(conMetadata.getServer(), Integer.toString(conMetadata.getPort()), conMetadata.getSchemaName(), conMetadata.getUserName(), conMetadata.getPassWord());

    }

    /*
     * private Connection getConnection(ConnectionMetadata conMetadata) {
     * Connection connection = null; String schemaName = ""; String serverName =
     * ""; int serverPort = 00; String serviceId = ""; String userName = "";
     * String password = ""; String server = ""; String dsnName = ""; String
     * connType = ""; ProgenLog.log(ProgenLog.FINE,this, "getConnection", "Enter
     * "+conMetadata.getConnId());
     *
     * try { serverName = conMetadata.getServerName(); serverPort =
     * conMetadata.getPort(); serviceId = conMetadata.getSID(); userName =
     * conMetadata.getUserName(); password = conMetadata.getPassWord(); server =
     * conMetadata.getServer(); dsnName = conMetadata.getDsnName(); connType =
     * conMetadata.getDbType(); schemaName = conMetadata.getSchemaName();
     *
     * if (connType != null && connType.equalsIgnoreCase("oracle")) {
     * Class.forName("oracle.jdbc.driver.OracleDriver"); connection =
     * DriverManager.getConnection("jdbc:oracle:thin:@" + server + ":" +
     * serverPort + ":" + serviceId + "", userName, password); } else if
     * (connType != null && connType.equalsIgnoreCase("mysql")) {
     * Class.forName("com.mysql.jdbc.Driver");
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("jdbc:mysql://"
     * + server + ":" + serverPort + "/" + dbName + "," + userName + "," +
     * password); connection = DriverManager.getConnection("jdbc:mysql://" +
     * server + ":" + serverPort + "/" + schemaName, userName, password); } else
     * if (connType != null &&
     * connType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) { // String
     * driver = "net.sourceforge.jtds.jdbc.Driver";
     * Class.forName("net.sourceforge.jtds.jdbc.Driver"); String url=
     * "jdbc:jtds:sqlserver://"+server+":"+serverPort+"/"+schemaName; connection
     * = DriverManager.getConnection(url,userName , password); } else if
     * (connType != null && connType.equalsIgnoreCase("EXCEL")) {
     * Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); connection =
     * DriverManager.getConnection("jdbc:odbc:" + dsnName + ""); }else
     * if(connType != null && connType.equalsIgnoreCase("PostgreSQL")){
     * Class.forName("org.postgresql.Driver"); connection =
     * DriverManager.getConnection("jdbc:postgresql://" + server + ":" +
     * serverPort + "/" + schemaName, userName, password); }
     * ProgenLog.log(ProgenLog.FINE,this, "getConnection", "Return connection");
     * return connection; } catch (SQLException exception) {
     * logger.error("Exception:",exception);
     * ProgenLog.log(ProgenLog.SEVERE,this, "getConnection", "Exception
     * "+exception.getMessage()); } catch (ClassNotFoundException cnfe) {
     * logger.error("Exception:",cnfe); ProgenLog.log(ProgenLog.SEVERE,this,
     * "getConnection", "Exception "+cnfe.getMessage()); }
     *
     * return connection;
    }
     */
    private Connection getConnection(ConnectionMetadata conMetadata) {

//   ProgenLog.log(ProgenLog.FINE,this, "getConnection", "Enter with pool "+conMetadata.getConnId());
        logger.info("Enter with pool " + conMetadata.getConnId());
        PoolingDataSource conPool;
        try {
            if (extConnectionPool == null) {
                synchronized (ProgenConnection.class) {
                    if (extConnectionPool == null) {
                        extConnectionPool = new HashMap<Integer, PoolingDataSource>();
                    }
                }
            }

            conPool = extConnectionPool.get(conMetadata.getConnId());

            if (conPool == null) {
                synchronized (ProgenConnection.class) {
                    if (conPool == null) {
                        conPool = this.createPoolingDataSource(conMetadata);
                        extConnectionPool.put(conMetadata.getConnId(), conPool);
                    }
                }
            }
//    ////.println("Arun++++");
//    this.connCount++;
//    ////.println("No. of Active Connections "+this.genericPool.getNumActive());
//    ////.println("No. of Idle Connections "+this.genericPool.getNumIdle());
//    ////.println("Connection Count "+this.connCount);
//    ////.println("Arun------");
//   ProgenLog.log(ProgenLog.FINE,this, "getConnection", "Return connection from pool");
            logger.info("Return connection from pool");
            return conPool.getConnection();

        } catch (SQLException ex) {
            //   ProgenLog.log(ProgenLog.SEVERE,this, "getConnection", "Exception "+exception.getMessage());
            logger.error("Exception:", ex);
        } catch (ClassNotFoundException ex) {
            //   ProgenLog.log(ProgenLog.SEVERE,this, "getConnection", "Exception "+ex.getMessage());
            logger.error("Exception:", ex);
        }

        return null;
    }

    /*
     * public int getNumActive() { return this.genericPool.getNumActive(); }
     *
     * public int getNumIdle() { return this.genericPool.getNumIdle();
    }
     */
    private PoolingDataSource createPoolingDataSource(ConnectionMetadata conMetadata) throws ClassNotFoundException {

        String dbUrl = null;

//        ProgenLog.log(ProgenLog.FINE,this, "createPoolingDataSource", "Enter");
        logger.info("Enter");

        GenericObjectPool connectionPool = new GenericObjectPool();
        connectionPool.setMaxActive(conMetadata.getMaxActive());
        connectionPool.setMaxWait(conMetadata.getMaxWait());
        if ("oracle".equalsIgnoreCase(conMetadata.getDbType())) {
            try {
                Class.forName("oracle.jdbc.OracleDriver").newInstance();
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                dbUrl = "jdbc:oracle:thin:@" + conMetadata.getServer() + ":" + conMetadata.getPort() + ":" + conMetadata.getSID();
                //Connection conn = DriverManager.getConnection(dbUrl);
            } catch (InstantiationException ex) {
                logger.error("Exception:", ex);
            } catch (IllegalAccessException ex) {
                logger.error("Exception:", ex);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            dbUrl = "jdbc:oracle:thin:@" + conMetadata.getServer() + ":" + conMetadata.getPort() + ":" + conMetadata.getSID();
        } else if (conMetadata.getDbType() != null && conMetadata.getDbType().equalsIgnoreCase("mysql")) {
            Class.forName("com.mysql.jdbc.Driver");
//              try {
//                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
//            } catch (SQLException ex) {
//                logger.error("Exception:",ex);
//            }
            //modified by Govardhan
//            dbUrl = "jdbc:mysql://" + conMetadata.getServer() + ":" + conMetadata.getPort() + "/" + conMetadata.getSchemaName();
            dbUrl = "jdbc:mysql://" + conMetadata.getServer() + ":" + conMetadata.getPort() + "/" + conMetadata.getSchemaName() + "?zeroDateTimeBehavior=convertToNull";


        } else if (conMetadata.getDbType() != null && conMetadata.getDbType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
            try {
                // String driver = "net.sourceforge.jtds.jdbc.Driver";
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
                dbUrl = "jdbc:jtds:sqlserver://" + conMetadata.getServer() + ":" + conMetadata.getPort() + "/" + conMetadata.getSchemaName();
                //connection = DriverManager.getConnection(url,userName , password);
            } catch (SQLException ex) {
//                ProgenLog.log(ProgenLog.SEVERE,this, "createPoolingDataSource", "Exception "+ex.getMessage());
                logger.error("Exception:", ex);
            }
        } else if (conMetadata.getDbType() != null && conMetadata.getDbType().equalsIgnoreCase("PostgreSQL")) {
            Class.forName("org.postgresql.Driver");
            dbUrl = "jdbc:postgresql://" + conMetadata.getServer() + ":" + conMetadata.getPort() + "/" + conMetadata.getSchemaName();
//                   connection = DriverManager.getConnection("jdbc:postgresql://" + server + ":" + serverPort + "/" + schemaName, userName, password);

        }

//        ProgenLog.log(ProgenLog.FINE,this, "createPoolingDataSource", "dbUrl is "+dbUrl);
        logger.info("dbUrl is " + dbUrl);
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(dbUrl, conMetadata.getUserName(), conMetadata.getPassWord());
//        try {
//            Connection conn = connectionFactory.createConnection();
//            ////.println(conn.hashCode());
//        } catch (SQLException ex) {
//            logger.error("Exception: ",ex);
//        }

        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
        poolableConnectionFactory.setConnectionFactory(connectionFactory);
        PoolingDataSource dataSource = new PoolingDataSource(poolableConnectionFactory.getPool());
//        this.genericPool = connectionPool;
//        ProgenLog.log(ProgenLog.FINE,this, "createPoolingDataSource", "Return poolingDataSource");
        logger.info("Return poolingDataSource");
        return dataSource;
    }

    public Connection getmySQLConnection(String dbname, String server, String port, String username, String password) {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" + dbname, username, password);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return connection;
    }

    public static void main(String args[]) {
        //ProgenConnection1 db = new ProgenConnection1();
        //db.dbConnect("jdbc:jtds:sqlserver://localhost:1433/metadata", "sa", "welcome");
//        JtdsDataSource dataSource = new JtdsDataSource();
//            dataSource.setUser("sa");
//            dataSource.setPassword("welcome");
//            dataSource.setPortNumber(1433);
//            dataSource.setServerName("localhost");
//            dataSource.setDatabaseName("metadata");
//        try {
//            Connection conn = dataSource.getConnection();
//            //PbEncrypter enc = new PbEncrypter();
////        String password = enc.encrypt(password);
//        } catch (SQLException ex) {
//            logger.error("Exception: ",ex);
//        }
        //PbEncrypter enc = new PbEncrypter();
//        String password = enc.encrypt(password);
    }
}

class ProgenConnection1 {

    public ProgenConnection1() {
    }

    //public static void main(String[] args) {
    public void dbConnect(String db_connect_string, String db_userid, String db_password) {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection conn = DriverManager.getConnection(db_connect_string, db_userid, db_password);
            ////.println("SQL Server Database connected");

        } catch (Exception e) {
        }
    }
}
