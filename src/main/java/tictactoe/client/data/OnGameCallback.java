package tictactoe.client.data;

public interface OnGameCallback {

    void getOpponentTurn(int column, int row, boolean isMyTurn);

    void setResult(String result);
}
