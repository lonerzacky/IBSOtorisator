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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;

import id.api.global.GlobalSelect;
import id.api.helper.ConnectionPool;
import id.api.helper.ConnectionPoolSys;
import id.api.model.Otorisasi;
import id.app.utility.AppUtility;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;

import com.google.auth.oauth2.GoogleCredentials;

public class AppGlobal {

    public static void showMessage(String msg) {
        Calendar c = Calendar.getInstance();
        String s = String.format(AppUtility.getString("str_time"), c);
        String msgRespon = s + AppUtility.getString("str_backslash") + msg;
        System.out.print(msgRespon);
    }

    public static void showMessageTimeBuffer(String msg, BufferedWriter log) {
        showMessage(msg);
        writeLog(msg, log);
    }

    public static void writeLog(String pesan, BufferedWriter log) {
        Calendar c = Calendar.getInstance();
        String s = String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL", c);
        try {
            log.write(s + "|" + pesan);
            log.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String errParam(Map json, String var, Integer status) {
        String errMsg = "";
        Object data = json.get(var);
        if ((!(data instanceof Integer) && !(data instanceof Double) && !(data instanceof Long)) && !(data instanceof String)) {
            if (!var.equals(var.toUpperCase())) {
                errMsg = errParam(json, var.toUpperCase(), status);
            } else {
                if (status == 1) {
                    AppGlobal.showMessage("MISSING " + var.toUpperCase() + " PARAMETER!\n");
                }
                if (data == null) {
                    errMsg = "PARAMETER " + var.toUpperCase() + " NULL!\n";
                } else {
                    errMsg += "MISSING " + var.toUpperCase() + " PARAMETER!\n";
                }
            }
        }
        return errMsg;
    }


    private static String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(Files.newInputStream(Paths.get("service-account.json")))
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));
        googleCredentials.refresh();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    public static boolean pushNotification(String fcmToken, String description, Otorisasi oto) throws IOException, SQLException {
        int timeout = 30;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        CloseableHttpClient httpClient =
                HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpPost request;
        HttpResponse httpResp;
        request = new HttpPost("https://fcm.googleapis.com/v1/projects/ibs-otorisatorpps/messages:send");
        String accessToken = getAccessToken();
        request.addHeader("Authorization", "Bearer " + accessToken);
        request.addHeader("Content-Type", "application/json; UTF-8");
        JSONObject jsonMData = new JSONObject();
        JSONObject jsonData = new JSONObject();
        JSONObject clientJsonData = new JSONObject();
        jsonData.put("token", fcmToken);
        clientJsonData.put("title", "Permintaan Otorisasi");
        clientJsonData.put("body", description);
        jsonData.put("notification", clientJsonData);

        JSONObject payloadData = new JSONObject();
        Connection connSys = ConnectionPoolSys.getConnection();
        Connection conn = ConnectionPool.getConnection();
        String namaPengirim = GlobalSelect.getValByKeyValString("nama_lengkap", "sys_daftar_user", "user_id", oto.get_user_id_pengirim(), connSys);
        String namaKantor = GlobalSelect.getValByKeyValString("nama_kantor", "app_kode_kantor", "kode_kantor", oto.get_kode_kantor(), conn);
        payloadData.put("otorisator_id", oto.get_otorisator_id());
        payloadData.put("tgl_trans", oto.get_tgl_trans());
        payloadData.put("pengirim", namaPengirim);
        payloadData.put("kantor", namaKantor);
        payloadData.put("keterangan", oto.get_keterangan());
        jsonData.put("data", payloadData);
        jsonMData.put("message", jsonData);
        StringEntity params = new StringEntity(jsonMData.toString());
        //request.addHeader("content-type", "application/json");
        request.setEntity(params);
        httpResp = httpClient.execute(request);
        int code = httpResp.getStatusLine().getStatusCode();
        System.out.println(httpResp);
        return code == 200;
    }


}
