package main.client.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationBase {

    private String hostname = "172.27.226.250"; // Adres serwera
    private int port = 1234; // Port serwera
    protected String resp = null;

    public String sendAndGetResp() {
        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            out.println(this.getOutMessage());

            resp = in.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resp;
    }

    // Klasa bazowa do nadpisania
    public String getOutMessage() {
        return null;
    }

    public void parseResponse() throws Exception {
    }
}
