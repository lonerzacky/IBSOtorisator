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

import id.app.constanta.ConstantaString;

import java.util.Arrays;
import java.util.Locale;

public class ConsoleUtility {
    private static final int PAD_LIMIT = 8192;
    private static int len = 0;
    private static final int maxlen = 50;


    public static String printRequest(String var, String val, int type) {
        String print = "";
        String enter = "\n";
        if (val == null) {
            val = "";
        }
        switch (type) {
            case 1:
                print = ConsoleUtility.center_delimiter(var, ConstantaString.global.lenPrint);
                break;
            case 2:
                print = ConsoleUtility.leftRightPad(var, val);
                break;
            case 3:
                print = ConsoleUtility.leftRightPadNon(var, val);
                break;
        }
        return print + enter;
    }

    public static String center_delimiter(String str, int size) {
        return center(str, size, '-');
    }

    public static String center(String str, int size, char padChar) {
        if (str == null || size <= 0) {
            return str;
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        str = leftPad(str, strLen + pads / 2, padChar);
        str = rightPad(str, size, padChar);
        return str;
    }

    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return leftPad(str, size, padChar);
        }
        return padding(pads, padChar).concat(str);
    }

    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return rightPad(str, size, padChar);
        }
        return str.concat(padding(pads, padChar));
    }

    private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        }
        final char[] buf = new char[repeat];
        Arrays.fill(buf, padChar);
        return new String(buf);
    }

    public static String leftRightPad(String sField, String sValue) {
        sField = sField.trim();
        sField = sField.toUpperCase(Locale.ENGLISH);
        len = (maxlen / 2) - sField.length();
        return sField + leftPad(" ", len, ' ') + ":" + sValue;
    }

    public static String leftRightPadNon(String sField, String sValue) {
        sField = sField.trim();
        sField = sField.toUpperCase(Locale.ENGLISH);
        len = (maxlen / 2) - sField.length();
        return sField + sValue;
    }
}