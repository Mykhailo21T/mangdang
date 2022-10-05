
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GUI extends Application {

    public static final int size = 20;
    public static final int scene_height = size * 20 + 100;
    public static final int scene_width = size * 20 + 200;

    public static Image image_floor;
    public static Image image_wall;
    public static Image hero_right, hero_left, hero_up, hero_down;
    private int id;

    public static Player me;
    public static List<Player> players = new ArrayList<Player>();
    private Socket serverSocket;
    private Label[][] fields;
    private TextArea scoreList;

    public static List<Player> getPlayers() {
        return players;
    }

    private String[] board = {    // 20x20
            "wwwwwwwwwwwwwwwwwwww",
            "w        ww        w",
            "w w  w  www w  w  ww",
            "w w  w   ww w  w  ww",
            "w  w               w",
            "w w w w w w w  w  ww",
            "w w     www w  w  ww",
            "w w     w w w  w  ww",
            "w   w w  w  w  w   w",
            "w     w  w  w  w   w",
            "w ww ww        w  ww",
            "w  w w    w    w  ww",
            "w        ww w  w  ww",
            "w         w w  w  ww",
            "w        w     w  ww",
            "w  w              ww",
            "w  w www  w w  ww ww",
            "w w      ww w     ww",
            "w   w   ww  w      w",
            "wwwwwwwwwwwwwwwwwwww"
    };


    // -------------------------------------------
    // | Maze: (0,0)              | Score: (1,0) |
    // |-----------------------------------------|
    // | boardGrid (0,1)          | scorelist    |
    // |                          | (1,1)        |
    // -------------------------------------------

    int[] position1 = {9, 4};
    int[] position2 = {14, 15};
    int[] position3 = {2, 15};
    int[] position;

    public void setId(String message) {
        id = Integer.parseInt(message);
    }

    public int getId() {
        return id;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            serverSocket = new Socket("10.10.139.53", 6789);//"10.10.139.128" 192.168.2.82
            DataOutputStream outputStream = new DataOutputStream(serverSocket.getOutputStream());

            ChangeThread changeThread = new ChangeThread(serverSocket, this);
            changeThread.start();

            System.out.println("Skriv dit navn:");
            BufferedReader brNavn = new BufferedReader(new InputStreamReader(System.in));
            String navnPlayer = brNavn.readLine();

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(0, 10, 0, 10));

            Text mazeLabel = new Text("Maze:");
            mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            Text scoreLabel = new Text("Score:");
            scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            scoreList = new TextArea();

            GridPane boardGrid = new GridPane();

            image_wall = new Image(getClass().getResourceAsStream("Image/wall4.png"), size, size, false, false);
            image_floor = new Image(getClass().getResourceAsStream("Image/floor1.png"), size, size, false, false);

            hero_right = new Image(getClass().getResourceAsStream("Image/heroRight.png"), size, size, false, false);
            hero_left = new Image(getClass().getResourceAsStream("Image/heroLeft.png"), size, size, false, false);
            hero_up = new Image(getClass().getResourceAsStream("Image/heroUp.png"), size, size, false, false);
            hero_down = new Image(getClass().getResourceAsStream("Image/heroDown.png"), size, size, false, false);

            fields = new Label[20][20];
            for (int j = 0; j < 20; j++) {
                for (int i = 0; i < 20; i++) {
                    switch (board[j].charAt(i)) {
                        case 'w':
                            fields[i][j] = new Label("", new ImageView(image_wall));
                            break;
                        case ' ':
                            fields[i][j] = new Label("", new ImageView(image_floor));
                            break;
                        default:
                            throw new Exception("Illegal field value: " + board[j].charAt(i));
                    }
                    boardGrid.add(fields[i][j], i, j);
                }
            }
            scoreList.setEditable(false);
            ///

            grid.add(mazeLabel, 0, 0);
            grid.add(scoreLabel, 1, 0);
            grid.add(boardGrid, 0, 1);
            grid.add(scoreList, 1, 1);

            Scene scene = new Scene(grid, scene_width, scene_height);
            primaryStage.setScene(scene);
            primaryStage.show();

            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                switch (event.getCode()) {
                    case UP:

                        try {
                            outputStream.writeBytes(me.name + "/" + me.getXpos() + "/" + me.getYpos() + "/0/-1/up/" + "\n");//"\n" var fjernet for at undgå fordobling

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case DOWN:

                        try {
                            outputStream.writeBytes(me.name + "/" + me.getXpos() + "/" + me.getYpos() + "/0/+1/down/" + "\n");//"\n" var fjernet for at undgå fordobling

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LEFT:

                        try {
                            outputStream.writeBytes(me.name + "/" + me.getXpos() + "/" + me.getYpos() + "/-1/0/left/" + "\n");//"\n" var fjernet for at undgå fordobling

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case RIGHT:

                        try {
                            outputStream.writeBytes(me.name + "/" + me.getXpos() + "/" + me.getYpos() + "/+1/0/right/" + "\n");//"\n" var fjernet for at undgå fordobling

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case SPACE:

                        try {
                            skyd();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            });

            // Setting up standard players

            opretMe(navnPlayer);

            scoreList.setText(getScoreList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void skyd() throws IOException {
        String retning = me.direction;
        int tempPosX = me.getXpos();
        int tempPosY = me.getYpos();
        int nyX = (int) (Math.random() * 18) + 1;
        int nyY = (int) (Math.random() * 18) + 1;
        for (Player p : players) {
            if (p != me && retning.equals("up") && p.xpos == tempPosX && p.ypos < tempPosY) {
                fields[p.getXpos()][p.getYpos()].setGraphic(new ImageView(image_floor));
                p.setXpos(nyX);
                p.setYpos(nyY);
                DataOutputStream outputStream = new DataOutputStream(serverSocket.getOutputStream());
                outputStream.writeBytes(p.name + "/" + nyX + "/" + nyY + "/0/0/down/" + "\n");
                System.out.printf("%s skydet", p.name);
            } else if (p != me && retning.equals("right") && p.xpos > tempPosX && p.ypos == tempPosY) {
                fields[p.getXpos()][p.getYpos()].setGraphic(new ImageView(image_floor));
                p.setXpos(nyX);
                p.setYpos(nyY);
                DataOutputStream outputStream = new DataOutputStream(serverSocket.getOutputStream());
                outputStream.writeBytes(p.name + "/" + nyX + "/" + nyY + "/0/0/down/" + "\n");
                System.out.printf("%s skydet", p.name);
            } else if (p != me && retning.equals("down") && p.xpos == tempPosX && p.ypos > tempPosY) {
                fields[p.getXpos()][p.getYpos()].setGraphic(new ImageView(image_floor));
                p.setXpos(nyX);
                p.setYpos(nyY);
                DataOutputStream outputStream = new DataOutputStream(serverSocket.getOutputStream());
                outputStream.writeBytes(p.name + "/" + nyX + "/" + nyY + "/0/0/down/" + "\n");
                System.out.printf("%s skydet", p.name);
            } else if (p != me && retning.equals("left") && p.xpos > tempPosX && p.ypos == tempPosY) {
                fields[p.getXpos()][p.getYpos()].setGraphic(new ImageView(image_floor));
                p.setXpos(nyX);
                p.setYpos(nyY);
                DataOutputStream outputStream = new DataOutputStream(serverSocket.getOutputStream());
                outputStream.writeBytes(p.name + "/" + nyX + "/" + nyY + "/0/0/down/" + "\n");
                System.out.printf("%s skydet", p.name);
            }
        }
    }

    /**
     * public void playerMoved(int delta_x, int delta_y, String direction, Player p) {
     * tilføjet xx og yy coordinater som sendes nu også i beskedet og som skal ikke længere bruges fra p.getx og p.gety
     */

    public void playerMoved(int xX, int yY, int delta_x, int delta_y, String direction, Player p) {

        int checkX = Math.abs(p.getXpos() - xX);
        int checkY = Math.abs(p.getYpos() - yY);
        if (Math.max(checkX, checkY) > 1) {
            fields[p.getXpos()][p.getYpos()].setGraphic(new ImageView(image_floor));
        }
        /** overstående er tjekker hvis player skydeSpawnet og fjerne den gamle billede fra bordet*/

        p.direction = direction;
        int x = xX, y = yY;

        if (board[y + delta_y].charAt(x + delta_x) == 'w') {
            p.addPoints(-1);
        } else {
            Player pL = getPlayerAt(x + delta_x, y + delta_y);
            if (pL != null) {
                p.addPoints(10);
                pL.addPoints(-10);
            } else {
                p.addPoints(1);

                fields[x][y].setGraphic(new ImageView(image_floor));
                x += delta_x;
                y += delta_y;

                if (direction.equals("right")) {
                    fields[x][y].setGraphic(new ImageView(hero_right));
                }
                ;
                if (direction.equals("left")) {
                    fields[x][y].setGraphic(new ImageView(hero_left));
                }
                ;
                if (direction.equals("up")) {
                    fields[x][y].setGraphic(new ImageView(hero_up));
                }
                ;
                if (direction.equals("down")) {
                    fields[x][y].setGraphic(new ImageView(hero_down));
                }
                ;

                p.setXpos(x);
                p.setYpos(y);
            }
        }
        scoreList.setText(getScoreList());
    }

    public String getScoreList() {
        StringBuffer b = new StringBuffer(100);
        for (Player p : players) {
            b.append(p + "\r\n");// fejl ved output som created ny spiller

        }
        return b.toString();
    }

    public Player getPlayerAt(int x, int y) {
        for (Player p : players) {
            if (p.getXpos() == x && p.getYpos() == y) {
                System.out.println(p.name);
                return p;
            }
        }
        return null;
    }

    public void movePleyers(int xX, int yY, String str, int deltaX, int deltaY, String direktion) {
        for (Player p : players) {
            if (p.name.equals(str)) {
                playerMoved(xX, yY, deltaX, deltaY, direktion, p);
            }
        }
    }

    public void opretPlayer(String navn, int xPos, int yPos, String direction) throws IOException {

        Player player = new Player(navn, xPos, yPos, direction);
        players.add(player);
        fields[xPos][yPos].setGraphic(new ImageView(hero_up));
        System.out.printf("player %s created\n", navn);

    }

    public void opretMe(String navn) {
        int randX = (int) (Math.random() * 18) + 1;
        int randY = (int) (Math.random() * 18) + 1;
        me = new Player(navn, randX, randY, "up");
        players.add(me);
        fields[randX][randY].setGraphic(new ImageView(hero_up));
        System.out.printf("player %s created as me\n", navn);
    }

    public int getAntalPlayers(){
        return players.size();
    }
}