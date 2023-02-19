package qwq.wumie.events.system;

public class StopBotEvent {
    private static final StopBotEvent INSTANCE = new StopBotEvent();

    public static StopBotEvent get() {
        return INSTANCE;
    }
}
