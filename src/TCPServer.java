import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TCPServer {
    public static Map<String,Socket> navne = new HashMap<>();
    public static Map<String,int[]> positioner = new HashMap<>();


    public static void main(String[] args) throws Exception {
        ServerSocket welcomSocket = new ServerSocket(6789);
        ArrayList<Socket> connectionsockets = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<>();
        HashMap<String,Socket> navne = new HashMap<>();
        HashMap<String,int[]> positioner = new HashMap<>();
        int n = 1;
        while (true) {
            Socket connectionSocket = welcomSocket.accept();
            connectionsockets.add(connectionSocket);
            (new ServerThread(connectionsockets, connectionSocket,players,navne,positioner)).start();//oprettes snakke tråde til en klijent
            n++;
        }
    }

}
//DataOutputStream dataOutputStream = new DataOutputStream(connectionSocket.getOutputStream());
//            dataOutputStream.writeBytes("id" + n + "\n");//sendes id
//            System.out.println(connectionsockets);        //men output er forkert
