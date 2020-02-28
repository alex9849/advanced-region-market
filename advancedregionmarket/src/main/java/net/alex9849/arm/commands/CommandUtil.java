package net.alex9849.arm.commands;

import java.util.List;

public class CommandUtil {

    public static <X> String getStringList(List<X> xList, StringGetter<X> stringGetter) {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for(X x : xList) {
            if(first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(stringGetter.get(x));
        }
        return sb.toString();
    }


    public interface StringGetter<X> {
        String get(X x);
    }
}
