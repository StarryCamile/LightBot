package qwq.wumie.events.system;

public class WhileEvent {
    private static final WhileEvent INSTANCE = new WhileEvent();

    public static WhileEvent get() {
        return INSTANCE;
    }
}
