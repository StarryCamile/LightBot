package qwq.wumie.module.impl;

import qwq.wumie.module.Module;
import qwq.wumie.module.results.AtResult;
import qwq.wumie.server.message.GMessage;
import qwq.wumie.utils.GsonUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AtMe extends Module {
    public AtMe() {
        super("[CQ:at,qq=975786409]");
    }

    @Override
    public void run(String[] args, GMessage message, GMessage.Sender sender, String label) {
        if (message.getUser_id().equalsIgnoreCase("975786409")) return;
        String msg = label.substring((name+" ").length());
        //Thread thread = new Thread(() -> {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                String inputLine;
                URL url =new URL("http://api.qingyunke.com/api.php?key=free&appid=0&msg="+msg);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
                while ((inputLine = in.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Info info = GsonUtils.jsonToBean(stringBuilder.toString(),Info.class);
            sendMessage(new AtResult(message.getUser_id()).toJSON() + info.content,message);
       // });
      //  thread.start();
    }

    public static class Info {
        int result;
        String content;
    }
}
