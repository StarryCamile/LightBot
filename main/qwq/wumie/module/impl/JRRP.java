package qwq.wumie.module.impl;

import qwq.wumie.Launch;
import qwq.wumie.module.Module;
import qwq.wumie.module.impl.register.Register;
import qwq.wumie.server.message.GMessage;
import qwq.wumie.utils.FileUtils;
import qwq.wumie.utils.GsonUtils;
import qwq.wumie.utils.RandomUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JRRP extends Module {
    private final List<Info> infoList = new ArrayList<>();
    private final List<String> whiteList = new ArrayList<>();

    public JRRP() {
        super("jrrp");
        setEnable(Launch.config.isEnableJRRP());
        whiteList.add("2816302617");
        if (!new File(new File(Launch.data_FOLDER,"jrrp"),"whitelist.json").exists()) {
            new File(Launch.data_FOLDER,"jrrp").mkdirs();
            WhiteList w = new WhiteList(whiteList);
            String json= GsonUtils.beanToJson(w);
            FileUtils.save(new File(Launch.data_FOLDER,"jrrp"),"whitelist.json",json);
        }
        WhiteList w = GsonUtils.jsonToBean(Launch.readText(new File(new File(Launch.data_FOLDER,"jrrp"),"whitelist.json")).get(0),WhiteList.class);
        for (String s : w.users) {
            if (!whiteList.contains(s)) whiteList.add(s);
        }
    }

    @Override
    public void run(String[] args, GMessage message, GMessage.Sender sender,String label) {
        if (message.getRaw_message().startsWith("jrrp wl")) {
            if (Register.get(message.getUser_id(),false).getPerm().level >= 1) {
                String qq = args[2];
                if (inWhiteList(qq)) {
                    sendMessage(qq+"已在白名单内",message);
                } else {
                    whiteList.add(qq);
                    WhiteList w = new WhiteList(whiteList);
                    String json= GsonUtils.beanToJson(w);
                    FileUtils.save(new File(Launch.data_FOLDER,"jrrp"),"whitelist.json",json);
                    sendMessage("已添加"+qq+"到白名单内",message);
                }
            } else {
                sendMessage("权限不足",message);
            }
            return;
        }

        if (message.getRaw_message().equalsIgnoreCase("jrrp reset")) {
            if (Register.get(message.getUser_id(),false).getPerm().level >= 1) {
                infoList.clear();
                sendMessage("已重置今日人品", message);
            } else {
                 sendMessage("权限不足",message);
            }
            return;
        }

        if (inList(sender.getUser_id(),message.getGroup_id())) {
            sendMessage(
                    "[CQ:at,qq=" + sender.getUser_id() + "]\n" +
                            "您今天已经获取过人品值啦\n" +
                            "您的人品值是: " + get(sender.getUser_id(),message.getGroup_id()).luck
                    , message);
        } else {
            int luck = RandomUtils.nextInt(0, 100);
            if (inWhiteList(sender.getUser_id())) {
                luck = luck+RandomUtils.nextInt(100,Integer.MAX_VALUE);
            }
            sendMessage(
                    "[CQ:at,qq=" + sender.getUser_id() + "]\n" +
                            "您今日的人品值是\n" +
                            luck + "！"
                    , message);
            infoList.add(new Info(sender.getUser_id(),message.getGroup_id(), luck));
        }
    }

    private boolean inList(String qq,String group) {
        for (Info info : infoList) {
            if (info.qq.equalsIgnoreCase(qq) && info.group.equalsIgnoreCase(group)) {
                return true;
            }
        }
        return false;
    }

    private boolean inWhiteList(String qq) {
        for (String s : whiteList) {
            if (s.equals(qq)) {
                return true;
            }
        }
        return false;
    }

    private Info get(String qq,String group) {
        for (Info info : infoList) {
            if (info.qq.equalsIgnoreCase(qq) && info.group.equalsIgnoreCase(group)) {
                return info;
            }
        }
        return null;
    }

    record Info(String qq,String group,int luck) {
    }

    public static class WhiteList {
        List<String> users;

        public WhiteList(List<String> users) {
            this.users = users;
        }

        public void addUser(String u) {
            if (!users.contains(u)) users.add(u);
        }
    }
}
