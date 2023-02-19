package qwq.wumie.module.impl;

import qwq.wumie.Launch;
import qwq.wumie.module.Module;
import qwq.wumie.server.message.GMessage;
import qwq.wumie.utils.RandomUtils;

public class Dice extends Module {
    public Dice() {
        super("В╩вс");
        setEnable(Launch.config.isEnableDice());
    }

    @Override
    public void run(String[] args, GMessage message, GMessage.Sender sender,String label) {
        int number = RandomUtils.nextInt(1,6);
        sendMessage(getInfo(number),message);
    }

    private String getInfo(int number) {
        String txt = Launch.i18n.get("dice-info");
        while (txt.contains("%number")) {
            txt = txt.replace("%number",number+"");
        }
        return txt;
    }
}
