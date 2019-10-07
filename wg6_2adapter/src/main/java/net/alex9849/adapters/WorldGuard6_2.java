package net.alex9849.adapters;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.*;

public class WorldGuard6_2 extends WorldGuard6_1 {

    public Flag fuzzyMatchFlag(String id) {
        return DefaultFlag.fuzzyMatchFlag(WorldGuardPlugin.inst().getFlagRegistry(), id);
    }

    public <V> V parseFlagInput(Flag<V> flag, String input) throws InvalidFlagFormat {
        return flag.parseInput(FlagContext.create().setInput(input).build());
    }

    @Override
    public RegionGroup parseFlagInput(RegionGroupFlag flag, String input) throws InvalidFlagFormat {
        return flag.parseInput(FlagContext.create().setInput(input).build());
    }

}
