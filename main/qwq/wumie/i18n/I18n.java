package qwq.wumie.i18n;

import qwq.wumie.Launch;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

public class I18n {

    private Map<String, String> locales = new HashMap<>();
    private Map<String, String> fallbackLocales = new HashMap<>();
    private final String prefix;
    private ResourceBundle bundle;

    public I18n(Language language, String defaultPrefix) {
        this(language, defaultPrefix, false);
    }

    public I18n(Language language, String defaultPrefix, boolean silent) {
        this.prefix = defaultPrefix;
        try {
            Properties properties = new Properties();
            properties.load(new InputStreamReader(I18n.class.getResourceAsStream("/lang/" + language.getLanguageCode() + ".properties"), StandardCharsets.UTF_8));
            this.locales = new HashMap(properties);
            properties = new Properties();
            properties.load(new InputStreamReader(I18n.class.getResourceAsStream("/lang/zh.properties"), StandardCharsets.UTF_8));
            this.fallbackLocales = new HashMap(properties);
            this.bundle = ResourceBundle.getBundle("lang/", language.getLocale(), new UTF8Control());

            if (!silent)
                Launch.info("Using language: " + language + " @ /lang/" + language.getLanguageCode() + ".properties");
        } catch (Exception e) {
            Launch.info("Failed loading language " + language.name() + ": /lang/" + language.getLanguageCode() + ".properties");
            Launch.info("Falling back to default langauge CHINESE");
            e.printStackTrace();
        }
    }

    public String t(String key, Object... args) {
        String value = locales.get(key);
        if (value == null)
            value = fallbackLocales.get(key);
        return value == null ? "N/A" : MessageFormat.format(value, args).replace("%prefix%", prefix);
    }

    public String get(String key, Object... args) {
        return t(key,args);
    }

    public void info(String key, Object... args) {
        for (String line : t(key, args).split("\n")) {
            Launch.info(line);
        }
    }

    public void warning(String key, Object... args) {
        for (String line : t(key, args).split("\n")) {
            Launch.info(line);
        }
    }

    public void severe(String key, Object... args) {
        for (String line : t(key, args).split("\n")) {
            Launch.info(line);
        }
    }
}
