package net.alex9849.arm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringReplacer {
    private final HashMap<Pattern, Supplier<String>> replacerMap;

    public StringReplacer(HashMap<String, Supplier<String>> replacerMap) {
        this.replacerMap = new HashMap<>();
        for (Map.Entry<String, Supplier<String>> entry : replacerMap.entrySet()) {
            Pattern pattern = Pattern.compile(entry.getKey());
            this.replacerMap.put(pattern, entry.getValue());
        }
    }

    public String replace(String string) {
        for (Map.Entry<Pattern, Supplier<String>> entry : replacerMap.entrySet()) {
            Matcher matcher = entry.getKey().matcher(string);
            if (matcher.matches()) {
                string = matcher.replaceAll(entry.getValue().get());
            }
        }
        return string;
    }
}
