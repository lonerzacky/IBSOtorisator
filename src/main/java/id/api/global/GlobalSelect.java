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
package id.api.global;

import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GlobalSelect {

    public static String getValByKeyValString(String field, String table, String key, String val, Connection conn) {
        String val_return = "";
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = "SELECT " + field +
                    " FROM " + table +
                    " WHERE " + key + "='" + val + "'";

            st = conn.prepareStatement(query);
            rs = st.executeQuery(query);

            if (!rs.next()) {
                val_return = "";
            } else {
                val_return = rs.getString(field);
            }
            if (val_return == null) {
                val_return = "";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Gagal pengambilan data " + field);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
        }
        return val_return;
    }

}
