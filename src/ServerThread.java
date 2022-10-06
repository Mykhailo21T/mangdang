import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread {

    Socket socket;
    DataOutputStream outToClient;
    BufferedReader buffer;
    String sentence;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
    }

    public void gennemgang(String sentence) throws IOException {

        for (Socket s : TCPServer.getSockets()) {
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

    public void run() {

        try {
            buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                sentence = buffer.readLine();
                gennemgang(sentence);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


