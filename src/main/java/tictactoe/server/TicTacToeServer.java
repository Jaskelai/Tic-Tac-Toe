package tictactoe.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;
import tictactoe.entity.Headers;
import tictactoe.entity.MessageType;
import tictactoe.entity.Player;

import javax.websocket.server.ServerEndpoint;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@ServerEndpoint(value = "/game")
public class TicTacToeServer extends WebSocketServer {

    private static final int PORT = 1337;
    private static int currentPlayers = 0;
    private static Player player1;
    private static Player player2;
    private Map<Player, WebSocket> conns;

    public TicTacToeServer() {
        super(new InetSocketAddress(PORT));
        conns = new HashMap<>();
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
            conns.put(player1,webSocket);
            currentPlayers++;
        } else if (currentPlayers == 1) {
            player2 = new Player(username, image);
            conns.put(player2,webSocket);
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

}
