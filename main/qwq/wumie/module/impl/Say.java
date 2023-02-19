package qwq.wumie.module.impl;

import qwq.wumie.Launch;
import qwq.wumie.module.Module;
import qwq.wumie.module.impl.register.User;
import qwq.wumie.server.message.GMessage;

public class Say extends Module {
    public Say() {
        super("say", User.Prem.Admin);
        setEnable(Launch.config.isEnableSay());
    }

    @Override
    public void run(String[] args, GMessage message, GMessage.Sender sender,String label) {
        sendMessage(label.substring((args[0]+" ").length()),message);
    }
}
