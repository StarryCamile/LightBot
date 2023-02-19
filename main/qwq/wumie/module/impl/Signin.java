package qwq.wumie.module.impl;

import qwq.wumie.module.Module;
import qwq.wumie.module.impl.register.Register;
import qwq.wumie.module.impl.register.User;
import qwq.wumie.module.results.AtResult;
import qwq.wumie.server.message.GMessage;
import qwq.wumie.utils.ListUtils;
import qwq.wumie.utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class Signin extends Module {
    private static final List<String> ids = new ArrayList<>();

    public Signin() {
        super("signin","签到");
    }

    @Override
    public void run(String[] args, GMessage message, GMessage.Sender sender, String label) {
        String qq = message.getUser_id();
        AtResult result = new AtResult(qq);

        if (args.length == 2 && args[1].equalsIgnoreCase("reset")) {
            User user = Register.get(qq);
            if (user.getPerm().level == 0) {
                info("权限不足");
            } else {
                ids.clear();
                info("已重置今日签到");
            }
            return;
        }

        if (ListUtils.inList(ids, qq)) {
            info(result.toJSON()+"你已经签到过了");
        } else {
            ids.add(qq);
            int money = RandomUtils.nextInt(20,70);
            User user = Register.get(qq);
            user.setBal(user.getBal()+money);
            info(result.toJSON()+"签到成功","你获得了"+money+"￥");
        }
    }
}
