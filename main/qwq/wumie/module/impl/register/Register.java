package qwq.wumie.module.impl.register;

import qwq.wumie.Launch;
import qwq.wumie.module.Module;
import qwq.wumie.module.impl.register.User;
import qwq.wumie.module.results.MessageResult;
import qwq.wumie.server.message.GMessage;
import qwq.wumie.utils.FileUtils;
import qwq.wumie.utils.GsonUtils;
import qwq.wumie.utils.I18nUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Register extends Module {
    public static File folder = new File(Launch.data_FOLDER,"users");
    public static List<User> users = new ArrayList<>();

    public Register() {
        super("register");
        try {
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String[] args, GMessage message, GMessage.Sender sender,String label) {
        User user = new User(message.getUser_id(), User.Prem.Member,100);
        if (inList(user)) {
            sendMessage(I18nUtils.getConversion("registered-user","qq",user.getUser_id()),message);
        } else {
            users.add(user);
            String jsonString = GsonUtils.beanToJson(user);
            FileUtils.save(folder, user.getUser_id()+".json", jsonString);
            sendMessage(I18nUtils.getConversion("register-new-user","qq",user.getUser_id()),message);
        }
    }

    public static void add(User user) {
        GMessage message = Launch.getMainServer().currentMessage;
        if (inList(user)) {
            sendMessage(I18nUtils.getConversion("not-unknown-user","qq",user.getUser_id()),message);
        } else {
            users.add(user);
            String jsonString = GsonUtils.beanToJson(user);
            FileUtils.save(folder, user.getUser_id()+".json", jsonString);
            sendMessage(I18nUtils.getConversion("success-add-user","qq",user.getUser_id()),message);
        }
    }

    public static void remove(String qq) {
        users.removeIf(u -> u.getUser_id().equalsIgnoreCase(qq));
    }

    public static void load() throws IOException {
        if (!folder.exists()) folder.mkdirs();
        users.clear();

        Files.list(Launch.data_FOLDER.toPath().resolve("users")).forEach(path -> {
            if (isValidFile(path)) {
                File f = path.toFile();
                String json = Launch.readText(f).get(0);
                User u = GsonUtils.jsonToBean(json,User.class);
                users.add(u);
            }
        });
    }

    public static void save() {
        for (User user : users) {
            String jsonString = GsonUtils.beanToJson(user);
            FileUtils.save(folder, user.getUser_id() + ".json", jsonString);
        }
        reload();
    }

    public static void reload() {
        try {
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isValidFile(Path file) {
        String extension = file.toFile().getName().endsWith(".json") ? "json" : "操你妈又乱放文件";
        return  (extension.equals("json"));
    }

    public static boolean inList(User user) {
        for (User u : users) {
            if (u.getUser_id().equalsIgnoreCase(user.getUser_id())) {
                return true;
            }
        }
        return false;
    }

    public static boolean inList(String qq) {
        for (User u : users) {
            if (u.getUser_id().equalsIgnoreCase(qq)) {
                return true;
            }
        }
        return false;
    }

    public static User get(String qq,boolean b) {
        if (!inList(qq)) {
            if (b) {
                MessageResult result = new MessageResult(new MessageResult.Params(Launch.getMainServer().currentMessage.getGroup_id(), "你还没有注册"));
                Launch.getMainServer().broadcast(result.toJSON());
            }
            new User(null, User.Prem.Member,0);
        }
        for (User u : users) {
            if (u.getUser_id().equalsIgnoreCase(qq)) {
                return u;
            }
        }
        return new User(null, User.Prem.Member,0);
    }

    public static User get(String qq) {
        return get(qq,true);
    }
}
