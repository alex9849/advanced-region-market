package net.alex9849.arm.limitgroups;

import net.alex9849.arm.regionkind.LimitGroupElement;

import java.util.HashMap;

public class LimitGroup {
    private Integer total = -1;
    private HashMap<LimitGroupElement, Integer> regionKindLimits = new HashMap<>();
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

    public void addLimit(LimitGroupElement element, int limit) {
        if(limit < -1) {
            limit = 0;
        }
        regionKindLimits.put(element, limit);
    }

    public Integer getLimit(LimitGroupElement element) {
        return this.regionKindLimits.get(element);
    }

    public String getName() {
        return this.name;
    }
}
