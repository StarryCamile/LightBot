package qwq.wumie.module.impl.games;

public class GameInfo {
    public int gameId;
    public GameMode target;

    public GameInfo(int gameId, GameMode target) {
        this.gameId = gameId;
        this.target = target;
    }
}
