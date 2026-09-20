package module1;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

public class Client {
    static String serverAddress = "127.0.0.1";
    static int port = 8080;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(serverAddress, port)) {
            System.out.println("connected to server");

            // send the meta data to server

            File file = new File("module1/test.txt");

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            dos.writeUTF("test.txt");
            dos.writeLong(file.length());

            // reading the file
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }

            fis.close();
            System.out.println("File sent!");
        }

        catch (Exception e) {

        }
    }
}
