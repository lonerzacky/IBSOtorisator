/*
 * Copyright 2021 PT. USSI Pinbuk Prima Software.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package id.api.helper;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.InvalidObjectException;
import java.sql.Connection;
import java.sql.SQLException;


@SuppressWarnings("DuplicatedCode")
public class ConnectionPool {

    private static BasicDataSource dataSource = null;
    private static Connection connection;

    private static BasicDataSource getDataSource(PoolConfiguration config) {
        if (dataSource == null) {
            BasicDataSource ds = new BasicDataSource();
            String url = "jdbc:mysql://" + config.getDBAddress() + ":" + config.getDBPort() + "/" + config.getDBName() + "?useLegacyDatetimeCode=false&serverTimezone=Asia/Jakarta&autoReconnect=true";
            ds.setUrl(url);
            ds.setUsername(config.getDBUser());
            ds.setPassword(config.getDBPassword());
            ds.setMinIdle(config.getMinIdlePool());
            ds.setMaxIdle(config.getMaxIdlePool());
            ds.setMaxOpenPreparedStatements(config.getPrepStmtCacheSize());
            ds.setDefaultAutoCommit(config.getAutoCommit());
            ds.setValidationQuery("SELECT 1");
            ds.setTestOnBorrow(true);
            dataSource = ds;
        }
        return dataSource;
    }

    public static void configure(PoolConfiguration configuration) {
        dataSource = getDataSource(configuration);
    }

    public static boolean isDataSourceNull() {
        return (dataSource == null);
    }

    public static synchronized Connection getConnection() throws SQLException, InvalidObjectException {
        if (dataSource == null) throw new InvalidObjectException("PoolConfiguration not defined...");
        if (connection == null || connection.isClosed()) {
            connection = dataSource.getConnection();
        }
        return connection;
    }
}
