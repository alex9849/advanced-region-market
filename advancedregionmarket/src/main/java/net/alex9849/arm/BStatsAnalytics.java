package net.alex9849.arm;

import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.regions.SellRegion;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;

public class BStatsAnalytics {

    public static RegionStatistics getRegionStatistics() {
        RegionStatistics regionStatistics = new RegionStatistics();

        for (Region region : AdvancedRegionMarket.getInstance().getRegionManager()) {
            if (region instanceof SellRegion) {
                if (region.isSold()) {
                    regionStatistics.soldSellRegions++;
                } else {
                    regionStatistics.availableSellRegions++;
                }

            } else if (region instanceof RentRegion) {
                if (region.isSold()) {
                    regionStatistics.soldRentRegions++;
                } else {
                    regionStatistics.availableRentRegions++;
                }

            } else if (region instanceof ContractRegion) {
                if (region.isSold()) {
                    regionStatistics.soldContractRegions++;
                } else {
                    regionStatistics.availableContractRegions++;
                }

            }
        }
        return regionStatistics;
    }

    public void register(Plugin plugin) {
        try {
            Metrics metrics = new Metrics(plugin);

            metrics.addCustomChart(new Metrics.SingleLineChart("total_regions", new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    RegionStatistics regionStatistics = BStatsAnalytics.getRegionStatistics();
                    int total_regions = regionStatistics.availableContractRegions;
                    total_regions += regionStatistics.availableRentRegions;
                    total_regions += regionStatistics.availableSellRegions;
                    total_regions += regionStatistics.soldContractRegions;
                    total_regions += regionStatistics.soldRentRegions;
                    total_regions += regionStatistics.soldSellRegions;
                    return total_regions;
                }
            }));

            metrics.addCustomChart(new Metrics.AdvancedPie("region_status", new Callable<Map<String, Integer>>() {
                @Override
                public Map<String, Integer> call() throws Exception {
                    RegionStatistics regionStatistics = BStatsAnalytics.getRegionStatistics();
                    Map<String, Integer> map = new HashMap<>();
                    map.put("Sellregion available", regionStatistics.availableSellRegions);
                    map.put("Sellregion sold", regionStatistics.soldSellRegions);
                    map.put("Rentregion available", regionStatistics.availableRentRegions);
                    map.put("Rentregion sold", regionStatistics.soldRentRegions);
                    map.put("Contractregion available", regionStatistics.availableContractRegions);
                    map.put("Contractregion sold", regionStatistics.soldContractRegions);
                    return map;
                }
            }));

            metrics.addCustomChart(new Metrics.SingleLineChart("total_sellregions", new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    RegionStatistics regionStatistics = BStatsAnalytics.getRegionStatistics();
                    int total_sellregions = regionStatistics.availableSellRegions;
                    total_sellregions += regionStatistics.soldSellRegions;
                    return total_sellregions;
                }
            }));

            metrics.addCustomChart(new Metrics.SingleLineChart("total_rentregions", new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    RegionStatistics regionStatistics = BStatsAnalytics.getRegionStatistics();
                    int total_rentregions = regionStatistics.availableRentRegions;
                    total_rentregions += regionStatistics.soldRentRegions;
                    return total_rentregions;
                }
            }));

            metrics.addCustomChart(new Metrics.SingleLineChart("total_contractregions", new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    RegionStatistics regionStatistics = BStatsAnalytics.getRegionStatistics();
                    int total_contractregions = regionStatistics.availableContractRegions;
                    total_contractregions += regionStatistics.soldContractRegions;
                    return total_contractregions;
                }
            }));

        } catch (Exception e) {
            Bukkit.getLogger().log(Level.INFO, "Could not generate statistics for bStats!");
        }
    }

    public static class RegionStatistics {
        private int availableSellRegions = 0;
        private int soldSellRegions = 0;
        private int availableRentRegions = 0;
        private int soldRentRegions = 0;
        private int availableContractRegions = 0;
        private int soldContractRegions = 0;

        private RegionStatistics() {}

        public int getAvailableSellRegions() {
            return availableSellRegions;
        }

        public void setAvailableSellRegions(int availableSellRegions) {
            this.availableSellRegions = availableSellRegions;
        }

        public int getSoldSellRegions() {
            return soldSellRegions;
        }

        public void setSoldSellRegions(int soldSellRegions) {
            this.soldSellRegions = soldSellRegions;
        }

        public int getAvailableRentRegions() {
            return availableRentRegions;
        }

        public void setAvailableRentRegions(int availableRentRegions) {
            this.availableRentRegions = availableRentRegions;
        }

        public int getSoldRentRegions() {
            return soldRentRegions;
        }

        public void setSoldRentRegions(int soldRentRegions) {
            this.soldRentRegions = soldRentRegions;
        }

        public int getAvailableContractRegions() {
            return availableContractRegions;
        }

        public void setAvailableContractRegions(int availableContractRegions) {
            this.availableContractRegions = availableContractRegions;
        }

        public int getSoldContractRegions() {
            return soldContractRegions;
        }

        public void setSoldContractRegions(int soldContractRegions) {
            this.soldContractRegions = soldContractRegions;
        }
    }
}
