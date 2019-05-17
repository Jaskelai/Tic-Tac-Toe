package tictactoe.client.data;

import tictactoe.entity.Player;

import java.io.IOException;

public interface OnStartCallback {

    void onOpponentFound(Player player) throws IOException;

    void onOpenedConnection();
}
