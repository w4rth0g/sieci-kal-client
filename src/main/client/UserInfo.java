package main.client;

// klasa przechowujaca informacje o biezacym uzytkowniku
public class UserInfo {
    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        UserInfo.token = token;
    }

    private static String token = null;

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        UserInfo.userId = userId;
    }

    private static int userId = -1;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserInfo.username = username;
    }

    private static String username = null;
}
