package qwq.wumie.module;

import qwq.wumie.Launch;
import qwq.wumie.module.impl.register.User;
import qwq.wumie.module.results.MessageResult;
import qwq.wumie.server.Server;
import qwq.wumie.server.message.GMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Module {
    public String name;
    public List<String> names = new ArrayList<>();
    public boolean enable;
    public User.Prem level = User.Prem.Member;

    public static Server server = Launch.getMainServer();

    public void onInit() {}

    public Module(String name) {
        this.name = name;
        names.add(name);
        this.enable = true;
    }

    public Module(String name,String...  names) {
        this(name);
        this.names.addAll(Arrays.asList(names));
    }

    public Module(String name, boolean enable) {
        this(name);
        this.enable = enable;
    }

    public Module(String name, User.Prem level) {
        this(name);
        this.level = level;
    }

    public Module(String name, boolean enable, User.Prem level) {
        this(name,enable);
        this.level = level;
    }

    public void setLevel(User.Prem level) {
        this.level = level;
    }

    public void setEnable(boolean b) {
        this.enable = b;
    }

    public abstract void run(String[] args, GMessage message, GMessage.Sender sender,String label);

    public static void sendMessage(String message, GMessage gMessage) {
        MessageResult result = new MessageResult(new MessageResult.Params(gMessage.getGroup_id(),message));
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

    public boolean inName(String arg0) {
        for (String name : names) {
            if (name.equalsIgnoreCase(arg0)) {
                return true;
            }
        }
        return false;
    }
}
