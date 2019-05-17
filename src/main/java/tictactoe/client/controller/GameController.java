package tictactoe.client.controller;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tictactoe.client.data.MyWebSocketClient;
import tictactoe.client.data.OnGameCallback;
import tictactoe.client.data.PlayerRepository;
import tictactoe.entity.Headers;
import tictactoe.entity.MessageType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class GameController extends AbstractController implements OnGameCallback, Initializable {

    private boolean isMyTurn = true;

    @FXML
    private GridPane gridPane;

    @FXML
    private GridPane gridPaneGame;

    @Autowired
    @Lazy
    private PlayerRepository playerRepository;

    @Autowired
    @Lazy
    private MyWebSocketClient myWebSocketClient;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addGridEvent();
    }

    public void addGridEvent() {
        if (isMyTurn) {
            gridPane.setOnMouseClicked((event) -> {
                double clickedX = event.getX();
                double clickedY = event.getY();
                double height = gridPane.getHeight();
                double width = gridPane.getWidth();
                double start = gridPane.getLayoutX();
                double top = gridPane.getLayoutY();
                int row = 0;
                int column = 0;
                if (clickedX > start && clickedX < start + width) {
                    if (clickedX < start + width * 0.45) {
                        column = 1;
                    } else {
                        if (clickedX < start + width * 0.65) {
                            column = 2;
                        } else {
                            if (clickedX < start + width) {
                                column = 3;
                            }
                        }
                    }
                    if (clickedY > top && clickedY < top + height) {
                        if (clickedY < top + height * 0.5) {
                            row = 1;
                        } else {
                            if (clickedY < top + height * 0.7) {
                                row = 2;
                            } else {
                                if (clickedY < top + height) {
                                    row = 3;
                                }
                            }
                        }
                    }
                }
                byte[] a = playerRepository.getPlayer().getImage();
                Image image = new Image(new ByteArrayInputStream(a));
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(60);
                imageView.setFitWidth(60);
                StackPane stackPane = new StackPane();
                stackPane.setPrefSize(60.0, 60.0);
                stackPane.getChildren().add(imageView);
                gridPaneGame.add(stackPane, column-1, row-1);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Headers.MESSAGE_TYPE.name(), MessageType.TURN.name());
                jsonObject.put(Headers.ROW.name(),row);
                jsonObject.put(Headers.COLUMN.name(), column);
                myWebSocketClient.sendMessage(jsonObject);
            });
            isMyTurn = false;
        }
    }

    @Override
    public void getOpponentTurn(int column, int row) {
        Platform.runLater(() -> {
            byte[] a = playerRepository.getOpponentPlayer().getImage();
            Image image = new Image(new ByteArrayInputStream(a));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            StackPane stackPane = new StackPane();
            stackPane.setPrefSize(60.0, 60.0);
            stackPane.getChildren().add(imageView);
            gridPaneGame.add(stackPane, column-1, row-1);
        });
    }

    @Override
    public void setResult(String result) {
        Platform.runLater(() -> {
            if (result.equals("true")) {
                playerRepository.setResult(true);
            } else {
                playerRepository.setResult(false);
            }
            Stage stage = (Stage) gridPane.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/end.fxml"));
            loader.setControllerFactory(getContext()::getBean);
            Parent endScene = null;
            try {
                endScene = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(new Scene(endScene));
        });
    }
}
