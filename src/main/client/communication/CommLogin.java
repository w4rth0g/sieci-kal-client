package main.client.communication;

public class CommLogin extends CommunicationBase{

    public String getUsername() {
        return username;
    }

    private final String username;
    private final String password;

    public String getLoginToken() {
        return loginToken;
    }

    private String loginToken;

    public int getUserId() {
        return userId;
    }

    private int userId;

    public CommLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getOutMessage() {
        return "LOGIN " + username + " " + password;
    }

    @Override
    public void parseResponse() throws Exception {
        String[] respPrated = this.resp.split(" ");

        if (respPrated[0].equals("EXCEPTION")) {
            throw new Exception(respPrated[1]);
        }

        this.loginToken = respPrated[1];
        this.userId = Integer.parseInt(respPrated[2]);
    }
}
