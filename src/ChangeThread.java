
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

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
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String strng = br.readLine();
            String[] stringSplit = strng.split(" ");
            String players = stringSplit[2];
            System.out.println(stringSplit);
            if(stringSplit[2].contentEquals("[]")){
                Platform.runLater(() ->  gui.opretPlayer(stringSplit[1],9,4,"up"));

            }
            System.out.println(strng);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message = bufferedReader.readLine();
                Platform.runLater(()->gui.movePleyers(message));
                System.out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

//Platform.runLater(() -> {
//                   if(gui.getId()==0){
//                       System.out.println("hejsa");
//                       System.out.println(message);
//                       gui.setId(message);
//                   }else {
//                       gui.opretSpiller(message, id);
//                   }
// });
