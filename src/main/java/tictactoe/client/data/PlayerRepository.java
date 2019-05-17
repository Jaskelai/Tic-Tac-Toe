package tictactoe.client.data;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.springframework.stereotype.Component;
import tictactoe.entity.Player;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class PlayerRepository {

    private Player player = new Player();
    private Player opponentPlayer;
    private boolean result = false;

    public void setPlayerName(String name) {
        player.setName(name);
    }

    public void setPlayerImage(Image image) {
        player.setImage(getByteArray(image));
    }

    public Player getPlayer() {
        return player;
    }

    public Player getOpponentPlayer() {
        return opponentPlayer;
    }

    public void setOpponentPlayer(Player opponentPlayer) {
        this.opponentPlayer = opponentPlayer;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean getResult() {
        return result;
    }

    private byte[] getByteArray(Image image) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] result = null;
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", byteStream);
            result = byteStream.toByteArray();
            byteStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
