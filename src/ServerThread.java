import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerThread extends Thread {
    ArrayList<Socket> clientSockets;
    Socket socket;
    DataOutputStream outToClient;

    ArrayList<Player> playerArrayList;
    Map<String, Socket> navne = new HashMap<>();
    Map<String, int[]> positioner = new HashMap<>();


    public ServerThread(ArrayList<Socket> clientSockets, Socket socket, ArrayList<Player> players, HashMap<String, Socket> navne, HashMap<String, int[]> positioner) throws IOException {
        this.clientSockets = clientSockets;
        this.socket = socket;
        this.playerArrayList = players;
        this.positioner = positioner;
        this.navne = navne;
    }


    public synchronized void gennemgang(ArrayList<Socket> sockets) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String sentence = buffer.readLine();
        if(!sentence.trim().equals("")) { //ingen tom string (der var nogle gang tom string som er kommet)
            for (Socket s : sockets) {
                try {
                    outToClient = new DataOutputStream(s.getOutputStream());
                    outToClient.writeBytes(sentence + "\n");
                    outToClient.flush();
                    System.out.println("gennemgang " + sentence);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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


