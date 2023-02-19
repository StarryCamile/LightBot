package qwq.wumie.events.message;

import qwq.wumie.events.Cancellable;
import qwq.wumie.server.message.GMessage;

public class MessageEvent extends Cancellable {
    private static final MessageEvent INSTANCE = new MessageEvent();

    public String text;
    public GMessage message;

    public static MessageEvent get(String text,GMessage message) {
        INSTANCE.text = text;
        INSTANCE.message = message;
        return INSTANCE;
    }
}
