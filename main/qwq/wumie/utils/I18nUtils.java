package qwq.wumie.utils;

import qwq.wumie.Launch;

import java.util.List;

public class I18nUtils {
    public static String get(String key) {
        return Launch.i18n.get(key);
    }

    public static String getConversion(String key,String[] str,String[] conversionStr) {
        String txt = get(key);
        List<String> l = List.of(str);
        List<String> l1 = List.of(conversionStr);
        for (int i = 0;i < l.size();i++) {
            String s = "%"+l.get(i);
            String s1 = l1.get(i);
            txt = txt.replace(s,s1);
        }
        return txt;
    }

    public static String getConversion(String key,String str,String conversionStr) {
        String txt = get(key);
        while (txt.contains("%"+str)) {
            txt = txt.replace("%"+str,conversionStr);
        }
        return txt;
    }

    public static String conversion(String all,String str,String conversionStr) {
        String txt = all;
        while (txt.contains("%"+str)) {
            txt = txt.replace("%"+str,conversionStr);
        }
        return txt;
    }
}
