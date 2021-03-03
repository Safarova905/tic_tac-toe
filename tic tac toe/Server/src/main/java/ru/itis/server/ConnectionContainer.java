package ru.itis.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConnectionContainer {
    private List<Connection> connections;

    public ConnectionContainer() {
        connections = new LinkedList<>();
    }

    public void addConnection(Connection c){
        connections.add(c);
    }

    public void removeConnection(Connection c){
        connections.remove(c);
    }

    public void clear(){
        ArrayList<Connection> connectionsToRemove = new ArrayList<>();
        for(Connection c : connections){
            if(!c.isAlive()){
                connectionsToRemove.add(c);
            }
        }
        connections.removeAll(connectionsToRemove);
    }

    public void messageToAll(Message m){
        for(Connection c : connections){
            c.sendMessage(m);
        }
        clear();
    }
}
