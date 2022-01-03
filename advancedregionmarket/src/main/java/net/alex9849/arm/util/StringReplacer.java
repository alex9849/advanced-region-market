package net.alex9849.arm.util;

import java.util.HashMap;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringReplacer {
    private final HashMap<String, Supplier<String>> replacerMap;
    private final Pattern pattern;

    public StringReplacer(HashMap<String, Supplier<String>> replacerMap) {
        this.replacerMap = replacerMap;
        this.pattern = Pattern.compile("(\\%[^%\n ]+\\%)");
    }

    public String replace(String string) {
        Matcher matcher = pattern.matcher(string);
        StringBuilder sb = new StringBuilder(string);

        int shifted = 0;
        while (matcher.find()) {
            Supplier<String> stringSupplier = replacerMap.get(matcher.group());
            if(stringSupplier != null) {
                String replacement = stringSupplier.get();
                sb.replace(matcher.start() + shifted, matcher.end() + shifted, replacement);
                shifted += replacement.length() - matcher.group().length();
            }
        }
        return sb.toString();
    }
}
