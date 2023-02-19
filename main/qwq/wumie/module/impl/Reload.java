package qwq.wumie.module.impl;

import qwq.wumie.Launch;
import qwq.wumie.module.Module;
import qwq.wumie.module.impl.register.User;
import qwq.wumie.server.message.GMessage;

import java.io.File;

public class Reload extends Module {
    public Reload() {
        super("reload", User.Prem.Owner);
    }

    @Override
    public void run(String[] args, GMessage message, GMessage.Sender sender,String label) {
        switch (args[1]) {
            case "config" -> {
                Launch.reload();
                sendMessage("已重新加载配置.",message);
            }
        }
    }
}
