package module3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class DBClient {

    public static void main(String[] args) {

        try (Socket socket = new Socket("127.0.0.1", 8080);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                Scanner sc = new Scanner(System.in)) {

            System.out.println("Input Format: <Method> <Body>");

            while (true) {

                String in = sc.nextLine();

                if (in.equalsIgnoreCase("quit")) {
                    System.out.println("Quitting...");
                    break;
                }

                dos.writeUTF(in);

                String[] ins = in.split(" ", 3);

                if (ins[0].equalsIgnoreCase("get") || ins[0].equalsIgnoreCase("info")) {
                    String out = dis.readUTF();
                    System.out.println(out);
                }

                else if (ins[0].equalsIgnoreCase("put")) {
                    System.out.println("key value sent to server");
                    String out = dis.readUTF();
                    System.out.println("Server replied: " + out);
                }

            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
