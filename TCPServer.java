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
            System.out.println(connectionsockets);
            (new ServerThread(connectionsockets, connectionSocket)).start();
            DataOutputStream dataOutputStream = new DataOutputStream(connectionSocket.getOutputStream());
            dataOutputStream.writeBytes("" + n + "\n");
            n++;
        }
    }

}
