package main.client.communication;

import main.client.UserInfo;

public class CommGetUser extends CommunicationBase {

    private int userId;

    public String getUsername() {
        return username;
    }

    private String username;

    public CommGetUser(int userId) {
        this.userId = userId;
    }

    @Override
    public String getOutMessage() {
        return UserInfo.getToken() + " GET_USER " + userId;
    }

    @Override
    public void parseResponse() throws Exception {
        String[] respPrated = this.resp.split(" ");

        if (respPrated[0].equals("EXCEPTION")) {
            throw new Exception(respPrated[1]);
        }

        this.username = respPrated[1];
    }
}
