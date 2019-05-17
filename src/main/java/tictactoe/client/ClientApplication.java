package tictactoe.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tictactoe.client.di.AppConfig;

public class ClientApplication extends Application {
    private final int SIZE = 400;
    private final String TITLE = "TIC-TAC-TOE";
    private static ApplicationContext context;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(new Scene(root, SIZE, SIZE));
        primaryStage.show();
    }
}
