package module1;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static int port = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port);) {

            System.out.println("Waiting for client......");

            Socket clienSocket = serverSocket.accept();
            System.out.println("Client connected....");

            DataInputStream dis = new DataInputStream(clienSocket.getInputStream());

            String filename = dis.readUTF();
            Long size = dis.readLong();

            System.out.println(filename + " " + size + " Bytes");

            FileOutputStream fos = new FileOutputStream("module1/server_" + filename);

            byte[] buffer = new byte[4096];
            long bytesRemaining = size;
            int bytesRead;

            while (bytesRemaining > 0
                    && (bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, bytesRemaining))) != -1) {
                fos.write(buffer, 0, bytesRead);
                bytesRemaining -= bytesRead;
            }

            fos.close();

            System.out.println("File saved successfully!");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}