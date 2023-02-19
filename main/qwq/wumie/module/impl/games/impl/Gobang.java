package qwq.wumie.module.impl.games.impl;

import qwq.wumie.Launch;
import qwq.wumie.events.EventHandler;
import qwq.wumie.events.message.MessageEvent;
import qwq.wumie.events.system.WhileEvent;
import qwq.wumie.module.Manager;
import qwq.wumie.module.impl.Game;
import qwq.wumie.module.impl.games.GameMode;
import qwq.wumie.module.impl.register.Register;
import qwq.wumie.module.impl.register.User;
import qwq.wumie.module.results.AtResult;
import qwq.wumie.utils.I18nUtils;
import qwq.wumie.utils.ListUtils;
import qwq.wumie.utils.MSTimer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Gobang extends GameMode {
    private final Color p1C = new Color(0, 0, 0, 255);
    private final Color p2C = new Color(255, 0, 0, 255);
    private String currentQQ;
    private MSTimer timer = new MSTimer();
    private List<Chess> chessArray = List.of(new Chess[]{
            new Chess(null, 0, 0, Chess.Type.None), new Chess(null, 0, 1, Chess.Type.None), new Chess(null, 0, 2, Chess.Type.None),
            new Chess(null, 0, 3, Chess.Type.None), new Chess(null, 0, 4, Chess.Type.None), new Chess(null, 0, 5, Chess.Type.None),
            new Chess(null, 0, 6, Chess.Type.None), new Chess(null, 0, 7, Chess.Type.None), new Chess(null, 0, 8, Chess.Type.None)
    });


    public Gobang() {
        super(I18nUtils.get("game-gobang-name"), 2);
    }

    @Override
    public void start() {
        super.start();
        Launch.EVENT_BUS.subscribe(this);
        info(I18nUtils.get("game-gobang-start-info"));
        started = true;
        timer.reset();
        currentQQ = players.get(0).getUser_id();
    }

    @Override
    public void end() {
        Launch.EVENT_BUS.unsubscribe(this);
        super.end();
    }

    @EventHandler
    private void onWhile(WhileEvent event) {
        if (players.size() != maxPlayer) {
            info(I18nUtils.get("game-gobang-player-exit-info"));
            players.clear();
            Game game = Manager.get(Game.class);
            if (game != null) game.removeGame(gameId);
        }
    }

    @EventHandler
    private void onMessage(MessageEvent e) {
        String message = e.message.getRaw_message();
        String qq = e.message.getUser_id();
        String[] args = message.split(" ");
        if (args[0].equalsIgnoreCase("ÏÂ")) {
            int x = Integer.parseInt(args[1]);
            int y = Integer.parseInt(args[2]);
            Launch.info("Íæ¼Ò" + qq + "ÏÂ" + getByCoord(x, y));
            currentQQ = currentQQ.equalsIgnoreCase(players.get(0).getUser_id()) ? players.get(1).getUser_id() : players.get(0).getUser_id();
            Chess.Type type = qq.equalsIgnoreCase(players.get(0).getUser_id()) ? Chess.Type.P1 : Chess.Type.P2;
            Chess chess = chessArray.get(getByCoord(x, y));
            if (chess.type.equals(Chess.Type.None)) {
                //chessArray = chessArray.remove();
                StringBuilder sb = new StringBuilder();
                int a = 0;
                for (Chess c : chessArray) {
                    String s = c.type.equals(Chess.Type.None) ? " " : "=";
                    if (c.type.equals(Chess.Type.P2)) s = "+";
                    sb.append(s);
                    if (a == 3 || a == 6) {
                        sb.append("\n");
                    }
                    a++;
                }
                info(sb.toString());
                info(I18nUtils.getConversion("game-gobang-next-info","qq",new AtResult(qq).toJSON()));
            } else {
                info(I18nUtils.get("game-gobang-contains-chess-info"));
            }
        }
    }

    public int getByCoord(int x, int y) {
        x = Math.min(x, 3);
        switch (x) {
            case 0 -> {
                return Math.min(y, 2);
            }
            case 1 -> {
                return Math.min(2 + y, 5);
            }
            case 2 -> {
                return Math.min(2 + 3 + y, 8);
            }
        }
        return 0;
    }

    @Override
    public void onJoin(String qq) {
        AtResult result = new AtResult(qq);

        if (started) {
            info(result.toJSON() + I18nUtils.get("game-gobang-started-info"));
            return;
        }

        if (players.size() < maxPlayer) {
            User user = Register.get(qq);
            if (ListUtils.inList(players, user)) {
                info(result.toJSON() + I18nUtils.get("game-gobang-in-game-info"));
            } else {
                players.add(user);
            }
        } else {
            info(result.toJSON() + I18nUtils.get("game-gobang-full-info"));
            return;
        }
        super.onJoin(qq);
    }

    @Override
    public void onLeft(String qq) {
        AtResult result = new AtResult(qq);
        if (players.size() != 0) {
            User user = Register.get(qq);
            if (ListUtils.inList(players, user)) {
                players.remove(getUser(user.getUser_id()));
            } else {
                info(result.toJSON() + I18nUtils.getConversion("game-gobang-left-not-in-game-info","gameId",gameId+""));
            }
        }

        super.onLeft(qq);
    }

public static class Chess {
    public String qq;
    public int x;
    public int y;
    public Type type;

    public Chess(String qq, int x, int y, Type type) {
        this.qq = qq;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public enum Type {
        P1,
        P2,
        None
    }
}
}
