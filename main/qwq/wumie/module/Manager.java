package qwq.wumie.module;

import qwq.wumie.Launch;
import qwq.wumie.module.impl.*;
import qwq.wumie.module.impl.register.Register;
import qwq.wumie.module.impl.register.User;
import qwq.wumie.module.results.MessageResult;
import qwq.wumie.server.message.GMessage;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    public static final List<Module> modules = new ArrayList<>();

    public void init(Launch main) {
        Launch.EVENT_BUS.subscribe(this);
        add(new Say());
        add(new JRRP());
        add(new Test());
        add(new Game());
        add(new AtMe());
        add(new Signin());
        add(new UserSet());
        add(new Dice());
        add(new Register());
        add(new Money());
        add(new Prem());
        add(new Reload());

        initModules();
    }

    public static <T extends Module> T get(String name) {
        for (Module m : modules) {
            if (m.inName(name)) {
                return (T) m;
            }
        }
        return null;
    }

    public static <T extends Module> T get(Class klass) {
        for (Module m : modules) {
            if (m.getClass().equals(klass)) {
                return (T) m;
            }
        }
        return null;
    }

    public void add(Module module) {
        modules.add(module);
    }

    private static void initModules() {
        for (Module m : modules) {
            m.onInit();;
        }
    }

    public void in(GMessage message, GMessage.Sender sender) {
        for (Module module : modules) {
            String text = message.getRaw_message();
            String[] args = text.split(" ");

            if (Launch.config.isEnableKey()) {
                if (text.startsWith(Launch.ownerKey)) {
                    String[] a1 = text.substring((Launch.ownerKey + " ").length()).split(" ");
                    if (module.inName(a1[0])) {
                        module.run(a1, message, sender, text.substring((Launch.ownerKey + " ").length()));
                        Launch.updateKey();
                        continue;
                    }
                }
            }

            if (module.inName(args[0])) {
                if (module.name.equalsIgnoreCase("register")) {
                    module.run(args, message, sender,message.getRaw_message());
                    Launch.updateKey();
                    continue;
                }
                if (module.enable) {
                    User user = Register.get(message.getUser_id());
                    if (user.getUser_id() == null) return;
                    if (user.getPerm().level >= module.level.level) {
                        module.run(args, message, sender,message.getRaw_message());
                        Launch.updateKey();
                    } else {
                        MessageResult result = new MessageResult(new MessageResult.Params(message.getGroup_id(), "È¨ÏÞ²»×ã"));
                        Launch.getMainServer().broadcast(result.toJSON());
                    }
                }
            }
        }
    }
}
