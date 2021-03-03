package ru.itis.game;

import ru.itis.server.Message;
import ru.itis.server.Protocol;
import ru.itis.server.Server;

import java.nio.ByteBuffer;

public class Room {
    private Game game;
    private Server server;

    private int state;
    private int id;

    public static final int WAITING = 0;
    public static final int GAME = 1;
    public static final int FINISHED = 2;

    private final String name;

    private Player ticPlayer;
    private Player tacPlayer;

    private int turn;

    public Room(String name, Server server) {
        this.server = server;
        this.name = name;
        game = new Game();
        turn = 0;
    }

    public void startGame(){
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(id);

        server.getConnections().messageToAll(new Message(Protocol.REMOVE_ROOM, buffer.array()));
        state = GAME;
        ticPlayer.getConnection().sendMessage(new Message(Protocol.START_GAME, new byte[]{Protocol.TIC_PLAYER}));
        tacPlayer.getConnection().sendMessage(new Message(Protocol.START_GAME, new byte[]{Protocol.TAC_PLAYER}));

    }

    public void placeSign(int x, int y, Player p){
        if(state != GAME || game.getSign(x, y) != 0){
            return;
        }
        if(turn % 2 == 0){
            if(ticPlayer == p){
                game.placeSign(x, y, Game.TIC);
                ticPlayer.getConnection().sendMessage(
                        new Message(Protocol.SIGN_PLACED, new byte[]{(byte) x, (byte) y, Protocol.TIC_PLAYER}));
                tacPlayer.getConnection().sendMessage(
                        new Message(Protocol.SIGN_PLACED, new byte[]{(byte) x, (byte) y, Protocol.TIC_PLAYER}));
                turn++;
            }
        }
        if(turn % 2 == 1){
            if(tacPlayer == p){
                game.placeSign(x, y, Game.TAC);
                ticPlayer.getConnection().sendMessage(
                        new Message(Protocol.SIGN_PLACED, new byte[]{(byte) x, (byte) y, Protocol.TAC_PLAYER}));
                tacPlayer.getConnection().sendMessage(
                        new Message(Protocol.SIGN_PLACED, new byte[]{(byte) x, (byte) y, Protocol.TAC_PLAYER}));
                turn++;
            }
        }
        if(game.checkWin() != 0){
            finishGame();
        }
    }

    public void disconnect(Player p){
        if(ticPlayer == p){
            ticPlayer = null;
            if(tacPlayer != null){
                tacPlayer.getConnection().sendMessage(new Message(Protocol.OPPONENT_DISCONNECT_FROM_ROOM));
            }
        }
        if(tacPlayer == p){
            tacPlayer = null;
            if(ticPlayer != null){
                ticPlayer.getConnection().sendMessage(new Message(Protocol.OPPONENT_DISCONNECT_FROM_ROOM));
            }
        }
        if(state == GAME){
            interruptGame();
        }
        p.setRoom(null);

    }

    public void interruptGame(){
        state = FINISHED;
        if(ticPlayer != null){
            ticPlayer.getConnection().sendMessage(new Message(Protocol.GAME_STOPPED));
        }
        if(tacPlayer != null){
            tacPlayer.getConnection().sendMessage(new Message(Protocol.GAME_STOPPED));
        }
        closeRoom();
    }

    public void finishGame(){
        state = FINISHED;
        int result = game.checkWin();
        if(result == Game.TIC){
            ticPlayer.getConnection().sendMessage(new Message(Protocol.WIN));
            tacPlayer.getConnection().sendMessage(new Message(Protocol.LOSE));
        }
        if(result == Game.TAC){
            ticPlayer.getConnection().sendMessage(new Message(Protocol.LOSE));
            tacPlayer.getConnection().sendMessage(new Message(Protocol.WIN));
        }
        if(result == 0){
            ticPlayer.getConnection().sendMessage(new Message(Protocol.GAME_STOPPED));
            tacPlayer.getConnection().sendMessage(new Message(Protocol.GAME_STOPPED));
        }
        closeRoom();
    }

    private void closeRoom(){
        if(ticPlayer != null) {
            ticPlayer.setRoom(null);
        }
        if (tacPlayer != null) {
            tacPlayer.setRoom(null);
        }
    }

    public Player getTicPlayer() {
        return ticPlayer;
    }

    public void setTicPlayer(Player ticPlayer) {
        this.ticPlayer = ticPlayer;
    }

    public Player getTacPlayer() {
        return tacPlayer;
    }

    public void setTacPlayer(Player tacPlayer) {
        this.tacPlayer = tacPlayer;
    }

    public Game getGame() {
        return game;
    }

    public int getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
