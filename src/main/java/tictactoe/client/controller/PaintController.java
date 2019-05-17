package tictactoe.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tictactoe.client.data.PlayerRepository;

import java.io.IOException;

@Component
public class PaintController extends AbstractController{
    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    @Autowired
    private PlayerRepository playerRepository;

    public void initialize() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        canvas.setOnMousePressed(event -> {
            g.beginPath();
            g.moveTo(event.getX(), event.getY());
            g.stroke();
        });

        canvas.setOnMouseDragged(event -> {
            g.setStroke(colorPicker.getValue());
            g.setLineWidth(4);
            g.lineTo(event.getX(), event.getY());
            g.stroke();
            g.closePath();
            g.beginPath();
            g.moveTo(event.getX(), event.getY());
        });

        canvas.setOnMouseReleased(event -> {
            g.lineTo(event.getX(), event.getY());
            g.stroke();
            g.closePath();
        });
    }

    public void onBtnStartClicked() throws IOException {
        Image snapshot = canvas.snapshot(null, null);
        playerRepository.setPlayerImage(snapshot);
        Stage stage = (Stage) canvas.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/wait.fxml"));
        loader.setControllerFactory(getContext()::getBean);
        Parent waitScene = loader.load();
        stage.setScene(new Scene(waitScene));
    }
}
