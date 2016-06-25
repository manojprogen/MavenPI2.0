package utils.db;

/**
 * Database connection Pool Manager
 *
 * @author saugupta
 *
 */
//import com.progen.log.ProgenLog;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Stack;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import org.apache.log4j.Logger;

public class JDBCConnectionPool {

    public static Logger logger = Logger.getLogger(JDBCConnectionPool.class);
    private ConnectionPoolDataSource _ds;
    private int _maxConnections;
    private int _timeout;
    private PrintWriter _log;
    private Semaphore _semaphore;
    private Stack _pooledConnections;
    private int _workingConnections;
    private PoolConnectionEventListener _poolConnectionEventListener;
    private boolean isDisposed;

    /**
     * Thrown in {@link #getConnection()} when no free connection becomes
     * available within
     * <code>_timeout</code> seconds.
     */
    public static class TimeoutException extends RuntimeException {

        public TimeoutException() {
            super("Timeout while waiting for a free database connection.");
        }
    }

    /**
     * Constructs a JDBCConnectionPool object with a _timeout of 60 seconds.
     *
     * @param _ds the data source for the connections.
     * @param _maxConnections the maximum number of connections.
     */
    public JDBCConnectionPool(ConnectionPoolDataSource dataSource, int maxConnections) {
        this(dataSource, maxConnections, 6000);
    }

    /**
     * Constructs a JDBCConnectionPool object.
     *
     * @param _ds the data source for the connections.
     * @param _maxConnections the maximum number of connections.
     * @param _timeout the maximum time in seconds to wait for a free
     * connection.
     */
    public JDBCConnectionPool(ConnectionPoolDataSource dataSource, int maxConnections, int timeout) {
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println("In JDBCConnectionPool(ConnectionPoolDataSource dataSource, int maxConnections, int timeout)");
        this._ds = dataSource;
        this._maxConnections = maxConnections;
        this._timeout = timeout;
        try {
            _log = dataSource.getLogWriter();
        } catch (SQLException e) {
//            ProgenLog.log(ProgenLog.SEVERE, this,"JDBCConnectionPool","Metadata Pool creation Failed "+e.getMessage());
            logger.error("Metadata Pool creation Failed :", e);
        }
        if (maxConnections < 1) {
            throw new IllegalArgumentException("Invalid maxConnections value.");
        }
        _semaphore = new Semaphore(maxConnections, true);
        _pooledConnections = new Stack();
        _poolConnectionEventListener = new PoolConnectionEventListener();
    }

    /**
     * Closes all unused pooled connections.
     */
    public synchronized void dispose() throws SQLException {
        if (isDisposed) {
            return;
        }
        isDisposed = true;
        SQLException e = null;
        while (!_pooledConnections.isEmpty()) {
            PooledConnection pconn = (PooledConnection) _pooledConnections.pop();
            try {
                pconn.close();
            } catch (SQLException e2) {
                if (e == null) {
                    e = e2;
                }
            }
        }
        if (e != null) {
            throw e;
        }
    }

    /**
     * Retrieves a connection from the connection pool. If
     * <code>_maxConnections</code> connections are already in use, the method
     * waits until a connection becomes available or
     * <code>_timeout</code> seconds elapsed. When the application is finished
     * using the connection, it must close it in order to return it to the pool.
     *
     * @return a new Connection object.
     * @throws TimeoutException when no connection becomes available within
     * <code>_timeout</code> seconds.
     */
    public Connection getConnection() throws SQLException {
        //   
        // This routine is unsynchronized, because _semaphore.tryAcquire() may block.
        synchronized (this) {
            if (isDisposed) {
                throw new IllegalStateException("Connection pool has been disposed.");
            }
        }
        try {
            if (!_semaphore.tryAcquire(_timeout, TimeUnit.SECONDS)) {
                throw new TimeoutException();
            }
        } catch (InterruptedException e) {
            logger.error("Exception:", e);
            throw new RuntimeException("Interrupted while waiting for a database connection.", e);
        }
        boolean ok = false;
        try {
            Connection conn = getConnection2();
            ////////////////////////////////////////////////////////////////////////////////////////////////////.println("conn is "+conn);
            ok = true;
            return conn;
        } finally {
            if (!ok) {
                _semaphore.release();
            }
        }
    }

    private synchronized Connection getConnection2() throws SQLException {
//        
        if (isDisposed) {
            throw new IllegalStateException("Connection pool has been disposed.");   // test again with lock
        }
        PooledConnection pconn;
        if (!_pooledConnections.empty()) {
            pconn = (PooledConnection) _pooledConnections.pop();
        } else {
            pconn = _ds.getPooledConnection();
        }
        Connection conn = pconn.getConnection();
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println("conn in getConnection2() is "+conn);
        _workingConnections++;
        pconn.addConnectionEventListener(_poolConnectionEventListener);
        assertInnerState();
        return conn;
    }

    private synchronized void recycleConnection(PooledConnection pconn) {
        if (isDisposed) {
            disposeConnection(pconn);
            return;
        }
        if (_workingConnections <= 0) {
            throw new AssertionError();
        }
        _workingConnections--;
        _semaphore.release();
        _pooledConnections.push(pconn);
        assertInnerState();
    }

    private synchronized void disposeConnection(PooledConnection pconn) {
        if (_workingConnections <= 0) {
            throw new AssertionError();
        }
        _workingConnections--;
        _semaphore.release();
        closeConnectionNoEx(pconn);
        assertInnerState();
    }

    private void closeConnectionNoEx(PooledConnection pconn) {
        try {
            pconn.close();
        } catch (SQLException e) {
            log("Error while closing database connection: " + e.toString());
        }
    }

    private void log(String msg) {
        String s = "MiniConnectionPoolManager: " + msg;
        try {
            if (_log == null) {
                //System.err.println(s);
            } else {
                _log.println(s);
            }
        } catch (Exception e) {
        }
    }

    private void assertInnerState() {
        if (_workingConnections < 0) {
            throw new AssertionError();
        }
        if (_workingConnections + _pooledConnections.size() > _maxConnections) {
            throw new AssertionError();
        }
        if (_workingConnections + _semaphore.availablePermits() > _maxConnections) {
            throw new AssertionError();
        }
    }

    private class PoolConnectionEventListener implements ConnectionEventListener {

        public void connectionClosed(ConnectionEvent event) {
            PooledConnection pconn = (PooledConnection) event.getSource();
            pconn.removeConnectionEventListener(this);
            recycleConnection(pconn);
        }

        public void connectionErrorOccurred(ConnectionEvent event) {
            PooledConnection pconn = (PooledConnection) event.getSource();
            pconn.removeConnectionEventListener(this);
            disposeConnection(pconn);
        }
    }

    /**
     * Returns the number of active (open) connections of this pool. This is the
     * number of
     * <code>Connection</code> objects that have been issued by {@link #getConnection()}
     * for which
     * <code>Connection.close()</code> has not yet been called.
     *
     * @return the number of active connections.
     *
     */
    public synchronized int getActiveConnections() {
        return _workingConnections;
    }
} // end class JDBCConnectionPool
