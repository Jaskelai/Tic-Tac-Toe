package tictactoe.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tictactoe.entity.Headers;
import tictactoe.entity.MessageType;
import tictactoe.client.data.MyWebSocketClient;
import tictactoe.client.data.OnStartCallback;
import tictactoe.client.data.PlayerRepository;
import tictactoe.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class WaitController extends AbstractController implements OnStartCallback {

    @Autowired
    private MyWebSocketClient myWebSocketClient;

    @Autowired
    private PlayerRepository playerRepository;

    @FXML
    private Label labelZhdi;

    @FXML
    private ImageView waitGif;

    public void initialize() throws InterruptedException, FileNotFoundException {
        myWebSocketClient.connect();
        File file = new File("E:\\dev\\java\\kn\\Tic-Tac-Toe\\src\\main\\resources\\img\\wait3.gif");
        Image image = new Image(new FileInputStream(file));
        waitGif.setImage(image);
    }

    @Override
    public void onOpponentFound(Player player) throws IOException {
        playerRepository.setOpponentPlayer(player);
        Stage stage = (Stage) labelZhdi.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/game.fxml"));
        loader.setControllerFactory(getContext()::getBean);
        Parent gameScene = loader.load();
        Platform.runLater(() -> stage.setScene(new Scene(gameScene)));
    }

    @Override
    public void onOpenedConnection() {
        JSONObject jsonObject = new JSONObject();
        Player player = playerRepository.getPlayer();
        jsonObject.put(Headers.MESSAGE_TYPE.name(), MessageType.INIT_MESSAGE.name());
        jsonObject.put(Headers.USERNAME.name(), player.getName());
        String encodedString = java.util.Base64.getEncoder().encodeToString(player.getImage());
        jsonObject.put(Headers.IMAGE.name(), encodedString);
        myWebSocketClient.sendMessage(jsonObject);
    }
}
