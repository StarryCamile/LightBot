package qwq.wumie;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import qwq.wumie.utils.FileUtils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class RedirectionClient extends WebSocketClient {
    public RedirectionClient(String serverUri) throws URISyntaxException {
        super(new URI(serverUri));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Launch.info("转发端已连接.");
    }

    @Override
    public void onMessage(String message) {
        if (message.equalsIgnoreCase("CLIENT_EXIT")) {
            Launch.stop();
            return;
        }

        if (message.equalsIgnoreCase("!JSON_")) {
            String path = message.substring("!JSON_".length());
            File file = new File(Launch.data_FOLDER,path);
            send("!RETURN_JSON_"+FileUtils.read(file).get(0));
        }

        Launch.getMainServer().onMessage(null,"__Client:"+message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (Launch.config.isDebug()) Launch.info("转发端断开连接.");
    }

    @Override
    public void onError(Exception ex) {

    }

    public void connectA() {
        Launch.info("正在连接到Py Server");
        this.connect();
    }
}
