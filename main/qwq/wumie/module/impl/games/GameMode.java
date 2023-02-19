package qwq.wumie.module.impl.games;

import qwq.wumie.Launch;
import qwq.wumie.module.impl.register.User;
import qwq.wumie.module.results.MessageResult;
import qwq.wumie.server.Server;
import qwq.wumie.server.message.GMessage;

import java.util.ArrayList;
import java.util.List;

public class GameMode {
    public int gameId = 0;
    public String name;
    public static Server server = Launch.getMainServer();
    public final List<User> players = new ArrayList<>();
    public boolean started = false;
    public int maxPlayer;

    public GameMode(String name,int maxPlayer) {
        this.name = name;
        this.maxPlayer = maxPlayer;
    }

    public void onJoin(String qq) {}
    public void onLeft(String qq) {}
    public void start() {}
    public void end() {}

    public User getUser(String qq) {
        for (User u : players) {
            if (u.getUser_id().equalsIgnoreCase(qq)) {
                return u;
            }
        }
        return null;
    }

    public static void sendMessage(String message, GMessage gMessage) {
        MessageResult result = new MessageResult(new MessageResult.Params(gMessage.getGroup_id(),message));
        server.broadcast(result.toJSON());
    }

    public static void info(String group,String message) {
        MessageResult result = new MessageResult(new MessageResult.Params(group,message));
        server.broadcast(result.toJSON());
    }

    public static void info(String message) {
        MessageResult result = new MessageResult(new MessageResult.Params(Launch.getMainServer().currentMessage.getGroup_id(),message));
        server.broadcast(result.toJSON());
    }

    public static void info(String... messages) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message).append("\n");
        }
        if (sb.toString().endsWith("\n")) sb.setLength(sb.length()-"\n".length());

        info(sb.toString());
    }

    public static void info(int group,String... messages) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message).append("\n");
        }
        if (sb.toString().endsWith("\n")) sb.setLength(sb.length()-"\n".length());

        MessageResult result = new MessageResult(new MessageResult.Params(group+"",sb.toString()));
        server.broadcast(result.toJSON());
    }
}
