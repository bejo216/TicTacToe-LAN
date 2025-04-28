import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("192.168.0.19", 1234); // Replace with server IP

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String message;

        while ((message = userInput.readLine()) != null) {
            out.println(message);
            System.out.println("Server replied: " + in.readLine());
        }

        socket.close();
    }
}
