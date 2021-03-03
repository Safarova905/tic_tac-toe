package ru.itis.game;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RoomContainer {
    private LinkedList<Room> rooms;

    private int idToGive;

    public RoomContainer(){
        rooms = new LinkedList<>();
        idToGive = 0;
    }

    public void addRoom(Room r){
        r.setId(idToGive++);
        rooms.add(r);
    }

    public void removeRoom(Room r){
        rooms.remove(r);
    }

    public void clear(){
        ArrayList<Room> roomsToRemove = new ArrayList<>();
        for(Room r : rooms){
            if(r.getState() == Room.FINISHED){
                roomsToRemove.add(r);
            }
        }
        rooms.removeAll(roomsToRemove);
    }

    public Room findRoomById(int id){
        for(Room r : rooms){
            if(r.getId() == id){
                return r;
            }
        }
        return null;
    }

    public List<byte[]> roomsToBytes(){
        List<byte[]> b = new ArrayList<>();
        for(int i = 0; i < rooms.size(); i++){
            Room room = rooms.get(i);
            if(room.getState() == Room.WAITING) {

                ByteBuffer buffer = ByteBuffer.allocate(4 + room.getName().getBytes().length);
                buffer.putInt(room.getId());
                buffer.put(room.getName().getBytes());
                b.add(buffer.array());
            }
        }
        return b;
    }
}
