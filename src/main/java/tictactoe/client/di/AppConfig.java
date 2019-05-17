package tictactoe.client.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import tictactoe.client.controller.GameController;
import tictactoe.client.controller.WaitController;
import tictactoe.client.data.MyWebSocketClient;
import tictactoe.client.data.PlayerRepository;

@Configuration
@ComponentScan(basePackages = "tictactoe.client.controller")
public class AppConfig {

    @Bean
    public PlayerRepository providePlayerRepository() {
        return new PlayerRepository();
    }

    @Bean
    public MyWebSocketClient provideWebSocketClient(WaitController waitController, GameController gameController) {
        return new MyWebSocketClient(waitController, gameController);
    }

}
