package ru.itis.client;

import javafx.fxml.FXML;

import java.io.IOException;
import java.nio.ByteBuffer;

public class GameStageController {
    @FXML
    private void placeSign_0_0(){
        try {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putInt(0).putInt(0);
            App.getOut().writeMessage(new Message(Protocol.PLACE_SIGN, buffer.array()));
        } catch (IOException ioException) {
            //ignore
        }
    }

    @FXML
    private void placeSign_0_1(){
        try {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putInt(1).putInt(0);
            App.getOut().writeMessage(new Message(Protocol.PLACE_SIGN, buffer.array()));
        } catch (IOException ioException) {
            //ignore
        }
    }

    @FXML
    private void placeSign_0_2(){
        try {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putInt(2).putInt(0);
            App.getOut().writeMessage(new Message(Protocol.PLACE_SIGN, buffer.array()));
        } catch (IOException ioException) {
            //ignore
        }
    }

    @FXML
    private void placeSign_1_0(){
        try {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putInt(0).putInt(1);
            App.getOut().writeMessage(new Message(Protocol.PLACE_SIGN, buffer.array()));
        } catch (IOException ioException) {
            //ignore
        }
    }

    @FXML
    private void placeSign_1_1(){
        try {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putInt(1).putInt(1);
            App.getOut().writeMessage(new Message(Protocol.PLACE_SIGN, buffer.array()));
        } catch (IOException ioException) {
            //ignore
        }
    }

    @FXML
    private void placeSign_1_2(){
        try {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putInt(2).putInt(1);
            App.getOut().writeMessage(new Message(Protocol.PLACE_SIGN, buffer.array()));
        } catch (IOException ioException) {
            //ignore
        }
    }

    @FXML
    private void placeSign_2_0(){
        try {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putInt(0).putInt(2);
            App.getOut().writeMessage(new Message(Protocol.PLACE_SIGN, buffer.array()));
        } catch (IOException ioException) {
            //ignore
        }
    }

    @FXML
    private void placeSign_2_1(){
        try {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putInt(1).putInt(2);
            App.getOut().writeMessage(new Message(Protocol.PLACE_SIGN, buffer.array()));
        } catch (IOException ioException) {
            //ignore
        }
    }

    @FXML
    private void placeSign_2_2(){
        try {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putInt(2).putInt(2);
            App.getOut().writeMessage(new Message(Protocol.PLACE_SIGN, buffer.array()));
        } catch (IOException ioException) {
            //ignore
        }
    }
}
