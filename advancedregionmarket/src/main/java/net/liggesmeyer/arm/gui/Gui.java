package net.liggesmeyer.arm.gui;

import net.liggesmeyer.arm.Main;
import net.liggesmeyer.arm.Messages;
import net.liggesmeyer.arm.Permission;
import net.liggesmeyer.arm.regions.Region;
import net.liggesmeyer.arm.regions.RentRegion;
import net.liggesmeyer.arm.Group.LimitGroup;
import net.liggesmeyer.arm.regions.RegionKind;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Gui implements Listener {
    private static Material REGION_OWNER_ITEM = Material.ENDER_CHEST;
    private static Material REGION_MEMBER_ITEM = Material.CHEST;
    private static Material REGION_FINDER_ITEM = Material.COMPASS;
    private static Material GO_BACK_ITEM = Material.OAK_DOOR;
    private static Material WARNING_YES_ITEM = Material.MELON_STEM;
    private static Material WARNING_NO_ITEM = Material.REDSTONE_BLOCK;
    private static Material TP_ITEM = Material.ENDER_PEARL;
    private static Material SELL_REGION_ITEM = Material.DIAMOND;
    private static Material RESET_ITEM = Material.TNT;
    private static Material EXTEND_ITEM = Material.CLOCK;
    private static Material INFO_ITEM = Material.BOOK;
    private static Material PROMOTE_MEMBER_TO_OWNER_ITEM = Material.LADDER;
    private static Material REMOVE_MEMBER_ITEM = Material.LAVA_BUCKET;

    public static void openARMGui(Player player) {
        CustomHolder menu = new CustomHolder(9, Messages.GUI_MAIN_MENU_NAME);

        ItemStack myRegions = new ItemStack(Gui.REGION_OWNER_ITEM);
        ItemMeta myRegionsMeta = myRegions.getItemMeta();
        myRegionsMeta.setDisplayName(Messages.GUI_MY_OWN_REGIONS);
        myRegions.setItemMeta(myRegionsMeta);

        ItemStack mymRegions = new ItemStack(Gui.REGION_MEMBER_ITEM);
        ItemMeta mymRegionsMeta = mymRegions.getItemMeta();
        mymRegionsMeta.setDisplayName(Messages.GUI_MY_MEMBER_REGIONS);
        mymRegions.setItemMeta(mymRegionsMeta);

        ItemStack searchRegion = new ItemStack(Gui.REGION_FINDER_ITEM);
        ItemMeta searchRegionMeta = searchRegion.getItemMeta();
        searchRegionMeta.setDisplayName(Messages.GUI_SEARCH_FREE_REGION);
        searchRegion.setItemMeta(searchRegionMeta);

        Icon regionMenu = new Icon(myRegions, 0).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openRegionOwnerGui(player);
            }
        });

        menu.addIcon(regionMenu);

        Icon regionMemberMenu = new Icon(mymRegions, 4).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openRegionMemberGui(player);
            }
        });

        menu.addIcon(regionMemberMenu);

        Icon regionfinder = new Icon(searchRegion, 8).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openRegionFinder(player);
            }
        });

        menu.addIcon(regionfinder);

        player.openInventory(menu.getInventory());
    }

    public static void openRegionOwnerGui(Player player) {
        List<Region> regions = Region.getRegionsByOwner(player.getUniqueId());

        int invsize = 0;
        while (regions.size() + 1 > invsize) {
            invsize = invsize + 9;
        }

        CustomHolder inv = new CustomHolder(invsize, Messages.GUI_OWN_REGIONS_MENU_NAME);

        for (int i = 0; i < regions.size(); i++) {
            String displaykind = "";
            if(regions.get(i).getRegionKind() != RegionKind.DEFAULT){
                displaykind = " (" + regions.get(i).getRegionKind().getName() + ")";
            }

            if(regions.get(i) instanceof RentRegion) {
                RentRegion region = (RentRegion) regions.get(i);
                ItemStack stack = new ItemStack(region.getLogo());
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName(region.getRegion().getId() + displaykind);
                List<String> regionlore = new LinkedList<>(Messages.GUI_RENT_REGION_LORE);
                for (int j = 0; j < regionlore.size(); j++) {
                    regionlore.set(j, regionlore.get(j).replace("%extendpercick%", region.getExtendPerClick()));
                    regionlore.set(j, regionlore.get(j).replace("%currency%", Messages.CURRENCY));
                    regionlore.set(j, regionlore.get(j).replace("%price%", region.getPrice() + ""));
                    regionlore.set(j, regionlore.get(j).replace("%remaining%", region.calcRemainingTime()));
                    regionlore.set(j, regionlore.get(j).replace("%maxrenttime%", region.getMaxRentTime()));
                }
                meta.setLore(regionlore);
                stack.setItemMeta(meta);
                Icon icon = new Icon(stack, i).addClickAction(new ClickAction() {
                    @Override
                    public void execute(Player player) {
                        Gui.decideRentSellOwnerManager(player, region);
                    }
                });
                inv.addIcon(icon);
            } else {
                ItemStack stack = new ItemStack(regions.get(i).getLogo());
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName(regions.get(i).getRegion().getId() + displaykind);
                stack.setItemMeta(meta);
                int finalI = i;
                Icon icon = new Icon(stack, i).addClickAction(new ClickAction() {
                    @Override
                    public void execute(Player player) {
                        Gui.decideRentSellOwnerManager(player, regions.get(finalI));
                    }
                });
                inv.addIcon(icon);
            }
        }

        ItemStack goBack = new ItemStack(Gui.GO_BACK_ITEM);
        ItemMeta goBackMeta = goBack.getItemMeta();
        goBackMeta.setDisplayName(Messages.GUI_GO_BACK);
        goBack.setItemMeta(goBackMeta);

        Icon gobackButton = new Icon(goBack, (invsize - 1)).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openARMGui(player);
            }
        });

        inv.addIcon(gobackButton);

        player.openInventory(inv.getInventory());

    }

    public static void openRegionManagerOwner(Player player, Region region) {

        int itemcounter = 2;
        int actitem = 1;

        if(player.hasPermission(Permission.MEMBER_TP)){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_RESETREGION)){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_RESETREGIONBLOCKS)){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_INFO)){
            itemcounter++;
        }

        CustomHolder inv = new CustomHolder(9 , region.getRegion().getId());

        ItemStack membersitem = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta membersitemmeta = (SkullMeta) membersitem.getItemMeta();
        membersitemmeta.setOwner(player.getDisplayName());
        membersitemmeta.setDisplayName(Messages.GUI_MEMBERS_BUTTON);
        membersitem.setItemMeta(membersitemmeta);
        Icon membersicon = new Icon(membersitem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openMemberList(player, region);
            }
        });
        inv.addIcon(membersicon);

        actitem++;

        if(player.hasPermission(Permission.MEMBER_TP)){
            ItemStack teleporteritem = new ItemStack(Gui.TP_ITEM);
            ItemMeta teleporteritemmeta = teleporteritem.getItemMeta();
            teleporteritemmeta.setDisplayName(Messages.GUI_TELEPORT_TO_REGION_BUTTON);
            teleporteritemmeta.setLore(Messages.GUI_TELEPORT_TO_REGION_BUTTON_LORE);
            teleporteritem.setItemMeta(teleporteritemmeta);
            Icon teleportericon = new Icon(teleporteritem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    region.teleportToRegion(player);
                    player.closeInventory();
                }
            });
            inv.addIcon(teleportericon);

            actitem++;
        }

        if(player.hasPermission(Permission.MEMBER_RESETREGIONBLOCKS)) {
            ItemStack resetItem = new ItemStack(Gui.RESET_ITEM);
            ItemMeta resetitemItemMeta = resetItem.getItemMeta();
            resetitemItemMeta.setDisplayName(Messages.GUI_RESET_REGION_BUTTON);
            List<String> message = new LinkedList<>(Messages.GUI_RESET_REGION_BUTTON_LORE);
            for (int i = 0; i < message.size(); i++) {
                message.set(i, message.get(i).replace("%days%", Region.getResetCooldown() + ""));
            }
            resetitemItemMeta.setLore(message);
            resetItem.setItemMeta(resetitemItemMeta);
            Icon reseticon = new Icon(resetItem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    if(region.timeSinceLastReset() >= Region.getResetCooldown()){
                        Gui.openRegionResetWarning(player, region);
                    } else {
                        String message = Messages.RESET_REGION_COOLDOWN_ERROR.replace("%remainingdays%", (Region.getResetCooldown() - region.timeSinceLastReset()) + "");
                        player.sendMessage(Messages.PREFIX + message);
                    }
                }
            });
            inv.addIcon(reseticon);

            actitem++;
        }

        if(player.hasPermission(Permission.MEMBER_RESETREGION)) {
            ItemStack resetItem = new ItemStack(Gui.SELL_REGION_ITEM);
            ItemMeta resetitemItemMeta = resetItem.getItemMeta();
            resetitemItemMeta.setDisplayName(Messages.GUI_USER_SELL_BUTTON);
            List<String> message = new LinkedList<>(Messages.GUI_USER_SELL_BUTTON_LORE);
            for (int i = 0; i < message.size(); i++) {
                message.set(i, message.get(i).replace("%paybackmoney%", region.getPaybackMoney() + ""));
                message.set(i, message.get(i).replace("%currency%", Messages.CURRENCY));

            }
            resetitemItemMeta.setLore(message);
            resetItem.setItemMeta(resetitemItemMeta);
            Icon reseticon = new Icon(resetItem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    Gui.openSellWarning(player, region);
                }
            });
            inv.addIcon(reseticon);

            actitem++;
        }

        if(player.hasPermission(Permission.MEMBER_INFO)) {
            ItemStack infoitem = new ItemStack(Gui.INFO_ITEM);
            ItemMeta infoitemmeta = infoitem.getItemMeta();
            infoitemmeta.setDisplayName(Messages.GUI_SHOW_INFOS_BUTTON);
            infoitem.setItemMeta(infoitemmeta);
            Icon infoicon = new Icon(infoitem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    region.regionInfo(player);
                    player.closeInventory();
                }
            });
            inv.addIcon(infoicon);

            actitem++;
        }

        ItemStack gobackitem = new ItemStack(Gui.GO_BACK_ITEM);
        ItemMeta gobackitemmeta = gobackitem.getItemMeta();
        gobackitemmeta.setDisplayName(Messages.GUI_GO_BACK);
        gobackitem.setItemMeta(gobackitemmeta);
        Icon gobackicon = new Icon(gobackitem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openRegionOwnerGui(player);
            }
        });
        inv.addIcon(gobackicon);

        actitem++;


        player.openInventory(inv.getInventory());


    }

    public static void openRentRegionManagerOwner(Player player, RentRegion region) {

        int itemcounter = 3;
        int actitem = 1;

        if(player.hasPermission(Permission.MEMBER_TP)){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_RESETREGION)){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_RESETREGIONBLOCKS)){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_INFO)){
            itemcounter++;
        }

        CustomHolder inv = new CustomHolder(9 , region.getRegion().getId());


        ItemStack membersitem = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta membersitemmeta = (SkullMeta) membersitem.getItemMeta();
        membersitemmeta.setOwner(player.getDisplayName());
        membersitemmeta.setDisplayName(Messages.GUI_MEMBERS_BUTTON);
        membersitem.setItemMeta(membersitemmeta);
        Icon membersicon = new Icon(membersitem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openMemberList(player, region);
            }
        });
        inv.addIcon(membersicon);

        actitem++;

        if(player.hasPermission(Permission.MEMBER_TP)) {
            ItemStack teleporteritem = new ItemStack(Gui.TP_ITEM);
            ItemMeta teleporteritemmeta = teleporteritem.getItemMeta();
            teleporteritemmeta.setDisplayName(Messages.GUI_TELEPORT_TO_REGION_BUTTON);
            teleporteritemmeta.setLore(Messages.GUI_TELEPORT_TO_REGION_BUTTON_LORE);
            teleporteritem.setItemMeta(teleporteritemmeta);
            Icon teleportericon = new Icon(teleporteritem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    region.teleportToRegion(player);
                    player.closeInventory();
                }
            });
            inv.addIcon(teleportericon);

            actitem++;
        }


        if(player.hasPermission(Permission.MEMBER_RESETREGIONBLOCKS)) {

            ItemStack resetItem = new ItemStack(Gui.RESET_ITEM);
            ItemMeta resetitemItemMeta = resetItem.getItemMeta();
            resetitemItemMeta.setDisplayName(Messages.GUI_RESET_REGION_BUTTON);
            List<String> resetmessage = new LinkedList<>(Messages.GUI_RESET_REGION_BUTTON_LORE);
            for (int i = 0; i < resetmessage.size(); i++) {
                resetmessage.set(i, resetmessage.get(i).replace("%days%", Region.getResetCooldown() + ""));
            }
            resetitemItemMeta.setLore(resetmessage);
            resetItem.setItemMeta(resetitemItemMeta);
            Icon reseticon = new Icon(resetItem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    if(region.timeSinceLastReset() >= Region.getResetCooldown()){
                        Gui.openRegionResetWarning(player, region);
                    } else {
                        String message = Messages.RESET_REGION_COOLDOWN_ERROR.replace("%remainingdays%", (Region.getResetCooldown() - region.timeSinceLastReset()) + "");
                        player.sendMessage(Messages.PREFIX + message);
                    }
                }
            });
            inv.addIcon(reseticon);

            actitem++;
        }

        if(player.hasPermission(Permission.MEMBER_RESETREGION)) {
            ItemStack resetItem = new ItemStack(Gui.SELL_REGION_ITEM);
            ItemMeta resetitemItemMeta = resetItem.getItemMeta();
            resetitemItemMeta.setDisplayName(Messages.GUI_USER_SELL_BUTTON);
            List<String> message = new LinkedList<>(Messages.GUI_USER_SELL_BUTTON_LORE);
            for (int i = 0; i < message.size(); i++) {
                message.set(i, message.get(i).replace("%paybackmoney%", region.getPaybackMoney() + ""));
                message.set(i, message.get(i).replace("%currency%", Messages.CURRENCY));

            }
            resetitemItemMeta.setLore(message);
            resetItem.setItemMeta(resetitemItemMeta);
            Icon reseticon = new Icon(resetItem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    Gui.openSellWarning(player, region);
                }
            });
            inv.addIcon(reseticon);

            actitem++;
        }

        ItemStack extendItem = new ItemStack(Gui.EXTEND_ITEM);
        ItemMeta extendItemMeta = extendItem.getItemMeta();
        extendItemMeta.setDisplayName(Messages.GUI_EXTEND_BUTTON);
        List<String> extendmessage = new LinkedList<>(Messages.GUI_EXTEND_BUTTON_LORE);
        for (int i = 0; i < extendmessage.size(); i++) {
            extendmessage.set(i, extendmessage.get(i).replace("%extendpercick%", region.getExtendPerClick()));
            extendmessage.set(i, extendmessage.get(i).replace("%price%", region.getPrice() + ""));
            extendmessage.set(i, extendmessage.get(i).replace("%remaining%", region.calcRemainingTime()));
            extendmessage.set(i, extendmessage.get(i).replace("%maxrenttime%", region.getMaxRentTime()));
            extendmessage.set(i, extendmessage.get(i).replace("%currency%", Messages.CURRENCY));
        }
        extendItemMeta.setLore(extendmessage);
        extendItem.setItemMeta(extendItemMeta);
        Icon extendicon = new Icon(extendItem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                region.buy(player);
                Gui.decideRentSellOwnerManager(player, region);
            }
        });
        inv.addIcon(extendicon);

        actitem++;

        if(player.hasPermission(Permission.MEMBER_INFO)){
            ItemStack infoitem = new ItemStack(Gui.INFO_ITEM);
            ItemMeta infoitemmeta = infoitem.getItemMeta();
            infoitemmeta.setDisplayName(Messages.GUI_SHOW_INFOS_BUTTON);
            infoitem.setItemMeta(infoitemmeta);
            Icon infoicon = new Icon(infoitem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    region.regionInfo(player);
                    player.closeInventory();
                }
            });
            inv.addIcon(infoicon);

            actitem++;
        }

        ItemStack gobackitem = new ItemStack(Gui.GO_BACK_ITEM);
        ItemMeta gobackitemmeta = gobackitem.getItemMeta();
        gobackitemmeta.setDisplayName(Messages.GUI_GO_BACK);
        gobackitem.setItemMeta(gobackitemmeta);
        Icon gobackicon = new Icon(gobackitem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openRegionOwnerGui(player);
            }
        });
        inv.addIcon(gobackicon);
        actitem++;

        player.openInventory(inv.getInventory());
    }

    public static void openRegionFinder(Player player) {

        int itemcounter = 1;
        int actitem = 1;

        if(player.hasPermission(Permission.MEMBER_LIMIT)){
            itemcounter++;
        }

        int invsize = 0;

        while (RegionKind.getRegionKindList().size() + itemcounter > invsize) {
            invsize = invsize + 9;
        }

        CustomHolder inv = new CustomHolder(invsize, Messages.GUI_REGION_FINDER_MENU_NAME);

        for(int i = 0; i < RegionKind.getRegionKindList().size(); i++) {
            String type = RegionKind.getRegionKindList().get(i).getName();
            Material material = RegionKind.getRegionKindList().get(i).getMaterial();
            if(player.hasPermission(Permission.ARM_BUYKIND + type)){
                ItemStack stack = new ItemStack(material);
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName(type);
                meta.setLore(RegionKind.getRegionKindList().get(i).getLore());
                stack.setItemMeta(meta);
                Icon icon = new Icon(stack, i).addClickAction(new ClickAction() {
                    @Override
                    public void execute(Player player) {
                        Region.teleportToFreeRegion(type, player);
                    }
                });
                inv.addIcon(icon);
            }
        }

        if(player.hasPermission(Permission.MEMBER_LIMIT)){
            ItemStack goBack = new ItemStack(Gui.INFO_ITEM);
            ItemMeta goBackMeta = goBack.getItemMeta();
            goBackMeta.setDisplayName(Messages.GUI_MY_LIMITS_BUTTON);
            goBack.setItemMeta(goBackMeta);

            Icon gobackButton = new Icon(goBack, (invsize - 2)).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    LimitGroup.getLimitChat(player);
                }
            });

            inv.addIcon(gobackButton);
        }


        ItemStack goBack = new ItemStack(Gui.GO_BACK_ITEM);
        ItemMeta goBackMeta = goBack.getItemMeta();
        goBackMeta.setDisplayName(Messages.GUI_GO_BACK);
        goBack.setItemMeta(goBackMeta);

        Icon gobackButton = new Icon(goBack, (invsize - 1)).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openARMGui(player);
            }
        });

        inv.addIcon(gobackButton);

        player.openInventory(inv.getInventory());

    }

    public static void openMemberList(Player player, Region region){
        ArrayList<UUID> members = Main.getWorldGuardInterface().getMembers(region.getRegion());

        int invsize = 0;
        while (members.size() + 1 > invsize) {
            invsize = invsize + 9;
        }

        CustomHolder inv = new CustomHolder(invsize, Messages.GUI_MEMBER_LIST_MENU_NAME.replaceAll("%regionid%", region.getRegion().getId()));

        for(int i = 0; i < members.size(); i++) {
            ItemStack membersitem = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
            SkullMeta membersitemmeta = (SkullMeta) membersitem.getItemMeta();
            membersitemmeta.setOwner(Bukkit.getOfflinePlayer(members.get(i)).getName());
            membersitemmeta.setDisplayName(Bukkit.getOfflinePlayer(members.get(i)).getName());
            membersitem.setItemMeta(membersitemmeta);
            int finalI = i;
            Icon membersicon = new Icon(membersitem, i).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    Gui.openMemberManager(player, region, Bukkit.getOfflinePlayer(members.get(finalI)));
                }
            });
            inv.addIcon(membersicon);
        }

        if(members.size() == 0){
            ItemStack info = new ItemStack(Gui.INFO_ITEM);
            ItemMeta infoMeta = info.getItemMeta();
            infoMeta.setDisplayName(Messages.MEMBERLIST_INFO);
            infoMeta.setLore(Messages.MEMBERLIST_INFO_LORE);
            info.setItemMeta(infoMeta);
            Icon infoButton = new Icon(info, (0));
            inv.addIcon(infoButton);
        }

        ItemStack goBack = new ItemStack(Gui.GO_BACK_ITEM);
        ItemMeta goBackMeta = goBack.getItemMeta();
        goBackMeta.setDisplayName(Messages.GUI_GO_BACK);
        goBack.setItemMeta(goBackMeta);

        Icon gobackButton = new Icon(goBack, (invsize - 1)).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.decideRentSellOwnerManager(player, region);
            }
        });

        inv.addIcon(gobackButton);
        player.openInventory(inv.getInventory());

    }

    public static void openMemberManager(Player player, Region region, OfflinePlayer member) {
        CustomHolder inv = new CustomHolder(9, region.getRegion().getId() + " - " + member.getName());

        ItemStack makeOwnerItem = new ItemStack(Gui.PROMOTE_MEMBER_TO_OWNER_ITEM);
        ItemMeta makeOwnerItemMeta = makeOwnerItem.getItemMeta();
        makeOwnerItemMeta.setDisplayName(Messages.GUI_MAKE_OWNER_BUTTON);
        makeOwnerItemMeta.setLore(Messages.GUI_MAKE_OWNER_BUTTON_LORE);
        makeOwnerItem.setItemMeta(makeOwnerItemMeta);

        Icon makeOwnerMenu = new Icon(makeOwnerItem, 0).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openMakeOwnerWarning(player, region, member);
            }
        });
        inv.addIcon(makeOwnerMenu);

        ItemStack removeItem = new ItemStack(Gui.REMOVE_MEMBER_ITEM);
        ItemMeta removeItemMeta = removeItem.getItemMeta();
        removeItemMeta.setDisplayName(Messages.GUI_REMOVE_MEMBER_BUTTON);
        removeItemMeta.setLore(Messages.GUI_REMOVE_MEMBER_BUTTON_LORE);
        removeItem.setItemMeta(removeItemMeta);

        Icon removeMenu = new Icon(removeItem, 4).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Main.getWorldGuardInterface().removeMember(member.getUniqueId(), region.getRegion());
                player.sendMessage(Messages.PREFIX + Messages.REGION_REMOVE_MEMBER_REMOVED);
                player.closeInventory();
            }
        });
        inv.addIcon(removeMenu);

        ItemStack goBack = new ItemStack(Gui.GO_BACK_ITEM);
        ItemMeta goBackMeta = goBack.getItemMeta();
        goBackMeta.setDisplayName(Messages.GUI_GO_BACK);
        goBack.setItemMeta(goBackMeta);

        Icon gobackButton = new Icon(goBack, (8)).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openMemberList(player, region);
            }
        });

        inv.addIcon(gobackButton);

        player.openInventory(inv.getInventory());

    }

    public static void openMakeOwnerWarning(Player player, Region region, OfflinePlayer member) {
        CustomHolder inv = new CustomHolder(9, Messages.GUI_MAKE_OWNER_WARNING_NAME);

        ItemStack yesItem = new ItemStack(Gui.WARNING_YES_ITEM);
        ItemMeta yesItemMeta = yesItem.getItemMeta();
        yesItemMeta.setDisplayName(Messages.GUI_YES);
        yesItem.setItemMeta(yesItemMeta);

        Icon yesButton = new Icon(yesItem, 0).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                region.setNewOwner(member);
                player.sendMessage(Messages.PREFIX + "Transfer Complete");
                player.closeInventory();
            }
        });
        inv.addIcon(yesButton);

        ItemStack noItem = new ItemStack(Gui.WARNING_NO_ITEM);
        ItemMeta noItemMeta = noItem.getItemMeta();
        noItemMeta.setDisplayName(Messages.GUI_NO);
        noItem.setItemMeta(noItemMeta);

        Icon noButton = new Icon(noItem, 8).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openMemberList(player, region);
            }
        });
        inv.addIcon(noButton);

        player.openInventory(inv.getInventory());
    }

    public static void openRegionMemberGui(Player player) {
        List<Region> regions = Region.getRegionsByMember(player.getUniqueId());

        int invsize = 0;
        while (regions.size() + 1 > invsize) {
            invsize = invsize + 9;
        }

        CustomHolder inv = new CustomHolder(invsize, Messages.GUI_MEMBER_REGIONS_MENU_NAME);

        for (int i = 0; i < regions.size(); i++) {
            ItemStack stack = new ItemStack(regions.get(i).getLogo());
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(regions.get(i).getRegion().getId());
            stack.setItemMeta(meta);
            int finalI = i;
            Icon icon = new Icon(stack, i).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    Gui.openRegionManagerMember(player, regions.get(finalI));
                }
            });
            inv.addIcon(icon);
        }

        if(regions.size() == 0){
            ItemStack info = new ItemStack(Gui.INFO_ITEM);
            ItemMeta infoMeta = info.getItemMeta();
            infoMeta.setDisplayName(Messages.GUI_MEMBER_INFO_ITEM);
            infoMeta.setLore(Messages.GUI_MEMBER_INFO_LORE);
            info.setItemMeta(infoMeta);
            Icon infoButton = new Icon(info, (0));
            inv.addIcon(infoButton);
        }

        ItemStack goBack = new ItemStack(Gui.GO_BACK_ITEM);
        ItemMeta goBackMeta = goBack.getItemMeta();
        goBackMeta.setDisplayName(Messages.GUI_GO_BACK);
        goBack.setItemMeta(goBackMeta);

        Icon gobackButton = new Icon(goBack, (invsize - 1)).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openARMGui(player);
            }
        });

        inv.addIcon(gobackButton);

        player.openInventory(inv.getInventory());
    }

    public static void openRegionManagerMember(Player player, Region region) {

        int itemcounter = 1;
        int actitem = 1;

        if(player.hasPermission(Permission.MEMBER_TP)){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_INFO)){
            itemcounter++;
        }

        CustomHolder inv = new CustomHolder(9 , region.getRegion().getId());

        if(player.hasPermission(Permission.MEMBER_TP)){
            ItemStack teleporteritem = new ItemStack(Gui.TP_ITEM);
            ItemMeta teleporteritemmeta = teleporteritem.getItemMeta();
            teleporteritemmeta.setDisplayName(Messages.GUI_TELEPORT_TO_REGION_BUTTON);
            teleporteritemmeta.setLore(Messages.GUI_TELEPORT_TO_REGION_BUTTON_LORE);
            teleporteritem.setItemMeta(teleporteritemmeta);
            Icon teleportericon = new Icon(teleporteritem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    region.teleportToRegion(player);
                    player.closeInventory();
                }
            });
            inv.addIcon(teleportericon);

            actitem++;
        }

        if(player.hasPermission(Permission.MEMBER_INFO)){
            ItemStack infoitem = new ItemStack(Gui.INFO_ITEM);
            ItemMeta infoitemmeta = infoitem.getItemMeta();
            infoitemmeta.setDisplayName(Messages.GUI_SHOW_INFOS_BUTTON);
            infoitem.setItemMeta(infoitemmeta);
            Icon infoicon = new Icon(infoitem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    region.regionInfo(player);
                    player.closeInventory();
                }
            });
            inv.addIcon(infoicon);

            actitem++;
        }

        ItemStack gobackitem = new ItemStack(Gui.GO_BACK_ITEM);
        ItemMeta gobackitemmeta = gobackitem.getItemMeta();
        gobackitemmeta.setDisplayName(Messages.GUI_GO_BACK);
        gobackitem.setItemMeta(gobackitemmeta);
        Icon gobackicon = new Icon(gobackitem, getPosition(actitem, itemcounter)).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openRegionMemberGui(player);
            }
        });
        inv.addIcon(gobackicon);
        actitem++;

        player.openInventory(inv.getInventory());
    }

    public static void openOvertakeGUI(Player player, List<Region> oldRegions){

        int invsize = 0;
        while (oldRegions.size() + 1 > invsize) {
            invsize = invsize + 9;
        }

        CustomHolder inv = new CustomHolder(invsize, Messages.GUI_TAKEOVER_MENU_NAME);
        for(int i = 0; i < oldRegions.size(); i++) {
            ItemStack stack = new ItemStack(oldRegions.get(i).getLogo());
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(oldRegions.get(i).getRegion().getId());
            List<String> message = new LinkedList<>(Messages.GUI_TAKEOVER_ITEM_LORE);
            for (int j = 0; j < message.size(); j++) {
                message.set(j, message.get(j).replace("%days%", oldRegions.get(i).getRemainingDaysTillReset() + ""));
            }
            meta.setLore(message);
            stack.setItemMeta(meta);
            int finalI = i;
            Icon icon = new Icon(stack, i).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    oldRegions.get(finalI).setNewOwner(player);
                    oldRegions.remove(finalI);
                    player.sendMessage(Messages.PREFIX + Messages.REGION_TRANSFER_COMPLETE_MESSAGE);
                    Gui.openOvertakeGUI(player, oldRegions);
                    }
                });
            inv.addIcon(icon);
        }

        ItemStack stack = new ItemStack(Gui.GO_BACK_ITEM);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(Messages.GUI_CLOSE);
        stack.setItemMeta(meta);
        Icon icon = new Icon(stack, invsize - 1).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                player.closeInventory();
            }
        });
        inv.addIcon(icon);

        player.openInventory(inv.getInventory());
    }

    public static void openRegionResetWarning(Player player, Region region){
        CustomHolder inv = new CustomHolder(9, Messages.GUI_RESET_REGION_WARNING_NAME);

        ItemStack yesItem = new ItemStack(Gui.WARNING_YES_ITEM);
        ItemMeta yesItemMeta = yesItem.getItemMeta();
        yesItemMeta.setDisplayName(Messages.GUI_YES);
        yesItem.setItemMeta(yesItemMeta);

        Icon yesButton = new Icon(yesItem, 0).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                player.closeInventory();
                region.userReset(player);
            }
        });
        inv.addIcon(yesButton);

        ItemStack noItem = new ItemStack(Gui.WARNING_NO_ITEM);
        ItemMeta noItemMeta = noItem.getItemMeta();
        noItemMeta.setDisplayName(Messages.GUI_NO);
        noItem.setItemMeta(noItemMeta);

        Icon noButton = new Icon(noItem, 8).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.decideRentSellOwnerManager(player, region);
            }
        });
        inv.addIcon(noButton);

        player.openInventory(inv.getInventory());
    }

    public static void openSellWarning(Player player, Region region){
        CustomHolder inv = new CustomHolder(9, Messages.GUI_USER_SELL_WARNING);

        ItemStack yesItem = new ItemStack(Gui.WARNING_YES_ITEM);
        ItemMeta yesItemMeta = yesItem.getItemMeta();
        yesItemMeta.setDisplayName(Messages.GUI_YES);
        yesItem.setItemMeta(yesItemMeta);

        Icon yesButton = new Icon(yesItem, 0).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                player.closeInventory();
                region.userSell(player);
            }
        });
        inv.addIcon(yesButton);

        ItemStack noItem = new ItemStack(Gui.WARNING_NO_ITEM);
        ItemMeta noItemMeta = noItem.getItemMeta();
        noItemMeta.setDisplayName(Messages.GUI_NO);
        noItem.setItemMeta(noItemMeta);

        Icon noButton = new Icon(noItem, 8).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.decideRentSellOwnerManager(player, region);
            }
        });
        inv.addIcon(noButton);

        player.openInventory(inv.getInventory());
    }

    public static void decideRentSellOwnerManager(Player player, Region region) {
        if(region instanceof RentRegion) {
            Gui.openRentRegionManagerOwner(player, (RentRegion) region);
        } else {
            Gui.openRegionManagerOwner(player, region);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof CustomHolder) {
            event.setCancelled(true);

            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();

                ItemStack itemStack = event.getCurrentItem();
                if (itemStack == null || itemStack.getType() == Material.AIR) return;

                CustomHolder customHolder = (CustomHolder) event.getView().getTopInventory().getHolder();

                Icon icon = customHolder.getIcon(event.getRawSlot());
                if (icon == null) return;

                for (ClickAction clickAction : icon.getClickActions()) {
                    clickAction.execute(player);
                }
            }
        }
    }

    private static int getPosition(int itemNr, int maxItems){
        if(maxItems < itemNr || maxItems > 9){
            throw new IndexOutOfBoundsException("getPosition-Method cant handle more than max. 9 Items");
        }
        if(maxItems == 1){
            return 4;
        }
        else if(maxItems == 2) {
            if(itemNr == 1) {
                return 2;
            } else {
                return 6;
            }
        } else if(maxItems == 3) {
            if(itemNr == 1) {
                return 0;
            } else if(itemNr == 2){
                return 4;
            } else {
                return 8;
            }
        } else if(maxItems == 4) {
            if(itemNr == 1) {
                return 0;
            } else if(itemNr == 2){
                return 2;
            } else if(itemNr == 3){
                return 6;
            } else {
                return 8;
            }
        } else if(maxItems == 5) {
            if(itemNr == 1) {
                return 0;
            } else if(itemNr == 2){
                return 2;
            } else if(itemNr == 3){
                return 4;
            } else if(itemNr == 4){
                return 6;
            } else {
                return 8;
            }
        } else if(maxItems == 6) {
            if(itemNr == 1) {
                return 0;
            } else if(itemNr == 2){
                return 1;
            } else if(itemNr == 3){
                return 3;
            } else if(itemNr == 4){
                return 5;
            } else if(itemNr == 5){
                return 7;
            } else {
                return 8;
            }
        } else if(maxItems == 7) {
            if(itemNr == 1) {
                return 0;
            } else if(itemNr == 2){
                return 1;
            } else if(itemNr == 3){
                return 3;
            } else if(itemNr == 4){
                return 4;
            } else if(itemNr == 5){
                return 5;
            } else if(itemNr == 6){
                return 7;
            } else {
                return 8;
            }
        } else if(maxItems == 8) {
            if(itemNr == 1) {
                return 0;
            } else if(itemNr == 2){
                return 1;
            } else if(itemNr == 3){
                return 2;
            } else if(itemNr == 4){
                return 3;
            } else if(itemNr == 5){
                return 5;
            } else if(itemNr == 6){
                return 6;
            } else if(itemNr == 7){
                return 7;
            } else {
                return 8;
            }
        } else {
            if(itemNr == 1) {
                return 0;
            } else if(itemNr == 2){
                return 1;
            } else if(itemNr == 3){
                return 2;
            } else if(itemNr == 4){
                return 3;
            } else if(itemNr == 5){
                return 4;
            } else if(itemNr == 6){
                return 5;
            } else if(itemNr == 7){
                return 6;
            } else if(itemNr == 8){
                return 7;
            } else {
                return 8;
            }
        }
    }

    public static void setRegionOwnerItem(Material regionOwnerItem){
        if(regionOwnerItem != null) {
            Gui.REGION_OWNER_ITEM = regionOwnerItem;
        }
    }

    public static void setRegionMemberItem(Material regionMemberItem) {
        if(regionMemberItem != null) {
            Gui.REGION_MEMBER_ITEM = regionMemberItem;
        }
    }

    public static void setRegionFinderItem(Material regionFinderItem) {
        if(regionFinderItem != null) {
            Gui.REGION_FINDER_ITEM = regionFinderItem;
        }
    }

    public static void setGoBackItem(Material goBackItem) {
        if(goBackItem != null) {
            Gui.GO_BACK_ITEM = goBackItem;
        }
    }

    public static void setWarningYesItem(Material warningYesItem) {
        if(warningYesItem != null) {
            Gui.WARNING_YES_ITEM = warningYesItem;
        }
    }

    public static void setWarningNoItem(Material warningNoItem) {
        if(warningNoItem != null) {
            Gui.WARNING_NO_ITEM = warningNoItem;
        }
    }

    public static void setTpItem(Material tpItem) {
        if(tpItem != null) {
            Gui.TP_ITEM = tpItem;
        }
    }

    public static void setSellRegionItem(Material sellRegionItem) {
        if(sellRegionItem != null) {
            Gui.SELL_REGION_ITEM = sellRegionItem;
        }
    }

    public static void setResetItem(Material resetRegion) {
        if(resetRegion != null) {
            Gui.RESET_ITEM = resetRegion;
        }
    }

    public static void setExtendItem(Material extendItem) {
        if(extendItem != null) {
            Gui.EXTEND_ITEM = extendItem;
        }
    }

    public static void setInfoItem(Material infoItem) {
        if(infoItem != null) {
            Gui.INFO_ITEM = infoItem;
        }
    }

    public static void setPromoteMemberToOwnerItem(Material promoteMemberToOwnerItem) {
        if(promoteMemberToOwnerItem != null) {
            Gui.PROMOTE_MEMBER_TO_OWNER_ITEM = promoteMemberToOwnerItem;
        }
    }

    public static void setRemoveMemberItem(Material removeMemberItem) {
        if(removeMemberItem != null) {
            Gui.REMOVE_MEMBER_ITEM = removeMemberItem;
        }
    }
}
