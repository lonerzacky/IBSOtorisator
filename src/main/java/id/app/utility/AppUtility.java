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
package id.app.utility;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;

import id.app.global.AppGlobal;
import org.json.simple.JSONObject;

public class AppUtility {

    private AppUtility() {
    }

    public static String getString(String key) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("strings", Locale.getDefault());
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            e.printStackTrace();
            return '!' + key + '!';
        }
    }
    public static JSONObject responseUtil(String response_code, String response_message, String response_data) {

        JSONObject nParams = new JSONObject();
        nParams.put("response_code", response_code);
        nParams.put("response_message", response_message);
        nParams.put("response_data", response_data);
        AppGlobal.showMessage(response_message + "\n");
        return nParams;
    }
    public static JSONObject responErr(String code, String error) {
        String message;
        if ("01".equals(code)) {
            message = "REQUEST ERROR";
        } else {
            message = "UNKNOWN ERROR";
        }
        return responseUtil(code, message, error);
    }

}
