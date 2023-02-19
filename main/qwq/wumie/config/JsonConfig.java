package qwq.wumie.config;

import qwq.wumie.i18n.Language;

public class JsonConfig {
    boolean debug;
    boolean autoReconnect;
    String serverPort;
    String clientUrl;
    boolean enableJRRP;
    boolean enableDice;
    boolean enableSay;
    boolean enableTest;
    boolean enableKey;
    String language;

    public JsonConfig(boolean debug, boolean autoReconnect, String serverPort, String clientUrl, boolean enableJRRP, boolean enableDice, boolean enableSay, boolean enableTest, boolean enableKey, String language) {
        this.debug = debug;
        this.autoReconnect = autoReconnect;
        this.serverPort = serverPort;
        this.clientUrl = clientUrl;
        this.enableJRRP = enableJRRP;
        this.enableDice = enableDice;
        this.enableSay = enableSay;
        this.enableTest = enableTest;
        this.enableKey = enableKey;
        this.language = language;
    }

    public Language getLanguage() {
        return Language.getByName(language);
    }

    public void setLanguage(Language language) {
        this.language = language.getLocale().getLanguage();
    }

    public boolean isEnableKey() {
        return enableKey;
    }

    public void setEnableKey(boolean enableKey) {
        this.enableKey = enableKey;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public boolean isEnableJRRP() {
        return enableJRRP;
    }

    public void setEnableJRRP(boolean enableJRRP) {
        this.enableJRRP = enableJRRP;
    }

    public boolean isEnableDice() {
        return enableDice;
    }

    public void setEnableDice(boolean enableDice) {
        this.enableDice = enableDice;
    }

    public boolean isEnableSay() {
        return enableSay;
    }

    public void setEnableSay(boolean enableSay) {
        this.enableSay = enableSay;
    }

    public boolean isEnableTest() {
        return enableTest;
    }

    public void setEnableTest(boolean enableTest) {
        this.enableTest = enableTest;
    }
}
