package net.alex9849.arm.util.stringreplacer;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

import java.util.Collection;
import java.util.HashMap;

public class StringReplacer {
    private HashMap<String, StringCreator> replacerMap;
    private Trie ahoCorasickTrie;
    private int minExtraLength;

    public StringReplacer(HashMap<String, StringCreator> replacerMap, int minExtraLength) {
        this.replacerMap = replacerMap;
        this.minExtraLength = minExtraLength;
        this.ahoCorasickTrie = Trie.builder().ignoreOverlaps().
                addKeywords(this.replacerMap.keySet()).build();
    }

    public StringBuffer replace(String string) {
        Collection<Emit> emits = this.ahoCorasickTrie.parseText(string);
        StringBuffer sb = new StringBuffer(string);
        sb.ensureCapacity(sb.capacity() + this.minExtraLength);
        int shifted = 0;
        for(Emit emit : emits) {
            StringCreator replacement = this.replacerMap.get(emit.getKeyword());
            if(replacement == null) {
                continue;
            }
            String replaceString = replacement.create();
            sb.replace(emit.getStart() + shifted, emit.getEnd() + shifted + 1, replaceString);
            shifted += replaceString.length() - emit.getKeyword().length();
        }
        return sb; //replace(sb);
    }

    /*public StringBuffer replace(StringBuffer stringBuffer) {
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
    } */

}
