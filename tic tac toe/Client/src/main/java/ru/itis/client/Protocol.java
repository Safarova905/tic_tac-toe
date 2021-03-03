package ru.itis.client;

public class Protocol {
    public static final int MAX_ACTION_LENGTH = 1024;

    public static final int PORT = 2021;

    public static final byte TIC_PLAYER = 1;
    public static final byte TAC_PLAYER = 2;

    public static final byte SEND_ERROR = 0;
    public static final byte JOIN_TO_ROOM = 1;
    public static final byte JOINED = 2;
    public static final byte CREATE_ROOM = 3;
    public static final byte START_GAME = 4;
    public static final byte PLACE_SIGN = 5;
    public static final byte SIGN_PLACED = 6;
    public static final byte WIN = 7;
    public static final byte OPPONENT_DISCONNECT_FROM_ROOM = 8;
    public static final byte YOUR_TURN = 9;
    public static final byte LOSE = 10;
    public static final byte GAME_STOPPED = 11;
    public static final byte EXIT_FROM_ROOM = 12;
    public static final byte ADD_ROOM = 13;
    public static final byte REMOVE_ROOM = 14;
    public static final byte GET_ROOMS = 15;
}
