package tictactoe.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.io.File;

public class PaintController {

    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    public void initialize() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        graphicsContext.setFill(colorPicker.getValue());

        canvas.setOnMousePressed(event ->{
            graphicsContext.beginPath();
            graphicsContext.moveTo(event.getX(), event.getY());
            graphicsContext.stroke();
        });

        canvas.setOnMouseDragged(event -> {
            graphicsContext.lineTo(event.getX(), event.getY());
            graphicsContext.stroke();
            graphicsContext.closePath();
            graphicsContext.beginPath();
            graphicsContext.moveTo(event.getX(), event.getY());

        });

        canvas.setOnMouseReleased(event -> {
            graphicsContext.lineTo(event.getX(), event.getY());
            graphicsContext.stroke();
            graphicsContext.closePath();
        });
    }

    public void onSave() {
        try {
            Image snapshot = canvas.snapshot(null, null);
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("figure.png"));
//            я не знаю, где сохранять и всё такое
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e);
        }
    }
}
