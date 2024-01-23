package main.client.communication;

import main.client.UserInfo;
import main.client.model.Event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CommGetEvents extends CommunicationBase{


    public CommGetEvents() {
        this.eventList = new ArrayList<>();
    }

    public List<Event> getEventList() {
        return eventList;
    }

    private List<Event> eventList;

    @Override
    public String getOutMessage() {
        return UserInfo.getToken() + " LIST_EVENTS";
    }

    @Override
    public String sendAndGetResp() {
        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            out.println(this.getOutMessage());

            while((resp = in.readLine()) != null) {
                String[] respParted = resp.split(" \\| ");
                
                Event e = new Event();
                e.setEventId(Integer.parseInt(respParted[0]));
                e.setUserId(Integer.parseInt(respParted[1]));
                e.setTitle(respParted[2]);
                e.setDescription(respParted[3]);
                e.setStartTime(Timestamp.valueOf(respParted[4]));
                e.setEndTime(Timestamp.valueOf(respParted[5]));

                this.eventList.add(e);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resp;
    }
}
