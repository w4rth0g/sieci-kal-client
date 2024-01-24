package main.client;

public class UserInfo {
    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        UserInfo.token = token;
    }

    private static String token = null;

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        UserInfo.userId = userId;
    }

    private static String userId = null;
}
