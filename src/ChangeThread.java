
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ChangeThread extends Thread {
    Socket socket;
    GUI gui;
    BufferedReader bufferedReader;
    String message;
    private final int antalSpillere = 2;
    public ChangeThread(Socket socket, GUI gui) {
        this.socket = socket;
        this.gui = gui;

    }

    @Override
    public void run() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                message = bufferedReader.readLine();
                gennemgang(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean findPlayer(String navn) {

        for (Player p : gui.getPlayers()) {
            if (p.name.equals(navn)) {
                return true; //break;
            }
        }
        return false;
    }

    private void gennemgang(String message) {
        String[] stringSplittet = message.split("/");
        Platform.runLater(() -> {
            if (gui.getAntalPlayers() <antalSpillere && !findPlayer(stringSplittet[0]) && stringSplittet[0].trim().length() > 0) {//new player som er lige joinet oprettes
                try {
                    gui.opretPlayer(stringSplittet[0], Integer.parseInt(stringSplittet[1]), Integer.parseInt(stringSplittet[2]), "up");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Arrays.toString(stringSplittet));
            gui.movePleyers(Integer.parseInt(stringSplittet[1]), Integer.parseInt(stringSplittet[2]),
                    stringSplittet[0], Integer.parseInt(stringSplittet[3]), Integer.parseInt(stringSplittet[4]), stringSplittet[5]);
            /** posX,posY,navn,n,n,direktion */
        });
    }
}
