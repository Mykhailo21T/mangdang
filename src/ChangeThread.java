
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ChangeThread extends Thread {
    Socket socket;
    GUI gui;
    Player player;
    int id = 0;

    public ChangeThread(Socket socket, GUI gui) {
        this.socket = socket;
        this.gui = gui;

    }

    @Override
    public void run() {

        while (true) {
            gennemgang();
        }
    }

    private boolean findPlayer(String str) {

        for (Player p : gui.getPlayers()) {
            if (p.name.equals(str)) {
                return true; //break;
            }
        }
        return false;
    }

    private synchronized void gennemgang(){
        try {
        if(interrupted())
            wait();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message = bufferedReader.readLine();
            String[] stringSplittet = message.split("/");
            Platform.runLater(() -> {
                if (!findPlayer(stringSplittet[0])&&stringSplittet[0].trim().length()>0 && gui.getScoreList().length()<3) {//new player som er lige joinet oprettes
                    try {
                        gui.opretPlayer(stringSplittet[0], Integer.parseInt(stringSplittet[1]), Integer.parseInt(stringSplittet[2]), "up");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Arrays.toString(stringSplittet));
                gui.movePleyers(Integer.parseInt(stringSplittet[1]),Integer.parseInt(stringSplittet[2]),
                        stringSplittet[0],Integer.parseInt(stringSplittet[3]),Integer.parseInt(stringSplittet[4]),stringSplittet[5]);
                /** posX,posY,navn,n,n,direktion */
            });
            //System.out.println(message);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        notifyAll();
    }
}
