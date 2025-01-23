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
package id.app.constanta;

public class ConstantaString {

    public static class global {
        public static int lenPrint = 70;
        public static int typeContent = 2;
        public static int typeHeader = 1;
        public static String lockKey = "4EC73FF4872E32B9E46B4B752646E";
        public static String apiSuccessCode = "00";
        public static String apiFailCode = "01";
        public static String apiErrorCode = "09";
    }

    public static class apicode {
        public static String ApiLogin = "login";
        public static String ApiSendOtorisasi = "oto/send";
        public static String ApiUpdateOtorisasi = "oto/update";
        public static String ApiListOtorisasi = "oto/list";
    }

    public static String doubleStringNull(String address, String dbAddr) {
        if (address.isEmpty()) {
            address = dbAddr;
        }
        return address;
    }

    public static String singleStringNull(String passwd) {
        return passwd;
    }

    public static Boolean singleNullBoolean(String value) {
        return Boolean.valueOf(value);
    }
}
