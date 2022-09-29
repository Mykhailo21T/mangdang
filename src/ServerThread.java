import javax.xml.crypto.Data;
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
    ArrayList<Player> playerArrayList;

    public ServerThread(ArrayList<Socket> clientSockets, Socket socket, ArrayList<Player> players) throws IOException {
        this.clientSockets = clientSockets;
        this.socket = socket;
        this.playerArrayList = players;
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
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String temp = br.readLine();
            System.out.println(temp);
            if(temp == "antal"){
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeBytes(""+playerArrayList.toString()+"\n");
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        while (true) {
                try {
                    //sendId(clientSockets);
                    gennemgang(clientSockets);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


