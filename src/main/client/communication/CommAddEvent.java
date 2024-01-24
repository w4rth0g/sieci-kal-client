package main.client.communication;

import main.client.UserInfo;

public class CommAddEvent extends CommunicationBase {

    private final String title;
    private final String desc;
    private final String startTime;
    private final String endTime;

    public CommAddEvent(String title, String desc, String startTime, String endTime) {
        this.title = title;
        this.desc = desc;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String getOutMessage() {
        return UserInfo.getToken() + " ADD_EVENT " + title + " " + desc + " " + startTime + " " + endTime;
    }

    @Override
    public void parseResponse() throws Exception {
        String[] respPrated = this.resp.split(" ");

        if (respPrated[0].equals("EXCEPTION")) {
            throw new Exception(respPrated[1]);
        }
    }
}
