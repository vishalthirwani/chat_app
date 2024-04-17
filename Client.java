import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    Socket socket;
    BufferedReader in;
    PrintWriter out;

    public Client() {
        System.out.println("Sending request to server");
        try {
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Client connected");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            startReading();
            startWriting();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reading from server");
            try {
                while (true) {
                    String message = in.readLine();
                    if (message.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Server: " + message);
                }
            } catch (Exception e) {
                System.out.println("Connection closed41");
            }
        };

        new Thread(r1).start();
    }

    private void startWriting() {
        Runnable r2 = () -> {
            System.out.println("Writing to server");
            try {
                while (!socket.isClosed()) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                    String message = input.readLine();
                    out.println(message);
                    out.flush();
                    if (message.equals("exit")) {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Connection closed63");
            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is client");
        new Client();
    }
}
