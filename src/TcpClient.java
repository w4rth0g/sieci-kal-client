import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpClient {
    public static void main(String[] args) {
        String hostname = "172.27.226.250"; // Adres serwera
        int port = 1234; // Port serwera

        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Połączono z serwerem. Wprowadź komendę logowania (np. 'LOGIN user1 pass1'):");

            String userInput = stdIn.readLine();
            out.println(userInput);

            String response = in.readLine();
            System.out.println("Odpowiedź serwera: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}