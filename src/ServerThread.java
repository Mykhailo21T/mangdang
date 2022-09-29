import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerThread  extends Thread{
    ArrayList<Socket> clientSockets;
    Socket socket;
    DataOutputStream outToClient;
    String sentence = "";
    ArrayList<Player> playerArrayList;
    Map<String,Socket> navne = new HashMap<>();
    Map<String,int[]> positioner = new HashMap<>();


    public ServerThread(ArrayList<Socket> clientSockets, Socket socket, ArrayList<Player> players, HashMap<String,Socket> navne, HashMap<String, int[]> positioner) throws IOException {
        this.clientSockets = clientSockets;
        this.socket = socket;
        this.playerArrayList = players;
        this.positioner = positioner;
        this.navne = navne;
    }


    public synchronized void gennemgang(ArrayList<Socket> sockets) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        sentence = buffer.readLine();
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

        while (true) {
            try {
                gennemgang(clientSockets);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    }


