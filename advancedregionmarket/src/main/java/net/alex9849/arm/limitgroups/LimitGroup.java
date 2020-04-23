package net.alex9849.arm.limitgroups;

import net.alex9849.arm.regionkind.RegionKind;

import java.util.HashMap;
import java.util.Map;

public class LimitGroup {
    private Integer total = -1;
    private HashMap<RegionKind, Integer> regionKindLimits = new HashMap<>();
    private String name;

    public LimitGroup(String name) {
        this.name = name;
    }

    public int getTotalLimit() {
        return this.total;
    }

    public void setTotalLimit(int total) {
        if(total < -1) {
            total = 0;
        }
        this.total = total;
    }

    public void addLimit(RegionKind regionKind, int limit) {
        if(limit < -1) {
            limit = 0;
        }
        regionKindLimits.put(regionKind, limit);
    }

    public Map<RegionKind, Integer> getLimits() {
        return new HashMap<>(this.regionKindLimits);
    }

    public String getName() {
        return this.name;
    }
}
