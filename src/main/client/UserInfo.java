package main.client;

public class UserInfo {
    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        UserInfo.token = token;
    }

    private static String token = null;
}
