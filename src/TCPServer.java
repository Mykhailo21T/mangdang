import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer {
    private static ArrayList<Socket> connectionsockets ;
    private static ServerSocket welcomSocket;

    public static void main(String[] args) throws Exception {
        connectionsockets = new ArrayList<>();
        welcomSocket = new ServerSocket(6789);
        while (true) {
            Socket connectionSocket = welcomSocket.accept();
            connectionsockets.add(connectionSocket);
            (new ServerThread(connectionSocket)).start();//oprettes snakke tråde til en klijent
        }
    }

    public synchronized static ArrayList<Socket> getSockets() {
        return connectionsockets;
    }
}
