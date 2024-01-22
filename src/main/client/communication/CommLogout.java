package main.client.communication;

import main.client.UserInfo;

public class CommLogout extends CommunicationBase {

    public CommLogout() {
    }

    @Override
    public String getOutMessage() {
        return UserInfo.getToken() + " LOGOUT";
    }

    @Override
    public void parseResponse() throws Exception {
        String[] respPrated = this.resp.split(" ");

        if (respPrated[0].equals("EXCEPTION")) {
            throw new Exception(respPrated[1]);
        }
    }
}
