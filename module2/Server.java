package module2;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static int port = 8080;

    public static void main(String[] args) {

        try (ServerSocket serversSocket = new ServerSocket(port);) {
            while (true) {
                Socket clientSocket = serversSocket.accept();
                Handler handler = new Handler(clientSocket);
                new Thread(handler).start();
            }

        } catch (Exception e) {

        }
    }

}

class Handler implements Runnable {

    private Socket clientSocket;

    public Handler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(clientSocket.getInputStream());) {

            String filename = dis.readUTF();
            Long size = dis.readLong();

            System.out.println(filename + " " + size + " Bytes");

            FileOutputStream fos = new FileOutputStream("server_" + filename);

            long bytesRemaining = size;
            int byteRead;
            byte[] buffer = new byte[4096];

            while (bytesRemaining > 0l &&
                    (byteRead = dis.read(buffer, 0, (int) Math.min(buffer.length, bytesRemaining))) != -1) {
                fos.write(buffer, 0, byteRead);
                bytesRemaining -= byteRead;
            }

            fos.close();

            System.out.println("File saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
