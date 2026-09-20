package module2_shared;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class TextEditorClient {

    static String add = "127.0.0.1";
    static int Port = 8080;

    public static void main(String[] args) {

        try (Socket socket = new Socket(add, Port);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                Scanner scanner = new Scanner(System.in);
                DataInputStream dis = new DataInputStream(socket.getInputStream())) {

            System.out.println("Enter your commands: ");

            String command = scanner.nextLine();

            System.out.println("Sending command to server....");

            dos.writeUTF(command);

            if (command.equalsIgnoreCase("read")) {

                String in;
                in = dis.readUTF();
                System.out.println(in);

            }

            else if (command.equalsIgnoreCase("append")) {

                System.out.println("Enter your text here: ");
                String text = scanner.nextLine();
                dos.writeUTF(text);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
