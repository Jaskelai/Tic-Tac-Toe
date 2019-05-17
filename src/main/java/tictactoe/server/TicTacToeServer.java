package tictactoe.server;

import javafx.util.Pair;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;
import tictactoe.entity.Headers;
import tictactoe.entity.MessageType;
import tictactoe.entity.Player;

import javax.websocket.server.ServerEndpoint;
import java.net.InetSocketAddress;
import java.util.*;

@ServerEndpoint(value = "/game")
public class TicTacToeServer extends WebSocketServer {

    private static final int PORT = 1337;
    private static int currentPlayers = 0;
    private static Player player1;
    private static Player player2;
    private Map<Player, WebSocket> conns;
    private List<Pair<Integer, Integer>> pairs;

    private boolean isGameOver = false;

    public TicTacToeServer() {
        super(new InetSocketAddress(PORT));
        conns = new HashMap<>();
        pairs = new ArrayList<>();
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("OPEN");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        conns.remove(webSocket);
        System.out.println("CLOSED");
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        JSONObject object = new JSONObject(message);
        String type = (String) object.get(Headers.MESSAGE_TYPE.name());
        if (MessageType.valueOf(type).equals(MessageType.INIT_MESSAGE)) {
            handleInit(object, webSocket);
        } else {
            if (MessageType.valueOf(type).equals(MessageType.TURN)) {
                handleTurn(object, webSocket);
            }
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {
        System.out.println("ONSTART");
    }

    private void handleInit(JSONObject object, WebSocket webSocket) {
        String username = (String) object.get(Headers.USERNAME.name());
        String encodedImage = (String) object.get(Headers.IMAGE.name());
        byte[] image = java.util.Base64.getDecoder().decode(encodedImage);
        if (currentPlayers == 0) {
            player1 = new Player(username, image);
            conns.put(player1, webSocket);
            currentPlayers++;
        } else if (currentPlayers == 1) {
            player2 = new Player(username, image);
            conns.put(player2, webSocket);
            currentPlayers++;
        }
        if (currentPlayers == 2) {
            JSONObject response = new JSONObject();
            response.put(Headers.MESSAGE_TYPE.name(), MessageType.INIT_MESSAGE.name());
            response.put(Headers.USERNAME.name(), player2.getName());
            String encodedString = java.util.Base64.getEncoder().encodeToString(player2.getImage());
            response.put(Headers.IMAGE.name(), encodedString);
            conns.get(player1).send(response.toString());
            response.put(Headers.USERNAME.name(), player1.getName());
            encodedString = java.util.Base64.getEncoder().encodeToString(player1.getImage());
            response.put(Headers.IMAGE.name(), encodedString);
            conns.get(player2).send(response.toString());
        }
    }

    private void handleTurn(JSONObject jsonObject, WebSocket webSocket) {
        int column = (int) jsonObject.get(Headers.COLUMN.name());
        int row = (int) jsonObject.get(Headers.ROW.name());
        JSONObject response = new JSONObject();
        response.put(Headers.MESSAGE_TYPE.name(), MessageType.TURN.name());
        response.put(Headers.ROW.name(), row);
        response.put(Headers.COLUMN.name(), column);
        processGame(column, row);
        if (!isGameOver) {
            System.out.println("NE ZACONCHENA");
            conns.get(player1).send(response.toString());
            conns.get(player2).send(response.toString());
        } else {
            response.put(Headers.RESULT.name(), true);
            conns.get(player1).send(response.toString());
            response.put(Headers.RESULT.name(), false);
            conns.get(player2).send(response.toString());
        }
    }

    private void processGame(int column, int row) {
        pairs.add(new Pair<>(column, row));
        if (pairs.contains(new Pair<>(1, 1)) && pairs.contains(new Pair<>(2, 2)) && pairs.contains(new Pair<>(3, 3))) {
            isGameOver = true;
            return;
        }
        if (pairs.contains(new Pair<>(1, 1)) && pairs.contains(new Pair<>(2, 1)) && pairs.contains(new Pair<>(3, 1))) {
            isGameOver = true;
            return;
        }
        if (pairs.contains(new Pair<>(1, 1)) && pairs.contains(new Pair<>(1, 2)) && pairs.contains(new Pair<>(1, 3))) {
            isGameOver = true;
            return;
        }
        if (pairs.contains(new Pair<>(2, 1)) && pairs.contains(new Pair<>(2, 2)) && pairs.contains(new Pair<>(2, 3))) {
            isGameOver = true;
            return;
        }
        if (pairs.contains(new Pair<>(1, 2)) && pairs.contains(new Pair<>(2, 2)) && pairs.contains(new Pair<>(3, 2))) {
            isGameOver = true;
            return;
        }
        if (pairs.contains(new Pair<>(3, 1)) && pairs.contains(new Pair<>(3, 2)) && pairs.contains(new Pair<>(3, 3))) {
            isGameOver = true;
            return;
        }
        if (pairs.contains(new Pair<>(1, 3)) && pairs.contains(new Pair<>(2, 3)) && pairs.contains(new Pair<>(3, 3))) {
            isGameOver = true;
        }
    }
}
