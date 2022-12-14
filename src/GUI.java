
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
import java.util.Arrays;
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

    public void udprint() {
        System.out.println("123");
    }

    /**
     * public void opretSpiller(String navn, int id) {
     * if(id==1) {
     * position = position1;
     * } else if (id==2){
     * position=position2;
     * } else
     * position=position3;
     * me = new Player(navn, position[0], position[1], "up");
     * players.add(me);
     * fields[position[0]][position[1]].setGraphic(new ImageView(hero_up));
     * }
     */

    public void setId(String message) {
        id = Integer.parseInt(message);
    }

    public int getId() {
        return id;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            serverSocket = new Socket("10.10.139.128", 6789);//"10.10.139.128" 192.168.2.82
            DataOutputStream outputStream = new DataOutputStream(serverSocket.getOutputStream());

            ChangeThread changeThread = new ChangeThread(serverSocket, this);
            changeThread.start();

            BufferedReader brNavn = new BufferedReader(new InputStreamReader(System.in));
            String navnPlayer = brNavn.readLine();
            DataOutputStream outputStream1 = new DataOutputStream(serverSocket.getOutputStream());
            outputStream1.writeBytes("new " + navnPlayer + "\n");


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
                        //playerMoved(0, -1, "up");
                        try {
                            outputStream.writeBytes(me.name + "/" + me.getXpos() + "/" + me.getYpos() + "/0/-1/up/" + getScoreList() + "\n");
//							System.out.println("up");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case DOWN:
                        //playerMoved(0, +1, "down");
                        try {
                            outputStream.writeBytes(me.name + "/" + me.getXpos() + "/" + me.getYpos() + "/0/+1/down/" + getScoreList()+ "\n");
//							System.out.println("down");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LEFT:
                        //playerMoved(-1, 0, "left");
                        try {
                            outputStream.writeBytes(me.name + "/" + me.getXpos() + "/" + me.getYpos() + "/-1/0/left/" + getScoreList()+ "\n");
//							System.out.println("left");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case RIGHT:
                        //playerMoved(+1, 0, "right");
                        try {
                            outputStream.writeBytes(me.name + "/" + me.getXpos() + "/" + me.getYpos() + "/+1/0/right/" + getScoreList()+ "\n");
//							System.out.println("right");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            });

            // Setting up standard players

            /**me = new Player(navnPlayer, 9, 4, "up");
            players.add(me);
            fields[9][4].setGraphic(new ImageView(hero_up));*/


            scoreList.setText(getScoreList());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void playerMoved(int delta_x, int delta_y, String direction,Player p) {
        p.direction = direction;
        int x = p.getXpos(), y = me.getYpos();

        if (board[y + delta_y].charAt(x + delta_x) == 'w') {
            p.addPoints(-1);
        } else {
            Player pL = getPlayerAt(x + delta_x, y + delta_y);
            if (pL != null) {
                p.addPoints(10);
                p.addPoints(-10);
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
            b.append(p + "\r\n");
        }
        return b.toString();
    }

    public Player getPlayerAt(int x, int y) {
        for (Player p : players) {
            if (p.getXpos() == x && p.getYpos() == y) {
                return p;
            }
        }
        return null;
    }

    public void movePleyers(String str) {
        String[] movements = str.split("/");
        System.out.println(Arrays.toString(movements));
        for (Player p : players) {
            int x = Integer.parseInt(movements[3]);
            int y = Integer.parseInt(movements[4]);
            playerMoved(x, y, movements[5],p);
        }
    }

    public Player opretPlayer(String navn, int xPos, int yPos, String direction) throws IOException {

        me = new Player(navn, xPos, yPos, direction);
        players.add(me);
        fields[xPos][yPos].setGraphic(new ImageView(hero_up));
        DataOutputStream send = new DataOutputStream(serverSocket.getOutputStream());
        send.writeBytes(navn + " " + xPos + " " + yPos + " " + direction + "\n");
        return me;
    }

}

//		Player playerNew = new Player(navn,xPos,yPos,direction);
//		players.add(playerNew);
//		fields[xPos][yPos].setGraphic(new ImageView(hero_up));
//		return playerNew;