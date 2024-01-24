package main.client.communication;

import main.client.UserInfo;

public class CommDeleteEvent extends CommunicationBase {

    private final int eventId;

    public CommDeleteEvent(int eventId) {
        this.eventId = eventId;
    }

    @Override
    public String getOutMessage() {
        return UserInfo.getToken() + " DELETE_EVENT " + eventId;
    }

    @Override
    public void parseResponse() throws Exception {
        String[] respPrated = this.resp.split(" ");

        if (respPrated[0].equals("EXCEPTION")) {
            throw new Exception(respPrated[1]);
        }
    }
}
