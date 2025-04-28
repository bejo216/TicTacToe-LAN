import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Objects;


/*
igraju na jednom seocketu al server mora radi stalno?(komp)

 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234); // Port to listen on
        System.out.println("Server is waiting for a connection...");

        Socket socket = serverSocket.accept(); // Accept incoming connection
        System.out.println("Client connected!");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);


        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String message;
        String inputLine;
        InetAddress localhost = InetAddress.getLocalHost();




        while (true) {
            System.out.println("System IP Address : " +
                    (localhost.getHostAddress()).trim());
            

            inputLine=in.readLine();
            System.out.println("Received: " + inputLine);
            socket.getOutputStream().write("Hello from server".getBytes());
            message=userInput.readLine();
            out.println(message);
            if (Objects.equals(message, "quit")){
                break;
            }

        }

        socket.close();
        serverSocket.close();
    }
}