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

import id.app.global.AppConfig;

public class Database {

    public static PoolConfiguration getPoolConfiguration() {
        PoolConfiguration cfg = new PoolConfiguration();
        String addr = AppConfig.getAddressDB().trim();
        String name = AppConfig.getNameDB().trim();
        String port = AppConfig.getPortDB().trim();
        String user = AppConfig.getUserDB().trim();
        String pass = AppConfig.getPassDB().trim();
        String autocommit = AppConfig.getAutocommit().trim();

        cfg.setDBAddress(addr);
        cfg.setDBName(name);
        cfg.setDBPort(port);
        cfg.setDBUser(user);
        cfg.setDBPassword(pass);
        cfg.setAutoCommit(autocommit);

        return cfg;
    }

    public static PoolConfigurationSys getPoolConfigurationSys() {
        PoolConfigurationSys cfg = new PoolConfigurationSys();
        String addr = AppConfig.getAddressDBSys().trim();
        String name = AppConfig.getNameDBSys().trim();
        String port = AppConfig.getPortDBSys().trim();
        String user = AppConfig.getUserDBSys().trim();
        String pass = AppConfig.getPassDBSys().trim();
        String autocommit = AppConfig.getAutocommitSys().trim();

        cfg.setDBAddress(addr);
        cfg.setDBName(name);
        cfg.setDBPort(port);
        cfg.setDBUser(user);
        cfg.setDBPassword(pass);
        cfg.setAutoCommit(autocommit);

        return cfg;
    }

}
