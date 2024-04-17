import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Server {
    ServerSocket serverSocket;
    Socket clientSocket;
    BufferedReader in;
    PrintWriter out;

    public Server() {
        try {
            serverSocket = new ServerSocket(7777);
            System.out.println("Sever is ready to accept the connections");
            System.out.println("Waiting for client connections");
            clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            startReading();
            startWriting();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reading from client");
            try {
                while (true) {
                    String message = in.readLine();
                    if (message.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        clientSocket.close();
                        break;
                    }
                    System.out.println("Client: " + message);
                }
            } catch (Exception e) {
                System.out.println("Connection closed");
            }
        };

        new Thread(r1).start();
    }

    private void startWriting() {
        Runnable r2 = () -> {
            System.out.println("Writing to client");
            try {
                while (!clientSocket.isClosed()) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                    String message = input.readLine();
                    out.println(message);
                    out.flush();
                    if (message.equals("exit")) {
                        clientSocket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Connection closed");
            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Server is starting..");
        new Server();
    }
}
