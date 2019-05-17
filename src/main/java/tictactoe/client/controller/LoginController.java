package tictactoe.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tictactoe.client.data.PlayerRepository;

import java.io.IOException;

@Component
public class LoginController extends AbstractController {

    @Autowired
    private PlayerRepository playerRepository;

    @FXML
    private TextField etLogin;

    @FXML
    private void onStartGameClicked() throws IOException {
        playerRepository.setPlayerName(etLogin.getText());
        Stage stage = (Stage) etLogin.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/paint.fxml"));
        loader.setControllerFactory(getContext()::getBean);
        Parent paintScene = loader.load();
        stage.setScene(new Scene(paintScene));
    }
}
