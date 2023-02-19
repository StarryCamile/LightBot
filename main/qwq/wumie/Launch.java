package qwq.wumie;

import org.java_websocket.enums.ReadyState;
import qwq.wumie.config.JsonConfig;
import qwq.wumie.events.EventBus;
import qwq.wumie.events.IEventBus;
import qwq.wumie.events.system.StartBotEvent;
import qwq.wumie.events.system.StopBotEvent;
import qwq.wumie.events.system.WhileEvent;
import qwq.wumie.i18n.I18n;
import qwq.wumie.i18n.Language;
import qwq.wumie.module.Manager;
import qwq.wumie.module.impl.register.Register;
import qwq.wumie.server.Server;
import qwq.wumie.utils.FileUtils;
import qwq.wumie.utils.GsonUtils;
import qwq.wumie.utils.RandomUtils;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Launch {
    public static I18n i18n;
    public static IEventBus EVENT_BUS = new EventBus();
    private static Server mainServer;
    private static RedirectionClient mainClient;
    private static Launch instance;
    public static Manager moduleManager;
    public static boolean running = false;
    public static String ownerKey;
    public static final File FOLDER = new File("LightBot");
    public static File data_FOLDER = new File(Launch.FOLDER,"java_data");
    public static final File configFile = new File(FOLDER, "java_configs.json");
    public static JsonConfig config = new JsonConfig(false, true, "14514", "ws://127.0.0.1:5608/qb", true, false, false, false,true,"zh");

    public static Server getMainServer() {
        return mainServer;
    }

    public static RedirectionClient getMainClient() {
        return mainClient;
    }

    public static Launch getInstance() {
        return instance;
    }

    public static void stop() {
        running = false;
    }

    public static void startClient() throws InterruptedException, URISyntaxException {
        running = true;
        RedirectionClient client = new RedirectionClient(config.getClientUrl());
        mainClient = client;
        info("--正在启动转发服务--");
        info("ServerUrl: " + client.getURI().toString());
        client.connectA();
        while (running) {
            if (config.isAutoReconnect()) {
                if (!mainClient.getReadyState().equals(ReadyState.OPEN)) {
                    if (config.isDebug()) info("转发端正在尝试连接到Py Server");
                    mainClient.reconnect();
                    Thread.sleep(2500);
                }
            }
            Thread.sleep(1);
        }
        EVENT_BUS.post(StopBotEvent.get());
        info("[Stop] 正在关闭转发端");
        mainClient.close();
        info("[Stop] 正在关闭服务器");
        mainServer.stop(1000);
        saveConfig();
        Runtime.getRuntime().exit(0);
    }

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        running = true;
        EVENT_BUS.registerLambdaFactory("", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
        if (!FOLDER.exists()) FOLDER.mkdirs();
        if (!data_FOLDER.exists()) data_FOLDER.mkdirs();
        if (!configFile.exists()) saveConfig();
        loadConfig(configFile);
        saveConfig();
        i18n = new I18n(config.getLanguage(),"QQBot",true);
        Server server = new Server(Integer.parseInt(config.getServerPort()));
        Thread clientThread = new Thread(() -> {
            try {
                Launch.startClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        clientThread.setName("Client ->");
        clientThread.start();
        Thread.currentThread().setName("Server ->");
        info("--正在启动WebSocket服务--");
        info("Port: " + server.getPort());
        server.start();
        i18n = new I18n(config.getLanguage(),"QQBot");
        instance = new Launch();
        mainServer = server;
        moduleManager = new Manager();
        moduleManager.init(instance);
        EVENT_BUS.post(StartBotEvent.get());
        BufferedReader sIn = new BufferedReader(new InputStreamReader(System.in));
        Thread whileThread = new Thread(() -> {
            while (running) {
                EVENT_BUS.post(WhileEvent.get());
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        whileThread.start();
        while (running) {
            Thread.sleep(1);
            String in = sIn.readLine();
            if (in.equals("exit")) {
                stop();
                break;
            }
        }
    }

    public static void reload() {
        Register.reload();
        loadConfig(configFile);
    }

    public static void loadConfig(File jsonFile) {
        updateKey();
        String jsonString = readText(jsonFile).get(0);
        config = GsonUtils.jsonToBean(jsonString, JsonConfig.class);
    }

    public static void saveConfig() {
        String jsonString = GsonUtils.beanToJson(config);
        FileUtils.save(FOLDER, "java_configs.json", jsonString);
    }

    public static void updateKey() {
        ownerKey = RandomUtils.randomString(16);
        FileUtils.save(FOLDER,"keys.txt",ownerKey);
        info("[Key] 更新Key为 "+ownerKey);
    }

    public static ArrayList<String> readText(File inputFile) {
        final ArrayList<String> readContent = new ArrayList<>();
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8));
            String str;
            while ((str = in.readLine()) != null) {
                readContent.add(str);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readContent;
    }

    public static void info(String message) {
        System.out.println("[QQBot] " + message);
    }
}
