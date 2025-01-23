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
package id.app.global;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class AppConfig {
    private static String _port = "";
    private static String _address = "";
    private static String _addressSys = "";
    private static String _name = "";
    private static String _nameSys = "";
    private static String _portDB = "";
    private static String _portDBSys = "";
    private static String _user = "";
    private static String _userSys = "";
    private static String _pass = "";
    private static String _passSys = "";
    private static String _autocommit = "";
    private static String _autocommit_sys = "";
    private static String _appName = "";
    private static String _BPRCode = "";
    private static String _AuthUserName = "";
    private static String _AuthPassword = "";

    Properties properties = new Properties();

    public AppConfig(String filename) throws Exception {
        String path = new File(".").getCanonicalPath() + "/" + filename;
        InputStream ispath = Files.newInputStream(Paths.get(path));
        properties.load(ispath);
    }

    public static void set_AuthUserName(String authUserName) {
        _AuthUserName = authUserName;
    }

    public static void set_AuthPassword(String authPassword) {
        _AuthPassword = authPassword;
    }

    public static void setPort(String port) {
        _port = port;
    }

    public static void setAutocommit(String autocommit) {
        _autocommit = autocommit;
    }

    public static void setAutocommitSys(String autocommit_sys) {
        _autocommit_sys = autocommit_sys;
    }

    public static void setAddressDB(String address) {
        _address = address;
    }

    public static void setAddressDBSys(String addressSys) {
        _addressSys = addressSys;
    }

    public static void setNameDB(String name) {
        _name = name;
    }

    public static void setNameDBSys(String nameSys) {
        _nameSys = nameSys;
    }

    public static void setPortDB(String portDB) {
        _portDB = portDB;
    }

    public static void setPortDBSys(String portDBSys) {
        _portDBSys = portDBSys;
    }

    public static void setUserDB(String user) {
        _user = user;
    }

    public static void setUserDBSys(String userSys) {
        _userSys = userSys;
    }

    public static void setPassDB(String pass) {
        _pass = pass;
    }

    public static void setPassDBSys(String passSys) {
        _passSys = passSys;
    }

    public static void setAppName(String appName) {
        _appName = appName;
    }

    public static void set_BPRCode(String BPRCode) {
        _BPRCode = BPRCode;
    }

    public static String get_AuthUserName() {
        return _AuthUserName;
    }

    public static String get_AuthPassword() {
        return _AuthPassword;
    }

    public static String getAppName() {
        return _appName;
    }

    public static String getPort() {
        return _port;
    }

    public static String getAutocommit() {
        return _autocommit;
    }

    public static String getAutocommitSys() {
        return _autocommit_sys;
    }

    public String getString(String key) {
        return properties.getProperty(key, null);
    }

    public static String getAddressDB() {
        return _address;
    }

    public static String getAddressDBSys() {
        return _addressSys;
    }

    public static String getNameDB() {
        return _name;
    }

    public static String getNameDBSys() {
        return _nameSys;
    }

    public static String getPortDB() {
        return _portDB;
    }

    public static String getPortDBSys() {
        return _portDBSys;
    }

    public static String getUserDB() {
        return _user;
    }

    public static String getUserDBSys() {
        return _userSys;
    }

    public static String getPassDB() {
        return _pass;
    }

    public static String getPassDBSys() {
        return _passSys;
    }

    public static String get_BPRCode() {
        return _BPRCode;
    }


}
