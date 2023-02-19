package qwq.wumie.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import qwq.wumie.Launch;
import qwq.wumie.events.message.MessageEvent;
import qwq.wumie.server.message.GMessage;
import qwq.wumie.utils.GsonUtils;

import java.net.InetSocketAddress;

public class Server extends WebSocketServer {
    private int connections = 0;
    public GMessage currentMessage;

    public Server(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connections++;
        Launch.info("一个客户端连接到此,当前连接数: "+connections);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections--;
        Launch.info("一个客户端断开连接,当前连接数: "+connections);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        GMessage info = GsonUtils.jsonToBean(message,GMessage.class);
        if (message.startsWith("__Client:") && conn == null) {
            String msg = message.substring("__Client:".length());
            broadcast(msg);
            return;
        }
        if (info.getPost_type().equalsIgnoreCase("message")) {
            MessageEvent event = MessageEvent.get(message,info);
            Launch.EVENT_BUS.post(event);
            if (event.isCancelled()) return;

            currentMessage = info;
            GMessage.Sender sender = info.getSender();
            Launch.info("[" + sender.getRole() + "] " + (sender.getCard().isEmpty() ? sender.getNickname() : sender.getCard()) + " -> \n" + info.getRaw_message());
            Launch.moduleManager.in(info,sender);

            if (Launch.config.isEnableSay() && info.getRaw_message().startsWith("say")) return;
            if (Launch.config.isEnableDice() && info.getRaw_message().equalsIgnoreCase("骰子")) return;
            Launch.getMainClient().send(message);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }

    @Override
    public void onStart() {
        Launch.info("已开启接收Server.");
    }
}
