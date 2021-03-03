package ru.itis.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class App extends Application {
    private Socket socket;

    private static ProtocolInputStream in;
    private static ProtocolOutputStream out;

    private int state;

    public static final int MENU = 0;
    public static final int WAITING = 1;
    public static final int GAME = 2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        socket = new Socket(InetAddress.getLocalHost(), Protocol.PORT);
        in = new ProtocolInputStream(socket.getInputStream());
        out = new ProtocolOutputStream(socket.getOutputStream());
        Parent menu = FXMLLoader.load(getClass().getResource("/menuStage.fxml"));
        Parent game = FXMLLoader.load(getClass().getResource("/gameStage.fxml"));
        VBox rooms = (VBox) menu.lookup("#rooms_menu");
        Node waiting = menu.lookup("#waiting");
        Label status = (Label) game.lookup("#status");
        Button create = (Button) menu.lookup("#createBtn");
        TextField roomName = (TextField) menu.lookup("#roomName");
        create.setOnAction((e) -> {
            try {
                App.getOut().writeMessage(new Message(Protocol.CREATE_ROOM, roomName.getText().getBytes()));
            } catch (IOException ioException) {
                //ignore
            }
        });
        Scene menuScene = new Scene(menu);
        Scene gameScene = new Scene(game);
        primaryStage.setScene(menuScene);
        primaryStage.show();
        System.out.println("ok");
        Thread connectionHandler = new Thread(() -> {
            try {
                App.getOut().writeMessage(new Message(Protocol.GET_ROOMS));
            } catch (IOException ioException) {
                //ignore
            }
            Message m = null;
            try {
                m = in.readMessage();
            } catch (IOException e) {
                m = null;
            }
            state = MENU;
            while (m != null) {
                if (m.getType() == Protocol.ADD_ROOM) {

                    ByteBuffer buffer = ByteBuffer.wrap(m.getData());
                    int id = buffer.getInt();
                    byte[] nameBytes = new byte[buffer.remaining()];
                    buffer.get(nameBytes);
                    Platform.runLater(() -> {
                        rooms.getChildren().add(new RoomNode(id, new String(nameBytes)));
                    });
                }
                if (m.getType() == Protocol.REMOVE_ROOM) {

                    ByteBuffer buffer = ByteBuffer.wrap(m.getData());
                    int id = buffer.getInt();
                    Platform.runLater(() -> {
                        rooms.getChildren().remove(new RoomNode(id, ""));
                    });
                }
                if (m.getType() == Protocol.JOIN_TO_ROOM) {
                    state = WAITING;
                    waiting.setVisible(true);
                    rooms.setVisible(false);
                }
                if (m.getType() == Protocol.START_GAME) {
                    state = GAME;
                    Platform.runLater(() ->{
                        primaryStage.setScene(gameScene);
                    });

                }
                if (m.getType() == Protocol.LOSE) {
                    Platform.runLater(() -> {
                        status.setText("Вы проиграли");
                    });

                }
                if (m.getType() == Protocol.WIN) {
                    Platform.runLater(() -> {
                        status.setText("Вы выиграли");
                    });

                }
                if (m.getType() == Protocol.SIGN_PLACED) {
                    ByteBuffer buffer = ByteBuffer.wrap(m.getData());
                    int x = buffer.get();
                    int y = buffer.get();
                    int sign = buffer.get();
                    Platform.runLater(() -> {
                        ImageView view = (ImageView) game.lookup("#tile_" + x + "_" + y);
                        if (sign == Protocol.TIC_PLAYER) {
                            view.setImage(new Image(String.valueOf(getClass().getResource("/krestik.png"))));
                        }
                        if (sign == Protocol.TAC_PLAYER) {
                            view.setImage(new Image(String.valueOf(getClass().getResource("/nolik.png"))));
                        }
                    });

                }
                try {
                    m = in.readMessage();
                } catch (IOException e) {
                    m = null;
                }
            }
        });
        connectionHandler.setDaemon(true);
        connectionHandler.start();
    }

    public static ProtocolInputStream getIn() {
        return in;
    }

    public static ProtocolOutputStream getOut() {
        return out;
    }
}
