package net.alex9849.arm.commands;

import java.util.Iterator;
import java.util.List;

public class CommandUtil {

    public static <X> String getStringList(List<X> xList, StringGetter<X> stringGetter, String splitter) {
        StringBuilder sb = new StringBuilder();

        Iterator<X> iterator = xList.iterator();
        while (iterator.hasNext()) {
            X x = iterator.next();
            sb.append(stringGetter.get(x));
            if (iterator.hasNext()) {
                sb.append(splitter);
            }
        }
        return sb.toString();
    }


    public interface StringGetter<X> {
        String get(X x);
    }
}
