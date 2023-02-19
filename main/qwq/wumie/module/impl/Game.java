package qwq.wumie.module.impl;

import qwq.wumie.Launch;
import qwq.wumie.events.EventHandler;
import qwq.wumie.events.system.StopBotEvent;
import qwq.wumie.module.Module;
import qwq.wumie.module.impl.games.GameInfo;
import qwq.wumie.module.impl.games.GameMode;
import qwq.wumie.module.impl.games.impl.Gobang;
import qwq.wumie.module.impl.games.impl.Pyramid;
import qwq.wumie.module.impl.games.impl.TicTacToe;
import qwq.wumie.module.impl.register.Register;
import qwq.wumie.module.impl.register.User;
import qwq.wumie.module.results.AtResult;
import qwq.wumie.server.message.GMessage;
import qwq.wumie.utils.I18nUtils;
import qwq.wumie.utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class Game extends Module {
    public static List<GameMode> games = new ArrayList<>();
    public static List<GameInfo> infos = new ArrayList<>();

    public static GameMode currentGame;

    public Game() {
        super("game","游戏");
        games.add(new Pyramid());
        games.add(new Gobang());
        games.add(new TicTacToe());
    }

    public GameMode getGame(String name) {
        for (GameMode g : games) {
            if (g.name.equalsIgnoreCase(name)) {
                return g;
            }
        }
        info("该游戏不存在");
        return null;
    }

    public GameInfo getGameInfo(int id) {
        for (GameInfo g : infos) {
            if (g.gameId == id) {
                return g;
            }
        }
        return null;
    }

    @Override
    public void onInit() {
        Launch.EVENT_BUS.subscribe(this);
    }

    @EventHandler
    private void onStop(StopBotEvent e) {
        Launch.EVENT_BUS.unsubscribe(this);
    }

    @Override
    public void run(String[] args, GMessage message, GMessage.Sender sender, String label) {
        String qq = message.getUser_id();
        if (args.length == 4) {
            qq = args[3];
        }

        User user = Register.get(qq);
        if (user.getUser_id() == null) return;

        AtResult result = new AtResult(user.getUser_id());
        if (args[1].equalsIgnoreCase("create") && args.length == 3) {
            GameMode game = getGame(args[2]);
            if (game != null) {
                for (GameMode g : games) {
                    try {
                        if (game.name.equalsIgnoreCase(g.name)) {
                            game = (GameMode) Class.forName(g.getClass().getName()).newInstance();
                            Launch.info(g.getClass().getName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (game.name.equalsIgnoreCase("金字塔")) game = new Pyramid();

                int gameId = RandomUtils.nextInt(game.maxPlayer+(game.started ? RandomUtils.nextInt(114500,888888) : RandomUtils.nextInt(5500,11451)),114514191);
                game.gameId = gameId;
                infos.add(new GameInfo(gameId,game));
                info(I18nUtils.getConversion("create-info","gameId",game.gameId+""));
            }
            return;
        }

        GameInfo gameInfo = getGameInfo(Integer.parseInt(args[2]));
        if (gameInfo == null) {
            info(I18nUtils.get("unknown-gameInfo"));
        } else {
            GameMode game = gameInfo.target;
            switch (args[1]) {
                case "join" -> {
                    if (game.players.size() >= game.maxPlayer) break;

                    if (game.getUser(user.getUser_id()) == null) {
                        info(result.toJSON() + I18nUtils.getConversion("join-game",new String[]{"name","players"},new String[]{game.name,(game.players.size()+1)+""}));
                    }
                    game.onJoin(user.getUser_id());
                    if (game.players.size() >= game.maxPlayer) {
                        addGame(game.gameId);
                        break;
                    }
                }
                case "exit" -> {
                    if (game.getUser(user.getUser_id()) != null) {
                        info(result.toJSON() + I18nUtils.getConversion("exit-game",new String[]{"name","players"},new String[]{game.name,(game.players.size()-1)+""}));
                    }
                    game.onLeft(user.getUser_id());
                    if (game.players.size() <= 0) {
                        removeGame(game.gameId);
                        break;
                    }
                }
            }
        }
    }

    public void removeGame(int id) {
        GameMode game = getGameInfo(id).target;
        game.end();
        Launch.EVENT_BUS.unsubscribe(game);
        infos.remove(getGameInfo(game.gameId));
        info(I18nUtils.getConversion("game-end","gameId",game.gameId+""));
    }

    public void addGame(int id) {
        GameMode game = getGameInfo(id).target;
        Launch.EVENT_BUS.subscribe(game);
        game.start();
        currentGame = game;
        info(I18nUtils.getConversion("game-start","gameId",game.gameId+""));
    }
}
