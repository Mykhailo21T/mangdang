import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread  extends Thread{
    ArrayList<Socket> clientSockets;
    Socket socket;
    DataOutputStream outToClient;
    String sentence = "";

    public ServerThread(ArrayList<Socket> clientSockets, Socket socket) throws IOException {
        this.clientSockets = clientSockets;
        this.socket = socket;
    }


    public synchronized void gennemgang(ArrayList<Socket> sockets) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        sentence = buffer.readLine();
        System.out.println(socket.getInetAddress());
        for (Socket s : sockets) {
            try {
                outToClient = new DataOutputStream(s.getOutputStream());
                outToClient.writeBytes(sentence + "\n");
                outToClient.flush();
                System.out.println(sentence);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void sendId(ArrayList<Socket> sockets) {
        for (Socket s : sockets) {
            try {
                int id = sockets.indexOf(s) + 1;
                outToClient = new DataOutputStream(s.getOutputStream());
                outToClient.writeBytes("" + id + "\n");
                //outToClient.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void run() {
        try {
        outToClient = new DataOutputStream(socket.getOutputStream());
        outToClient.writeBytes("");
        }catch (IOException e){
            e.printStackTrace();
        }
        while (true) {
                try {
                    sendId(clientSockets);
                    gennemgang(clientSockets);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


