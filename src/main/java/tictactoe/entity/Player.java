package tictactoe.entity;

import java.io.Serializable;

public class Player implements Serializable {

    private String name;

    private byte[] image;

    public Player() {
    }

    public Player(String name, byte[] image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
