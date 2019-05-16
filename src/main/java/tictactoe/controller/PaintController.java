package tictactoe.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;

public class PaintController {

    @FXML
    private Canvas canvas;

    public void initialize() {
        GraphicsContext g = canvas.getGraphicsContext2D();

        canvas.setOnMouseDragged(e -> {
            double size = 2.0;
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;

            g.setFill(Color.BLACK);
            g.fillRect(x, y, size, size);
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
