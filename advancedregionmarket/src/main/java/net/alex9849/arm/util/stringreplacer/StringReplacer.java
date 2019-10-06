package net.alex9849.arm.util.stringreplacer;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;

public class StringReplacer {
    private HashMap<String, StringCreator> replacerMap;
    private HashMap<String, Collection<Emit>> knownStrings;
    private Trie ahoCorasickTrie;
    private int minExtraLength;

    public StringReplacer(HashMap<String, StringCreator> replacerMap, int minExtraLength) {
        this.replacerMap = replacerMap;
        this.minExtraLength = minExtraLength;
        this.ahoCorasickTrie = Trie.builder().ignoreOverlaps().
                addKeywords(this.replacerMap.keySet()).build();
        this.knownStrings = new HashMap<>();
    }

    public StringBuffer replace(String string) {
        return this.replace(string, false);
    }

    public StringBuffer replace(String string, boolean textlerning) {
        Collection<Emit> emits;


        if(textlerning) {
            emits = this.knownStrings.get(string);
            if(emits == null) {
                emits = this.ahoCorasickTrie.parseText(string);
                this.knownStrings.put(string, emits);
            }
        } else {
            long timer = System.nanoTime();
            emits = this.ahoCorasickTrie.parseText(string);
            Bukkit.getLogger().log(Level.INFO, "Aho-Corasick: " + (System.nanoTime() - timer));
        }

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
        return sb;
    }

}
