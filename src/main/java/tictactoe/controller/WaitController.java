package tictactoe.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class WaitController {

    @FXML
    private ImageView wait;

    public void initialize() throws FileNotFoundException {
//        миша, сделай нормальный путь
        Image image = new Image(new FileInputStream("E:\\dev\\java\\kn\\Tic-Tac-Toe\\src\\main\\resources\\img\\wait3.gif"));
        wait.setImage(image);

    }

}
