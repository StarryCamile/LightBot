package qwq.wumie.module.impl;

import qwq.wumie.module.Module;
import qwq.wumie.module.impl.register.Register;
import qwq.wumie.module.impl.register.User;
import qwq.wumie.server.message.GMessage;

public class Money extends Module {
    public Money() {
        super("money");
    }

    @Override
    public void run(String[] args, GMessage message, GMessage.Sender sender, String label) {
        if (args.length == 1) {
            User user = Register.get(message.getUser_id());
            sendMessage("[CQ:at,qq="+user.getUser_id()+"] ��ӵ��"+user.getBal()+"��",message);
            return;
        }
        if (args.length == 2) {
            User user = Register.get(args[1],false);
            if (user.getUser_id() == null) {
                sendMessage(args[1]+"δע��򲻴���",message);
                return;
            }
            sendMessage("[CQ:at,qq="+message.getUser_id()+"] "+user.getUser_id()+"ӵ��"+user.getBal()+"��",message);
            return;
        }
        if (args.length == 4) {
            if (Register.get(message.getUser_id(),false).getPerm().level >= 1) {
                User user = Register.get(args[2],false);
                int money = Integer.parseInt(args[3]);
                if (args[1].equalsIgnoreCase("set")) {
                    user.setBal(money);
                    Register.save();
                    sendMessage("������" + user.getUser_id() + "�ģ�Ϊ" + user.getBal(), message);
                }
                if (args[1].equalsIgnoreCase("add")) {
                    user.setBal(user.getBal() + money);
                    Register.save();
                    sendMessage("��Ϊ" + user.getUser_id() + "�ģ����" + money, message);
                }
            }
        }
    }
}
