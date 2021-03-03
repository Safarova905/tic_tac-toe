package ru.itis.client;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public class RoomNode extends HBox {
    private int roomId;
    private String name;

    public RoomNode(int id, String name){
        roomId = id;
        this.name = name;
        Label roomName = new Label(name);
        Button join = new Button();
        join.setText("join");
        join.setOnAction((e) -> {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(4);
                buffer.putInt(roomId);
                App.getOut().writeMessage(new Message(Protocol.JOIN_TO_ROOM, buffer.array()));
            } catch (IOException ioException) {
                //ignore
            }
        });
        getChildren().add(roomName);
        getChildren().add(join);
    }

    public int getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomNode roomNode = (RoomNode) o;
        return roomId == roomNode.roomId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId);
    }
}
