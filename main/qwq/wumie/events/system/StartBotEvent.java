package qwq.wumie.events.system;

public class StartBotEvent {
    private static final StartBotEvent INSTANCE = new StartBotEvent();

    public static StartBotEvent get() {
        return INSTANCE;
    }
}
