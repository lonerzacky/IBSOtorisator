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

import id.app.constanta.ConstantaString;

public class PoolConfigurationSys {
    private String dbAddr = "localhost";
    private String dbName = "database";
    private int dbPort = 3306;
    private String dbUser = "root";
    private String dbPass = "";
    private boolean autoCommit = false;

    public PoolConfigurationSys() {
    }

    public void setDBAddress(String address){
        dbAddr = ConstantaString.doubleStringNull(address, dbAddr);
    }

    public void setDBName(String name){
        this.dbName = ConstantaString.doubleStringNull(name, dbName);
    }

    public void setDBPort(String port){
        int p = 0;
        try{
            p = Integer.parseInt(ConstantaString.doubleStringNull(port, "0").trim());
        }catch(NumberFormatException ignored){}
        setDBPort(p);
    }

    public void setDBPort(int port){
        this.dbPort = port;
    }

    public void setDBUser(String user){
        dbUser = ConstantaString.doubleStringNull(user, dbUser);
    }

    public void setDBPassword(String passwd){
        this.dbPass = ConstantaString.singleStringNull(passwd);
    }

    public void setAutoCommit(String value){
        setAutoCommit(ConstantaString.singleNullBoolean(value));
    }

    public void setAutoCommit(boolean value){
        this.autoCommit = value;
    }

    public String getDBAddress(){
        return this.dbAddr;
    }

    public String getDBName(){
        return this.dbName;
    }

    public int getDBPort(){
        return this.dbPort;
    }

    public String getDBUser(){
        return dbUser;
    }

    public String getDBPassword(){
        return this.dbPass;
    }

    public int getMinIdlePool(){
        return 0;
    }

    public int getMaxIdlePool(){
        return 0;
    }

    public int getPrepStmtCacheSize(){
        return 250;
    }

    public boolean getAutoCommit(){
        return autoCommit;
    }
}