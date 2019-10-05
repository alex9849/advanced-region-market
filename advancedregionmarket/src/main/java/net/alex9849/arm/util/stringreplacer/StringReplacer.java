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
        StringBuffer sb = new StringBuffer(string);
        return replace(sb);
    }

    public StringBuffer replace(StringBuffer stringBuffer) {
        stringBuffer.ensureCapacity(stringBuffer.capacity() + minExtraLength);
        for(Map.Entry<String, StringCreator> entry : replacerMap.entrySet()) {
            replace(stringBuffer, entry.getKey(), entry.getValue());
        }
        return stringBuffer;
    }

    private void replace(StringBuffer stringBuffer, String variable, StringCreator replacement) {

        int start = stringBuffer.indexOf(variable);
        String replacementString = null;
        if(start > -1) {
            replacementString = replacement.create();
        }
        while (start > -1) {
            int end = start + variable.length();
            int nextSearchStart = start + replacementString.length();
            stringBuffer.replace(start, end, replacementString);
            start = stringBuffer.indexOf(variable, nextSearchStart);
        }
    }

}
