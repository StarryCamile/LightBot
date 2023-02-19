package qwq.wumie.module.impl;

import qwq.wumie.Launch;
import qwq.wumie.module.Module;
import qwq.wumie.module.impl.register.Register;
import qwq.wumie.module.impl.register.User;
import qwq.wumie.server.message.GMessage;
import qwq.wumie.utils.I18nUtils;

import java.io.File;

public class UserSet extends Module {
    public UserSet() {
        super("user", User.Prem.Owner);
    }

    @Override
    public void run(String[] args, GMessage message, GMessage.Sender sender,String label) {
        String qq = message.getUser_id();
        User user = Register.get(qq);
        if (user.getUser_id() == null) return;
        switch (args[1].toLowerCase()) {
            case "add" -> {
                if (args.length != 5) {
                    sendMessage(Launch.i18n.get("error-use-command"), message);
                    return;
                }
                String id = args[2];
                User.Prem prem = User.Prem.get(args[3]);
                int money = Integer.parseInt(args[4]);
                User u = new User(id,prem,money);
                Register.add(u);
                Register.reload();
            }
            case "del","remove" -> {
                String id = args[2];
                if (Register.inList(id)) {
                    User u = Register.get(id);
                    Register.remove(u.getUser_id());
                    File file = new File(Register.folder,u.getUser_id()+".json");
                    file.delete();
                    sendMessage(I18nUtils.getConversion("remove-user","userId",u.getUser_id()),message);
                } else {
                    sendMessage(Launch.i18n.get("unknown-user"), message);
                    break;
                }
                Register.reload();
            }
            case "clear" -> {
                for (User u : Register.users) {
                    File f = new File(Register.folder,u.getUser_id()+".json");
                    f.delete();
                }
                sendMessage(Launch.i18n.get("clear-user"),message);
                Register.reload();
            }
        }
    }
}
