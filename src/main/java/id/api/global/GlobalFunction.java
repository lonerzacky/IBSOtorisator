package id.api.global;

import id.app.global.AppConfig;

import java.util.Base64;

public class GlobalFunction {

    public static boolean CekAuthorization(String HeaderAuth) {
        String auth, authString, username, password;
        String[] authArray;
        try {
            auth = HeaderAuth.substring(6);
            byte[] decodedBytes = Base64.getDecoder().decode(auth);
            authString = new String(decodedBytes);
            authArray = authString.split(":");
            username = authArray[0];
            password = authArray[1];

            return username.equals(AppConfig.get_AuthUserName()) && password.equals(AppConfig.get_AuthPassword());
        } catch (Exception e) {
            return false;
        }
    }

}