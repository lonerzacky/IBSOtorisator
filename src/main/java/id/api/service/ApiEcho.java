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

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import id.api.global.GlobalFunction;
import id.app.constanta.ConstantaString;
import id.app.global.AppConfig;
import org.json.simple.JSONObject;
import id.app.global.AppGlobal;
import id.app.utility.AppUtility;

public class ApiEcho extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String print;
        response.setHeader("API", "Echo");
        String headerAuth=(request.getHeader("Authorization"));
        boolean lCekAuth = GlobalFunction.CekAuthorization(headerAuth);
        PrintWriter out = response.getWriter();

        if (!lCekAuth) {
            JSONObject nParams = AppUtility.responseUtil(ConstantaString.global.apiErrorCode, "INVALID AUTHENTICATION","[]");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print(nParams);
            return;

        }
        try {
            print = AppConfig.getAppName();
            JSONObject nParams = new JSONObject();
            nParams.put("response_code", ConstantaString.global.apiSuccessCode);
            nParams.put("response_message", print);
            AppGlobal.showMessageTimeBuffer(print + "\n", Main.log);
            out.print(nParams);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject nParams = AppUtility.responErr("01", e.getMessage());
            out.print(nParams);
            out.close();
        }

    }
}