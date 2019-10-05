package net.alex9849.arm.util.stringreplacer;

import java.util.HashMap;
import java.util.Map;

public class StringReplacer {
    private HashMap<String, StringCreator> replacerMap;
    private int minExtraLength;

    public StringReplacer(HashMap<String, StringCreator> replacerMap, int minExtraLength) {
        this.replacerMap = replacerMap;
        this.minExtraLength = minExtraLength;
    }

    public StringBuffer replace(String string) {
        return replace(new StringBuffer(string));
    }

    public StringBuffer replace(StringBuffer stringBuffer) {
        stringBuffer.ensureCapacity(stringBuffer.capacity() + minExtraLength);
        for(Map.Entry<String, StringCreator> entry : replacerMap.entrySet()) {
            int index = stringBuffer.indexOf(entry.getKey());
            while (index >= 0) {
                stringBuffer.replace(index, index + entry.getKey().length(), entry.getValue().create());
                index = stringBuffer.indexOf(entry.getKey());
            }
        }
        return stringBuffer;
    }

}
