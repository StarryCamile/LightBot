package qwq.wumie.module.impl;

import qwq.wumie.Launch;
import qwq.wumie.module.Module;
import qwq.wumie.module.impl.register.User;
import qwq.wumie.server.message.GMessage;
import qwq.wumie.utils.FileUtils;

import java.io.File;

public class Test extends Module {
    private String json = "{}";

    public Test() {
        super("test", User.Prem.Admin);
        setEnable(Launch.config.isEnableTest());
    }

    @Override
    public void run(String[] args, GMessage message, GMessage.Sender sender,String label) {
        switch (args[1].toLowerCase()) {
            case "set" -> {
                this.json = message.getRaw_message().substring((args[0]+" "+args[1]+" ").length());
                sendMessage("ÉèÖÃÎª: \n"+json,message);
            }
            case "send" -> {
                File f = new File(Launch.FOLDER,"test.txt");
                if (!f.exists()) FileUtils.save(Launch.FOLDER,"test.txt","");


                sendMessage(Launch.readText(f).get(0),message);
            }
        }
    }
}
