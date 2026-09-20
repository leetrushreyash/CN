package module2_shared;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class TextEditorServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080);) {
            StringBuilder file = new StringBuilder();

            AtomicInteger c = new AtomicInteger(0);

            System.out.println("Server Started on port 8080");

            while (true) {

                Socket clientSocket = serverSocket.accept();

                System.out.println("Client " + c.getAndIncrement() + " got connected");

                Handler handler = new Handler(clientSocket, file);
                new Thread(handler).start();
            }

        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

class Handler implements Runnable {

    private Socket clientSocket;
    private StringBuilder file;

    public Handler(Socket clientSocket, StringBuilder file) {
        this.clientSocket = clientSocket;
        this.file = file;
    }

    @Override
    public void run() {

        try (DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());) {

            String command = dis.readUTF();

            if (command.equalsIgnoreCase("read")) {

                dos.writeUTF(file.toString());

            }

            else if (command.equals("append")) {
                String text = dis.readUTF();
                append(file, text);
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    synchronized public void append(StringBuilder file, String text) {
        file.append(text);
        file.append("\n");
    }

}
