package qwq.wumie.module.impl;

import qwq.wumie.module.Module;
import qwq.wumie.module.impl.register.Register;
import qwq.wumie.module.impl.register.User;
import qwq.wumie.server.message.GMessage;
import qwq.wumie.utils.FileUtils;
import qwq.wumie.utils.GsonUtils;

public class Prem extends Module {
    public Prem() {
        super("prem",User.Prem.Owner);
    }

    @Override
    public void run(String[] args, GMessage message, GMessage.Sender sender,String label) {
        String qq = args[2];
        String prem = args[3];
        User user = Register.get(qq);
        switch (args[1].toLowerCase()) {
            case "set" -> {
                User.Prem pm = User.Prem.get(prem);
                if (pm == null) {
                    sendMessage("该权限不存在",message);
                    return;
                }
                user.setPerm(pm);
                String jsonString = GsonUtils.beanToJson(user);
                FileUtils.save(Register.folder, user.getUser_id()+".json", jsonString);
                sendMessage("已设置"+user.getUser_id()+"的权限为"+user.getPerm(),message);
                Register.reload();
            }
            case "reset" -> {
                user.setPerm(User.Prem.Member);
                String jsonString = GsonUtils.beanToJson(user);
                FileUtils.save(Register.folder, user.getUser_id()+".json", jsonString);
                sendMessage("已重置"+user.getUser_id()+"的权限为"+user.getPerm(),message);
                Register.reload();
            }
        }
    }
}
