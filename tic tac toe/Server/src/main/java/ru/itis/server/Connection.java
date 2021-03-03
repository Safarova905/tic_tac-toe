package ru.itis.server;

import ru.itis.game.Player;
import ru.itis.game.Room;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;

public class Connection {
    private Socket s;
    private Server server;
    private ProtocolInputStream in;
    private ProtocolOutputStream out;
    private boolean isAlive;
    private Player player;


    public Connection(Socket s, Server server) {
        this.s = s;
        this.server = server;
        isAlive = true;
        try {
            in = new ProtocolInputStream(s.getInputStream());
            out = new ProtocolOutputStream(s.getOutputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        player = new Player(this);

        Thread connectionThread = new Thread(() -> {
            while (isAlive){
                Message m = null;
                try {
                    m = in.readMessage();
                } catch (IOException ioException) {
                    isAlive = false;
                }
                if(m == null){
                    isAlive = false;
                    return;
                }
                if(m.getType() == Protocol.CREATE_ROOM){
                    Room room = new Room(m.getDataAsString(), server);
                    player.createRoom(room);
                }
                if(m.getType() == Protocol.JOIN_TO_ROOM){
                    ByteBuffer buffer = ByteBuffer.wrap(m.getData());
                    player.ConnectToRoomById(buffer.getInt());
                }
                if(m.getType() == Protocol.PLACE_SIGN){
                    ByteBuffer buffer = ByteBuffer.wrap(m.getData());
                    player.placeSign(buffer.getInt(), buffer.getInt());
                }
                if(m.getType() == Protocol.EXIT_FROM_ROOM){
                    player.disconnect();
                }
                if(m.getType() == Protocol.GET_ROOMS){
                    List<byte[]> bytes = server.getRooms().roomsToBytes();
                    for (byte[] aByte : bytes) {
                        sendMessage(new Message(Protocol.ADD_ROOM, aByte));
                    }
                }
            }
        });
        connectionThread.start();
    }

    public void closeConnection(){
        try {
            s.close();
        } catch (IOException ioException) {
            //ignore
        }
        isAlive = false;
        server.removeConnection(this);
    }

    public void sendMessage(Message m){
        try {
            out.writeMessage(m);
        } catch (IOException ioException) {
            isAlive = false;
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public Server getServer() {
        return server;
    }
}
