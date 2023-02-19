package qwq.wumie.i18n;

import java.util.Arrays;
import java.util.Locale;

public enum Language {
    CHINESE(new Locale("zh", "CN")),
    CHINESE1(new Locale("zh_bt", "CN"));

    private final Locale locale;

    Language(Locale locale) {
        this.locale = locale;
    }

    public String getLanguageCode() {
        return locale.getLanguage();
    }

    public static Language getByLocale(Locale locale) {
        return Arrays.stream(values())
                .filter(language -> language.locale.equals(locale))
                .findAny()
                .orElse(Language.CHINESE);
    }

    public static Language getByName(String name) {
        return Arrays.stream(values())
                .filter(language -> language.locale.getLanguage().equalsIgnoreCase(name))
                .findAny()
                .orElse(Language.CHINESE);
    }

    public Locale getLocale() {
        return locale;
    }
}
