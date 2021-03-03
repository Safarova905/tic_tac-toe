package ru.itis.server;

import ru.itis.game.RoomContainer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ConnectionContainer connections;
    private ServerSocket serverSocket;
    private boolean exit;
    private RoomContainer rooms;



    public void init(){
        connections = new ConnectionContainer();
        rooms = new RoomContainer();
        exit = false;
        try {
            serverSocket = new ServerSocket(Protocol.PORT);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void start(){
        Thread connectionAcceptThread = new Thread(() -> {
            while (!exit) {
                Socket s;
                try {
                    s = serverSocket.accept();
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
                connections.addConnection(new Connection(s, this));
            }
        });
        connectionAcceptThread.start();
    }



    public void removeConnection(Connection c){
        connections.removeConnection(c);
    }

    public ConnectionContainer getConnections() {
        return connections;
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.init();
        server.start();
    }

    public RoomContainer getRooms() {
        return rooms;
    }
}
