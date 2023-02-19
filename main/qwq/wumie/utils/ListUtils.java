package qwq.wumie.utils;

import java.util.List;

public class ListUtils {
    public static boolean inList(List list,Object value) {
        for (Object o : list) {
            if (o.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
