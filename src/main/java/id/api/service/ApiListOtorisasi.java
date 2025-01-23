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

import com.github.jedis.lock.JedisLock;
import id.api.controller.nasabah.global;
import id.api.global.GlobalFunction;
import id.api.model.Otorisasi;
import id.app.constanta.ConstantaString;
import id.app.global.AppGlobal;
import id.app.utility.AppUtility;
import id.app.utility.ConsoleUtility;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;


public class ApiListOtorisasi extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        JSONObject nParams = AppUtility.responErr("01", "UNKNOWN METHOD");
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        out.print(nParams);
    }

    @SneakyThrows
    @SuppressWarnings({"rawtypes", "DuplicatedCode"})
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> data;
        StringBuilder print = new StringBuilder();
        response.setHeader("Otorisasi", "List");
        PrintWriter out = response.getWriter();
        StringBuilder jb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONParser parser = new JSONParser();
        String errMsg = "";
        Map json = null;
        try {
            json = (Map) parser.parse(jb.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        assert json != null;
        Iterator iter = json.entrySet().iterator();
        print.append(ConsoleUtility.printRequest("Request", "", ConstantaString.global.typeHeader));
        Map.Entry entry;

        while (iter.hasNext()) {
            entry = (Map.Entry) iter.next();
            String cek = AppGlobal.errParam(json, entry.getKey().toString(), 0);
            if (cek.isEmpty()) {
                print.append(ConsoleUtility.printRequest(entry.getKey().toString(),
                        entry.getValue().toString(), ConstantaString.global.typeContent));
            } else {
                print.append(ConsoleUtility.printRequest(entry.getKey().toString(),
                        "null", ConstantaString.global.typeContent));
                json.put(entry.getKey().toString(), "");
            }
        }
        String tgl_awal;
        String tgl_akhir;
        String user_id;
        String status;
        Jedis jedis = new Jedis("localhost");
        JedisLock lock = new JedisLock(jedis, ConstantaString.global.lockKey, 10000, 30000);
        lock.acquire();
        String headerAuth=(request.getHeader("Authorization"));
        boolean lCekAuth = GlobalFunction.CekAuthorization(headerAuth);
        if (!lCekAuth) {
            JSONObject nParams = AppUtility.responseUtil(ConstantaString.global.apiErrorCode, "INVALID AUTHENTICATION","[]");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print(nParams);
            lock.release();
            return;
        }

        try {
            errMsg += AppGlobal.errParam(json, "tgl_awal", 1);
            errMsg += AppGlobal.errParam(json, "tgl_akhir", 1);
            errMsg += AppGlobal.errParam(json, "user_id", 1);
            errMsg += AppGlobal.errParam(json, "status", 1);

            if (errMsg.isEmpty()) {
                print.append(ConsoleUtility.printRequest("Respon", "", ConstantaString.global.typeHeader));
                tgl_awal = json.get("tgl_awal").toString();
                tgl_akhir = json.get("tgl_akhir").toString();
                user_id = json.get("user_id").toString();
                status = json.get("status").toString();
                Otorisasi oto = new Otorisasi();
                oto.set_tgl_awal(tgl_awal);
                oto.set_tgl_akhir(tgl_akhir);
                oto.set_user_id_pengirim(user_id);
                oto.set_status(status);
                data = global.listOto(oto);
            } else {
                JSONObject nParams = new JSONObject();
                nParams.put("response_code", ConstantaString.global.apiErrorCode);
                nParams.put("response_message", errMsg);
                data = nParams;
            }

            print.append(ConsoleUtility.printRequest("", data.toString(), ConstantaString.global.typeContent));
            print.append(ConsoleUtility.printRequest("End", "", ConstantaString.global.typeHeader));
            System.out.println(print);
            out.print(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject nParams = AppUtility.responErr(ConstantaString.global.apiErrorCode, e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(nParams);
        } finally {

            lock.release();
        }


    }
}
