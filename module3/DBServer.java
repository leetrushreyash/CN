package module3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

class Record {
    String value;
    String timeCreated;

    public Record(String value, String timeCreated) {
        this.value = value;
        this.timeCreated = timeCreated;
    }
}

public class DBServer {

    static AtomicInteger c = new AtomicInteger(1);
    static ConcurrentHashMap<String, Record> map = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        try (ServerSocket socket = new ServerSocket(8080)) {

            while (true) {

                Socket clientSocket = socket.accept();
                System.out.println("Client " + c + " got connected");

                Handler handler = new Handler(clientSocket, map);

                new Thread(handler).start();

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}

class Handler implements Runnable {

    Socket clientSocket;
    ConcurrentHashMap<String, Record> map;

    public Handler(Socket clientSocket, ConcurrentHashMap<String, Record> map) {
        this.clientSocket = clientSocket;
        this.map = map;
    }

    @Override
    public void run() {

        try (
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());) {

            while (true) {
                String[] ins = dis.readUTF().split(" ", 3);

                String action = ins[0];

                if (action.equalsIgnoreCase("get")) {

                    Record output = map.get(ins[1]);
                    dos.writeUTF(output.value);
                }

                else if (action.equalsIgnoreCase("put")) {

                    LocalDateTime now = LocalDateTime.now();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    String prettyTime = now.format(formatter);

                    Record newRecord = new Record(ins[2], prettyTime);
                    map.put(ins[1], newRecord);
                    dos.writeUTF("OK");

                }

                else if (action.equalsIgnoreCase("info")) {

                    Record rec = map.get(ins[1]);

                    dos.writeUTF(rec.value + " (Created at: " + rec.timeCreated + ")");
                }

                else if (action.equalsIgnoreCase("quit")) {
                    dos.writeUTF("Goodbye!");
                    break;
                }

                else {
                    dos.writeUTF("ERROR: Unknown command");
                }
            }
        } catch (Exception e) {
            System.out.println("Client disconnected.");
        }
    }

}
