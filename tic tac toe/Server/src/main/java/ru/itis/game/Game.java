package ru.itis.game;

public class Game {
    public static final int TIC = -1;
    public static final int TAC = 1;

    private final int[][] map;

    public Game() {
        map = new int[3][3];
    }

    public Game(int[][] map) {
        this.map = map;
    }

    public int checkWin(){
        int sum = 0;
        for(int i = 0; i < 3; i++){
            sum = 0;
            for(int j = 0; j < 3; j++){
                sum += map[i][j];
            }
            if(sum == 3){
                return 1;
            }
            if(sum == -3){
                return -1;
            }
        }
        for(int i = 0; i < 3; i++){
            sum = 0;
            for(int j = 0; j < 3; j++){
                sum += map[j][i];
            }
            if(sum == 3){
                return 1;
            }
            if(sum == -3){
                return -1;
            }
        }
        sum = 0;
        for(int i = 0; i < 3; i++){
            sum += map[i][i];
        }
        if(sum == 3){
            return 1;
        }
        if(sum == -3){
            return -1;
        }
        sum = 0;
        for(int i = 0; i < 3; i++){
            sum += map[2 - i][i];
        }
        if(sum == 3){
            return 1;
        }
        if(sum == -3){
            return -1;
        }
        return 0;
    }

    public void placeSign(int x, int y, int sign){
        map[x][y] = sign;
    }

    public int getSign(int x, int y){
        return map[x][y];
    }
}
