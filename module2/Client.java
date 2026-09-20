package module2;

import java.io.DataOutputStream;
import java.net.Socket;
import java.io.File;
import java.io.FileInputStream;

public class Client {

    static String addr = "127.0.0.1";
    static int port = 8080;

    public static void main(String[] args) {
        try (Socket socket = new Socket(addr, port)) {

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            String filename = "module2/test.txt";
            File file = new File(filename);

            Long size = file.length();

            // sending the meta data
            dos.writeUTF(file.getName());
            dos.writeLong(size);

            FileInputStream fis = new FileInputStream(file);

            byte[] buffer = new byte[4096];

            int byteRead;

            while ((byteRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, byteRead);
            }

            dos.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
