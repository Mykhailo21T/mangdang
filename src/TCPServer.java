import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer {

    public static void main(String[] args) throws Exception {
        ServerSocket welcomSocket = new ServerSocket(6789);
        ArrayList<Socket> connectionsockets = new ArrayList<>();

        int n = 1;
        while (true) {
            Socket connectionSocket = welcomSocket.accept();
            connectionsockets.add(connectionSocket);
            (new ServerThread(connectionsockets, connectionSocket)).start();//oprettes snakke tråde til en klijent
            DataOutputStream dataOutputStream = new DataOutputStream(connectionSocket.getOutputStream());
//            dataOutputStream.writeBytes("id" + n + "\n");//sendes id
//            System.out.println(connectionsockets);        //men output er forkert
            n++;
        }
    }

}
