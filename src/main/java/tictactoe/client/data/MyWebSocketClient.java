package tictactoe.client.data;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import tictactoe.entity.Headers;
import tictactoe.entity.MessageType;
import tictactoe.entity.Player;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MyWebSocketClient extends WebSocketClient {

    private static final String SERVER_PATH = "ws://127.0.0.1:1337/game";
    private static URI serverUri;

    private OnStartCallback onStartCallback;
    private OnGameCallback onGameCallback;

    static {
        try {
            serverUri = new URI(SERVER_PATH);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public MyWebSocketClient(OnStartCallback onStartCallback, OnGameCallback onGameCallback) {
        super(serverUri);
        this.onStartCallback = onStartCallback;
        this.onGameCallback = onGameCallback;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        onStartCallback.onOpenedConnection();
    }

    @Override
    public void onMessage(String message) {
        JSONObject object = new JSONObject(message);
        String type = (String) object.get(Headers.MESSAGE_TYPE.name());
        if (MessageType.valueOf(type).equals(MessageType.INIT_MESSAGE)) {
            String username = (String) object.get(Headers.USERNAME.name());
            String encodedImage = (String) object.get(Headers.IMAGE.name());
            byte[] image = java.util.Base64.getDecoder().decode(encodedImage);
            Player opponent = new Player(username, image);
            try {
                onStartCallback.onOpponentFound(opponent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (MessageType.valueOf(type).equals(MessageType.TURN)) {
            int column = (int) object.get(Headers.COLUMN.name());
            int row = (int) object.get(Headers.ROW.name());
            Object isMy = object.get(Headers.ISMY.name());
            boolean isMyTurn;
            isMyTurn = isMy.equals("true");
            onGameCallback.getOpponentTurn(column, row, isMyTurn);
            Object result = object.get(Headers.RESULT.name());
            if (result != null) {
                onGameCallback.setResult((String) result);
            }
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("CONNECTION CLOSED");
    }

    @Override
    public void onError(Exception e) {

    }

    public void sendMessage(JSONObject object) {
        this.send(object.toString());
    }
}
