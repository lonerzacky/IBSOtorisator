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
package id.api.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InvalidObjectException;
import java.net.BindException;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import id.api.helper.*;
import id.app.constanta.ConstantaString;
import id.app.global.AppGlobal;
import org.apache.log4j.BasicConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import id.app.global.AppConfig;
import id.app.utility.AppUtility;
import org.eclipse.jetty.util.log.StdErrLog;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection", "DuplicatedCode"})
public class Main {
    public static Properties property = new Properties();
    public static PoolConfiguration config;
    public static PoolConfigurationSys configSys;
    public static BufferedWriter log;
    public static AppConfig configini;

    public static void main(String[] args) {
        try {
            BasicConfigurator.configure();
            Calendar c = Calendar.getInstance();
            String s = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", c);
            String dir = "log";
            File file = new File(System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + dir + FileSystems.getDefault().getSeparator());
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.mkdir();
            }

            String namafile = s + ".log";
            log = new BufferedWriter(new FileWriter(
                    System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + dir + FileSystems.getDefault().getSeparator() + namafile, true));
            new Main();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static void start() {
        Properties pr = new Properties();
        pr.setProperty(AppUtility.getString("liblevel"), AppUtility.getString("typelevel"));
        StdErrLog.getLoggingProperty(pr, AppUtility.getString("liblevel"), AppUtility.getString("typelevel"));

        try {
            Server server = new Server(Integer.parseInt(AppConfig.getPort()));
            ServletHandler handler = new ServletHandler();
            regApi(handler);
            server.setHandler(handler);
            AppGlobal.showMessageTimeBuffer(AppUtility.getString("str_service"), log);
            AppGlobal.showMessageTimeBuffer(AppUtility.getString("str_portlisten") + " " + AppConfig.getPort() + AppUtility.getString("str_endreturn"), log);
            server.start();
            server.join();
        } catch (BindException be) {
            be.printStackTrace();
            AppGlobal.showMessageTimeBuffer(AppUtility.getString("str_stop") + be.getMessage() + AppUtility.getString("str_end"), log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void regApi(ServletHandler handler) {
        handler.addServletWithMapping(ApiEcho.class, "");
        handler.addServletWithMapping(ApiLogin.class, "/" + ConstantaString.apicode.ApiLogin + "/*");
        handler.addServletWithMapping(ApiSendOtorisasi.class, "/" + ConstantaString.apicode.ApiSendOtorisasi + "/*");
        handler.addServletWithMapping(ApiUpdateOtorisasi.class, "/" + ConstantaString.apicode.ApiUpdateOtorisasi + "/*");
        handler.addServletWithMapping(ApiListOtorisasi.class, "/" + ConstantaString.apicode.ApiListOtorisasi + "/*");
     }

    public void initConfig() {
        String appConfig = System.getProperty(AppUtility.getString("str_user")) + System.getProperty(AppUtility.getString("str_separator")) + AppUtility.getString("str_ini");

        FileInputStream fileApp;
        try {
            fileApp = new FileInputStream(appConfig);
            property.load(fileApp);
            configini = new AppConfig(AppUtility.getString("str_ini"));
            AppConfig.setPort(configini.getString(AppUtility.getString("str_listen")));
            AppConfig.setAddressDB(configini.getString(AppUtility.getString("str_addr")));
            AppConfig.setNameDB(configini.getString(AppUtility.getString("str_name")));
            AppConfig.setPortDB(configini.getString(AppUtility.getString("str_port")));
            AppConfig.setUserDB(configini.getString(AppUtility.getString("str_userdb")));
            AppConfig.setPassDB(configini.getString(AppUtility.getString("str_passdb")));
            AppConfig.setAutocommit(configini.getString(AppUtility.getString("str_autocommit")));
            AppConfig.setPortDBSys(configini.getString(AppUtility.getString("str_listen")));
            AppConfig.setAddressDBSys(configini.getString(AppUtility.getString("str_addr_sys")));
            AppConfig.setNameDBSys(configini.getString(AppUtility.getString("str_name_sys")));
            AppConfig.setPortDBSys(configini.getString(AppUtility.getString("str_port_sys")));
            AppConfig.setUserDBSys(configini.getString(AppUtility.getString("str_userdb_sys")));
            AppConfig.setPassDBSys(configini.getString(AppUtility.getString("str_passdb_sys")));
            AppConfig.setAutocommitSys(configini.getString(AppUtility.getString("str_autocommit_sys")));
            AppConfig.setAppName(configini.getString(AppUtility.getString("str_app_name")));
            AppConfig.set_BPRCode(configini.getString(AppUtility.getString("str_bpr_code")));
            AppConfig.set_AuthUserName(configini.getString(AppUtility.getString("str_auth_username")));
            AppConfig.set_AuthPassword(configini.getString(AppUtility.getString("str_auth_password")));

            initDatabasePool();
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initDatabasePool() {
        AppGlobal.showMessageTimeBuffer(AppUtility.getString("str_connect"), log);
        AppGlobal.showMessageTimeBuffer(AppUtility.getString("str_startdb"), log);
        Connection conn = null;
        Statement stmt = null;
        if (ConnectionPool.isDataSourceNull()) {
            config = Database.getPoolConfiguration();
            try {
                ConnectionPool.configure(config);
            } catch (RuntimeException e2) {
                e2.printStackTrace();
                AppGlobal.showMessageTimeBuffer("Configuration database failed : " + e2.getMessage(), log);
                System.exit(0);
            }

            try {
                conn = ConnectionPool.getConnection();
            } catch (InvalidObjectException | SQLException e1) {
                e1.printStackTrace();
                AppGlobal.showMessageTimeBuffer("Connection to database failed : " + e1.getMessage(), log);
                System.exit(0);
            }

            try {
                stmt = conn.prepareStatement("select 1");
                stmt.executeQuery("select 1");
            } catch (SQLException e1) {
                e1.printStackTrace();
                AppGlobal.showMessageTimeBuffer("SQL failed : " + e1.getMessage(), log);
                System.exit(0);
            }

            try {
                stmt.close();
            } catch (Exception ignored) {
            }
            try {
                conn.close();
            } catch (Exception ignored) {
            }
            initDatabasePoolSys();
        }
    }

    public void initDatabasePoolSys() {
        AppGlobal.showMessageTimeBuffer("Connecting Server Database Sys ... \n", log);
        AppGlobal.showMessageTimeBuffer("Starting Database Sys ...\n", log);
        Connection conn = null;
        Statement stmt = null;
        if (ConnectionPoolSys.isDataSourceNull()) {
            configSys = Database.getPoolConfigurationSys();
            try {
                ConnectionPoolSys.configure(configSys);
            } catch (RuntimeException e2) {
                e2.printStackTrace();
                AppGlobal.showMessageTimeBuffer("Configuration database sys failed : " + e2.getMessage(), log);
                System.exit(0);
            }

            try {
                conn = ConnectionPoolSys.getConnection();
            } catch (InvalidObjectException | SQLException e1) {
                e1.printStackTrace();
                AppGlobal.showMessageTimeBuffer("Connection to database sys failed : " + e1.getMessage(), log);
                System.exit(0);
            }

            try {
                stmt = conn.prepareStatement("select 1");
                stmt.executeQuery("select 1");
            } catch (SQLException e1) {
                e1.printStackTrace();
                AppGlobal.showMessageTimeBuffer("SQL failed : " + e1.getMessage(), log);
                System.exit(0);
            }

            try {
                stmt.close();
            } catch (Exception ignored) {
            }
            try {
                conn.close();
            } catch (Exception ignored) {
            }
            AppGlobal.showMessageTimeBuffer("Connection Database Success \n", log);
            start();
        }
    }

    public Main() {
        initConfig();
    }
}
