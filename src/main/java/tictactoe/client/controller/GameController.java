package tictactoe.client.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import org.springframework.stereotype.Component;
import tictactoe.client.data.OnGameCallback;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class GameController implements OnGameCallback, Initializable {

    @FXML
    private GridPane gridPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addGridEvent();
    }

    public void addGridEvent() {
        gridPane.setOnMouseClicked((event) -> {
            double height = gridPane.getHeight();
            double width = gridPane.getWidth();
            double start = gridPane.getLayoutX();
            double top = gridPane.getLayoutY();
            if (event.getX() > start && event.getX() < start + width) {

            }
////            Integer colIndex = GridPane.getColumnIndex();
////            Integer rowIndex = GridPane.getRowIndex();
//            System.out.println(colIndex);
//            System.out.println(rowIndex);
        });
    }
}
