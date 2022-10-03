
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
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message = bufferedReader.readLine();
                String[] stringSplittet = message.split("/");
                Platform.runLater(() -> {
                    if (!findPlayer(stringSplittet[0])) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
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
}

//TODO: at spiller skal kunne bevege sig selv ud af besked navn


/**
 * try {
 * boolean fundet = false;
 * for (Player p : gui.getPlayers()) {
 * if (p.name == stringSplit[0])
 * fundet = true;
 * }
 * if (fundet = false) {
 * int xpos = Integer.parseInt(stringSplit[1]);
 * int ypos = Integer.parseInt(stringSplit[2]);
 * gui.opretPlayer(stringSplit[0], xpos, ypos, "up");
 * System.out.println(gui.getPlayerAt(9, 4));
 * }
 * } catch (IOException e) {
 * e.printStackTrace();
 * }
 *
 * hvis personens x og y koordinater stoerre and 1 forskel
 * saa den gamle position skal aendres med gulvet
 *
 */
