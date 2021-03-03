package ru.itis.game;

import ru.itis.server.Connection;
import ru.itis.server.Message;
import ru.itis.server.Protocol;

import java.nio.ByteBuffer;

public class Player {
    private final Connection connection;
    private Room room;

    public Player(Connection connection) {
        this.connection = connection;
        Thread pingThread = new Thread(() -> {
            while (connection.isAlive()){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    //ignore
                }
            }
            disconnect();
        });
        pingThread.start();
    }

    public void ConnectToRoomById(int id){
        if(room != null){
            connection.sendMessage(new Message(Protocol.SEND_ERROR));
        }
        Room r = connection.getServer().getRooms().findRoomById(id);
        if(r == null){
            connection.sendMessage(new Message(Protocol.SEND_ERROR));
        }
        else {
            setRoom(r);
            if(r.getTicPlayer() != null) {
                if (r.getTacPlayer() != null) {
                    connection.sendMessage(new Message(Protocol.SEND_ERROR));
                } else {
                    r.setTacPlayer(this);
                }
            }
            else {
                r.setTicPlayer(this);
            }
            if(r.getTicPlayer() != null && r.getTacPlayer() != null){
                r.startGame();
            }
        }
    }

    public void placeSign(int x, int y){
        if(room == null){
            connection.sendMessage(new Message(Protocol.SEND_ERROR));
        }
        room.placeSign(x, y, this);
    }

    public void createRoom(Room create){
        if(room != null){
            connection.sendMessage(new Message(Protocol.SEND_ERROR));
        }
        setRoom(create);
        create.setTicPlayer(this);
        connection.getServer().getRooms().addRoom(create);
        ByteBuffer buffer = ByteBuffer.allocate(4 + create.getName().getBytes().length);
        buffer.putInt(create.getId()).put(create.getName().getBytes());
        ByteBuffer idBuffer = ByteBuffer.allocate(4);
        idBuffer.putInt(create.getId());
        connection.sendMessage(new Message(Protocol.JOIN_TO_ROOM, idBuffer.array()));
        connection.getServer().getConnections().messageToAll(new Message(Protocol.ADD_ROOM, buffer.array()));
    }

    public void disconnect(){
        if(room != null){
            room.disconnect(this);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
