package net.alex9849.arm.gui;

import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regions.*;
import net.alex9849.arm.Group.LimitGroup;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class Gui implements Listener {
    private static Material REGION_OWNER_ITEM = Material.ENDER_CHEST;
    private static Material REGION_MEMBER_ITEM = Material.CHEST;
    private static Material REGION_FINDER_ITEM = Material.COMPASS;
    private static Material GO_BACK_ITEM = Material.OAK_DOOR;
    private static Material WARNING_YES_ITEM = Material.MELON;
    private static Material WARNING_NO_ITEM = Material.REDSTONE_BLOCK;
    private static Material TP_ITEM = Material.ENDER_PEARL;
    private static Material SELL_REGION_ITEM = Material.DIAMOND;
    private static Material RESET_ITEM = Material.TNT;
    private static Material EXTEND_ITEM = Material.CLOCK;
    private static Material INFO_ITEM = Material.BOOK;
    private static Material PROMOTE_MEMBER_TO_OWNER_ITEM = Material.LADDER;
    private static Material REMOVE_MEMBER_ITEM = Material.LAVA_BUCKET;
    private static Material CONTRACT_ITEM = Material.WRITABLE_BOOK;
    private static Material FILL_ITEM = Material.GRAY_STAINED_GLASS_PANE;

    public static void openARMGui(Player player) {
        GuiInventory menu = new GuiInventory(9, Messages.GUI_MAIN_MENU_NAME);
        FileConfiguration config = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getConfig();

        int itemcounter = 0;
        int actitem = 1;

        if(config.getBoolean("GUI.DisplayRegionOwnerButton")){
            itemcounter++;
        }
        if(config.getBoolean("GUI.DisplayRegionMemberButton")){
            itemcounter++;
        }
        if(config.getBoolean("GUI.DisplayRegionFinderButton")){
            itemcounter++;
        }


        if(config.getBoolean("GUI.DisplayRegionOwnerButton")){
            ItemStack myRegions = new ItemStack(Gui.REGION_OWNER_ITEM);
            ItemMeta myRegionsMeta = myRegions.getItemMeta();
            myRegionsMeta.setDisplayName(Messages.GUI_MY_OWN_REGIONS);
            myRegions.setItemMeta(myRegionsMeta);

            ClickItem regionMenu = new ClickItem(myRegions).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    Gui.openRegionOwnerGui(player, true);
                }
            });
            menu.addIcon(regionMenu, getPosition(actitem, itemcounter));
            actitem++;
            if(itemcounter == 1) {
                Gui.openRegionOwnerGui(player, false);
            }
        }
        if(config.getBoolean("GUI.DisplayRegionMemberButton")){
            ItemStack mymRegions = new ItemStack(Gui.REGION_MEMBER_ITEM);
            ItemMeta mymRegionsMeta = mymRegions.getItemMeta();
            mymRegionsMeta.setDisplayName(Messages.GUI_MY_MEMBER_REGIONS);
            mymRegions.setItemMeta(mymRegionsMeta);

            ClickItem regionMemberMenu = new ClickItem(mymRegions).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    Gui.openRegionMemberGui(player, true);
                }
            });

            menu.addIcon(regionMemberMenu, getPosition(actitem, itemcounter));
            actitem++;
            if(itemcounter == 1) {
                Gui.openRegionMemberGui(player, false);
            }
        }
        if(config.getBoolean("GUI.DisplayRegionFinderButton")){
            ItemStack searchRegion = new ItemStack(Gui.REGION_FINDER_ITEM);
            ItemMeta searchRegionMeta = searchRegion.getItemMeta();
            searchRegionMeta.setDisplayName(Messages.GUI_SEARCH_FREE_REGION);
            searchRegion.setItemMeta(searchRegionMeta);

            ClickItem regionfinder = new ClickItem(searchRegion).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    Gui.openRegionFinder(player, true);
                }
            });

            menu.addIcon(regionfinder, getPosition(actitem, itemcounter));
            actitem++;
            if(itemcounter == 1) {
                Gui.openRegionFinder(player, false);
            }
        }

        menu = Gui.placeFillItems(menu);

        if(itemcounter != 1) {
            player.openInventory(menu.getInventory());
        }
    }

    public static void openRegionOwnerGui(Player player, Boolean withGoBack) {
        List<Region> regions = RegionManager.getRegionsByOwner(player.getUniqueId());
        List<ClickItem> clickItems = new ArrayList<>();

        for (int i = 0; i < regions.size(); i++) {
            ItemStack regionItem = Gui.getRegionDisplayItem(regions.get(i), Messages.GUI_RENT_REGION_LORE, new ArrayList<>(), Messages.GUI_CONTRACT_REGION_LORE);
            int finalI = i;
            ClickItem clickItem = new ClickItem(regionItem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    Gui.decideOwnerManager(player, regions.get(finalI));
                }
            });
            clickItems.add(clickItem);
        }

        ClickAction goBackAction = null;

        if(withGoBack) {
            goBackAction = new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    Gui.openARMGui(player);
                }
            };
        }

        Gui.openInfiniteGuiList(player, clickItems, 0, Messages.GUI_OWN_REGIONS_MENU_NAME, goBackAction);
    }

    public static void openSellRegionManagerOwner(Player player, Region region) {

        int itemcounter = 2;
        int actitem = 1;

        if(player.hasPermission(Permission.MEMBER_TP)){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_SELLBACK)){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_RESETREGIONBLOCKS) && region.isUserResettable()){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_INFO)){
            itemcounter++;
        }

        if(Permission.hasAnySubregionPermission(player) && region.isTown()){
            itemcounter++;
        }

        GuiInventory inv = new GuiInventory(9 , region.getRegion().getId());

        ItemStack membersitem = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta membersitemmeta = (SkullMeta) membersitem.getItemMeta();
        membersitemmeta.setOwner(player.getDisplayName());
        membersitemmeta.setDisplayName(Messages.GUI_MEMBERS_BUTTON);
        membersitem.setItemMeta(membersitemmeta);
        ClickItem membersicon = new ClickItem(membersitem).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openMemberList(player, region);
            }
        });
        inv.addIcon(membersicon, getPosition(actitem, itemcounter));

        actitem++;

        if(Permission.hasAnySubregionPermission(player) && region.isTown()){
            //TODO make subregion Item changeable
            ItemStack subregionitem = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta subregionitemmeta = subregionitem.getItemMeta();
            subregionitemmeta.setDisplayName("Subregions");
            //TODO make lore changeable
            subregionitemmeta.setLore(new ArrayList<>());
            subregionitem.setItemMeta(subregionitemmeta);
            ClickItem teleportericon = new ClickItem(subregionitem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    List<ClickItem> clickItems = new ArrayList<>();
                    for(Region subregion : region.getSubregions()) {
                        ItemStack subregionItem = Gui.getRegionDisplayItem(subregion, Messages.GUI_RENT_REGION_LORE, new ArrayList<>(), Messages.GUI_CONTRACT_REGION_LORE);
                        ClickItem subregionClickItem = new ClickItem(subregionItem);
                        clickItems.add(subregionClickItem);
                    }
                    Gui.openInfiniteGuiList(player, clickItems, 0, "Subregions", new ClickAction() {
                        @Override
                        public void execute(Player player) throws InputException {
                            Gui.decideOwnerManager(player, region);
                        }
                    });
                }
            });
            inv.addIcon(teleportericon, getPosition(actitem, itemcounter));
            actitem++;
        }

        if(player.hasPermission(Permission.MEMBER_TP)){
            ItemStack teleporteritem = new ItemStack(Gui.TP_ITEM);
            ItemMeta teleporteritemmeta = teleporteritem.getItemMeta();
            teleporteritemmeta.setDisplayName(Messages.GUI_TELEPORT_TO_REGION_BUTTON);
            teleporteritemmeta.setLore(Messages.GUI_TELEPORT_TO_REGION_BUTTON_LORE);
            teleporteritem.setItemMeta(teleporteritemmeta);
            ClickItem teleportericon = new ClickItem(teleporteritem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    Teleporter.teleport(player, region);
                    player.closeInventory();
                }
            });
            inv.addIcon(teleportericon, getPosition(actitem, itemcounter));

            actitem++;
        }

        if(player.hasPermission(Permission.MEMBER_RESETREGIONBLOCKS) && region.isUserResettable()) {
            ItemStack resetItem = new ItemStack(Gui.RESET_ITEM);
            ItemMeta resetitemItemMeta = resetItem.getItemMeta();
            resetitemItemMeta.setDisplayName(Messages.GUI_RESET_REGION_BUTTON);
            List<String> message = new LinkedList<>(Messages.GUI_RESET_REGION_BUTTON_LORE);
            for (int i = 0; i < message.size(); i++) {
                message.set(i, message.get(i).replace("%days%", Region.getResetCooldown() + ""));
            }
            resetitemItemMeta.setLore(message);
            resetItem.setItemMeta(resetitemItemMeta);
            ClickItem reseticon = new ClickItem(resetItem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    if(region.timeSinceLastReset() >= Region.getResetCooldown()){
                        Gui.openRegionResetWarning(player, region, true);
                    } else {
                        String message = region.getConvertedMessage(Messages.RESET_REGION_COOLDOWN_ERROR);
                        throw new InputException(player, message);
                    }
                }
            });
            inv.addIcon(reseticon, getPosition(actitem, itemcounter));

            actitem++;
        }

        if(player.hasPermission(Permission.MEMBER_SELLBACK)) {
            ItemStack resetItem = new ItemStack(Gui.SELL_REGION_ITEM);
            ItemMeta resetitemItemMeta = resetItem.getItemMeta();
            resetitemItemMeta.setDisplayName(Messages.GUI_USER_SELL_BUTTON);
            List<String> message = new LinkedList<>(Messages.GUI_USER_SELL_BUTTON_LORE);
            for (int i = 0; i < message.size(); i++) {
                message.set(i, region.getConvertedMessage(message.get(i)));
            }
            resetitemItemMeta.setLore(message);
            resetItem.setItemMeta(resetitemItemMeta);
            ClickItem reseticon = new ClickItem(resetItem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    Gui.openSellWarning(player, region, true);
                }
            });
            inv.addIcon(reseticon, getPosition(actitem, itemcounter));

            actitem++;
        }

        if(player.hasPermission(Permission.MEMBER_INFO)) {
            ItemStack infoitem = new ItemStack(Gui.INFO_ITEM);
            ItemMeta infoitemmeta = infoitem.getItemMeta();
            infoitemmeta.setDisplayName(Messages.GUI_SHOW_INFOS_BUTTON);
            infoitem.setItemMeta(infoitemmeta);
            ClickItem infoicon = new ClickItem(infoitem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    region.regionInfo(player);
                    player.closeInventory();
                }
            });
            inv.addIcon(infoicon, getPosition(actitem, itemcounter));

            actitem++;
        }

        ItemStack gobackitem = new ItemStack(Gui.GO_BACK_ITEM);
        ItemMeta gobackitemmeta = gobackitem.getItemMeta();
        gobackitemmeta.setDisplayName(Messages.GUI_GO_BACK);
        gobackitem.setItemMeta(gobackitemmeta);
        ClickItem gobackicon = new ClickItem(gobackitem).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openRegionOwnerGui(player, isMainPageMultipleItems());
            }
        });
        inv.addIcon(gobackicon, getPosition(actitem, itemcounter));

        actitem++;

        inv = Gui.placeFillItems(inv);

        player.openInventory(inv.getInventory());


    }

    public static void openRentRegionManagerOwner(Player player, RentRegion region) {

        int itemcounter = 3;
        int actitem = 1;

        if(player.hasPermission(Permission.MEMBER_TP)){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_SELLBACK)){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_RESETREGIONBLOCKS) && region.isUserResettable()){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_INFO)){
            itemcounter++;
        }

        GuiInventory inv = new GuiInventory(9 , region.getRegion().getId());


        ItemStack membersitem = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta membersitemmeta = (SkullMeta) membersitem.getItemMeta();
        membersitemmeta.setOwner(player.getDisplayName());
        membersitemmeta.setDisplayName(Messages.GUI_MEMBERS_BUTTON);
        membersitem.setItemMeta(membersitemmeta);
        ClickItem membersicon = new ClickItem(membersitem).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openMemberList(player, region);
            }
        });
        inv.addIcon(membersicon, getPosition(actitem, itemcounter));

        actitem++;

        if(player.hasPermission(Permission.MEMBER_TP)) {
            ItemStack teleporteritem = new ItemStack(Gui.TP_ITEM);
            ItemMeta teleporteritemmeta = teleporteritem.getItemMeta();
            teleporteritemmeta.setDisplayName(Messages.GUI_TELEPORT_TO_REGION_BUTTON);
            teleporteritemmeta.setLore(Messages.GUI_TELEPORT_TO_REGION_BUTTON_LORE);
            teleporteritem.setItemMeta(teleporteritemmeta);
            ClickItem teleportericon = new ClickItem(teleporteritem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    Teleporter.teleport(player, region);
                    player.closeInventory();
                }
            });
            inv.addIcon(teleportericon, getPosition(actitem, itemcounter));

            actitem++;
        }


        if(player.hasPermission(Permission.MEMBER_RESETREGIONBLOCKS) && region.isUserResettable()) {

            ItemStack resetItem = new ItemStack(Gui.RESET_ITEM);
            ItemMeta resetitemItemMeta = resetItem.getItemMeta();
            resetitemItemMeta.setDisplayName(Messages.GUI_RESET_REGION_BUTTON);
            List<String> resetmessage = new LinkedList<>(Messages.GUI_RESET_REGION_BUTTON_LORE);
            for (int i = 0; i < resetmessage.size(); i++) {
                resetmessage.set(i, resetmessage.get(i).replace("%days%", Region.getResetCooldown() + ""));
            }
            resetitemItemMeta.setLore(resetmessage);
            resetItem.setItemMeta(resetitemItemMeta);
            ClickItem reseticon = new ClickItem(resetItem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    if(region.timeSinceLastReset() >= Region.getResetCooldown()){
                        Gui.openRegionResetWarning(player, region, true);
                    } else {
                        String message = region.getConvertedMessage(Messages.RESET_REGION_COOLDOWN_ERROR);
                        throw new InputException(player, message);
                    }
                }
            });
            inv.addIcon(reseticon, getPosition(actitem, itemcounter));

            actitem++;
        }

        if(player.hasPermission(Permission.MEMBER_SELLBACK)) {
            ItemStack resetItem = new ItemStack(Gui.SELL_REGION_ITEM);
            ItemMeta resetitemItemMeta = resetItem.getItemMeta();
            resetitemItemMeta.setDisplayName(Messages.GUI_USER_SELL_BUTTON);
            List<String> message = new LinkedList<>(Messages.GUI_USER_SELL_BUTTON_LORE);
            for (int i = 0; i < message.size(); i++) {
                message.set(i, region.getConvertedMessage(message.get(i)));
            }
            resetitemItemMeta.setLore(message);
            resetItem.setItemMeta(resetitemItemMeta);
            ClickItem reseticon = new ClickItem(resetItem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    Gui.openSellWarning(player, region, true);
                }
            });
            inv.addIcon(reseticon, getPosition(actitem, itemcounter));

            actitem++;
        }

        ItemStack extendItem = new ItemStack(Gui.EXTEND_ITEM);
        ItemMeta extendItemMeta = extendItem.getItemMeta();
        extendItemMeta.setDisplayName(Messages.GUI_EXTEND_BUTTON);
        List<String> extendmessage = new LinkedList<>(Messages.GUI_EXTEND_BUTTON_LORE);
        for (int i = 0; i < extendmessage.size(); i++) {
            extendmessage.set(i, region.getConvertedMessage(extendmessage.get(i)));
        }
        extendItemMeta.setLore(extendmessage);
        extendItem.setItemMeta(extendItemMeta);
        ClickItem extendicon = new ClickItem(extendItem).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) throws InputException {
                region.buy(player);
                Gui.decideOwnerManager(player, region);
            }
        });
        inv.addIcon(extendicon, getPosition(actitem, itemcounter));

        actitem++;

        if(player.hasPermission(Permission.MEMBER_INFO)){
            ItemStack infoitem = new ItemStack(Gui.INFO_ITEM);
            ItemMeta infoitemmeta = infoitem.getItemMeta();
            infoitemmeta.setDisplayName(Messages.GUI_SHOW_INFOS_BUTTON);
            infoitem.setItemMeta(infoitemmeta);
            ClickItem infoicon = new ClickItem(infoitem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    region.regionInfo(player);
                    player.closeInventory();
                }
            });
            inv.addIcon(infoicon, getPosition(actitem, itemcounter));

            actitem++;
        }

        ItemStack gobackitem = new ItemStack(Gui.GO_BACK_ITEM);
        ItemMeta gobackitemmeta = gobackitem.getItemMeta();
        gobackitemmeta.setDisplayName(Messages.GUI_GO_BACK);
        gobackitem.setItemMeta(gobackitemmeta);
        ClickItem gobackicon = new ClickItem(gobackitem).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openRegionOwnerGui(player, isMainPageMultipleItems());
            }
        });
        inv.addIcon(gobackicon, getPosition(actitem, itemcounter));
        actitem++;

        inv = Gui.placeFillItems(inv);

        player.openInventory(inv.getInventory());
    }

    public static void openContractRegionManagerOwner(Player player, ContractRegion region) {

        int itemcounter = 3;
        int actitem = 1;

        if(player.hasPermission(Permission.MEMBER_TP)){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_SELLBACK)){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_RESETREGIONBLOCKS) && region.isUserResettable()){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_INFO)){
            itemcounter++;
        }

        GuiInventory inv = new GuiInventory(9 , region.getRegion().getId());


        ItemStack membersitem = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta membersitemmeta = (SkullMeta) membersitem.getItemMeta();
        membersitemmeta.setOwner(player.getDisplayName());
        membersitemmeta.setDisplayName(Messages.GUI_MEMBERS_BUTTON);
        membersitem.setItemMeta(membersitemmeta);
        ClickItem membersicon = new ClickItem(membersitem).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openMemberList(player, region);
            }
        });
        inv.addIcon(membersicon, getPosition(actitem, itemcounter));

        actitem++;

        if(player.hasPermission(Permission.MEMBER_TP)) {
            ItemStack teleporteritem = new ItemStack(Gui.TP_ITEM);
            ItemMeta teleporteritemmeta = teleporteritem.getItemMeta();
            teleporteritemmeta.setDisplayName(Messages.GUI_TELEPORT_TO_REGION_BUTTON);
            teleporteritemmeta.setLore(Messages.GUI_TELEPORT_TO_REGION_BUTTON_LORE);
            teleporteritem.setItemMeta(teleporteritemmeta);
            ClickItem teleportericon = new ClickItem(teleporteritem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    Teleporter.teleport(player, region);
                    player.closeInventory();
                }
            });
            inv.addIcon(teleportericon, getPosition(actitem, itemcounter));

            actitem++;
        }


        if(player.hasPermission(Permission.MEMBER_RESETREGIONBLOCKS) && region.isUserResettable()) {

            ItemStack resetItem = new ItemStack(Gui.RESET_ITEM);
            ItemMeta resetitemItemMeta = resetItem.getItemMeta();
            resetitemItemMeta.setDisplayName(Messages.GUI_RESET_REGION_BUTTON);
            List<String> resetmessage = new LinkedList<>(Messages.GUI_RESET_REGION_BUTTON_LORE);
            for (int i = 0; i < resetmessage.size(); i++) {
                resetmessage.set(i, resetmessage.get(i).replace("%days%", Region.getResetCooldown() + ""));
            }
            resetitemItemMeta.setLore(resetmessage);
            resetItem.setItemMeta(resetitemItemMeta);
            ClickItem reseticon = new ClickItem(resetItem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    if(region.timeSinceLastReset() >= Region.getResetCooldown()){
                        Gui.openRegionResetWarning(player, region, true);
                    } else {
                        String message = region.getConvertedMessage(Messages.RESET_REGION_COOLDOWN_ERROR);
                        throw new InputException(player, message);
                    }
                }
            });
            inv.addIcon(reseticon, getPosition(actitem, itemcounter));

            actitem++;
        }

        if(player.hasPermission(Permission.MEMBER_SELLBACK)) {
            ItemStack resetItem = new ItemStack(Gui.SELL_REGION_ITEM);
            ItemMeta resetitemItemMeta = resetItem.getItemMeta();
            resetitemItemMeta.setDisplayName(Messages.GUI_USER_SELL_BUTTON);
            List<String> message = new LinkedList<>(Messages.GUI_USER_SELL_BUTTON_LORE);
            for (int i = 0; i < message.size(); i++) {
                message.set(i, region.getConvertedMessage(message.get(i)));
            }
            resetitemItemMeta.setLore(message);
            resetItem.setItemMeta(resetitemItemMeta);
            ClickItem reseticon = new ClickItem(resetItem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    Gui.openSellWarning(player, region, true);
                }
            });
            inv.addIcon(reseticon, getPosition(actitem, itemcounter));

            actitem++;
        }
        ItemStack extendItem = new ItemStack(Gui.CONTRACT_ITEM);
        ItemMeta extendItemMeta = extendItem.getItemMeta();
        extendItemMeta.setDisplayName(Messages.GUI_CONTRACT_ITEM);
        List<String> extendmessage = new LinkedList<>(Messages.GUI_CONTRACT_ITEM_LORE);
        for (int i = 0; i < extendmessage.size(); i++) {
            extendmessage.set(i, region.getConvertedMessage(extendmessage.get(i)));
        }
        extendItemMeta.setLore(extendmessage);
        extendItem.setItemMeta(extendItemMeta);
        ClickItem extendicon = new ClickItem(extendItem).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                region.changeTerminated(player);
                Gui.decideOwnerManager(player, region);
            }
        });
        inv.addIcon(extendicon, getPosition(actitem, itemcounter));

        actitem++;

        if(player.hasPermission(Permission.MEMBER_INFO)){
            ItemStack infoitem = new ItemStack(Gui.INFO_ITEM);
            ItemMeta infoitemmeta = infoitem.getItemMeta();
            infoitemmeta.setDisplayName(Messages.GUI_SHOW_INFOS_BUTTON);
            infoitem.setItemMeta(infoitemmeta);
            ClickItem infoicon = new ClickItem(infoitem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    region.regionInfo(player);
                    player.closeInventory();
                }
            });
            inv.addIcon(infoicon, getPosition(actitem, itemcounter));

            actitem++;
        }

        ItemStack gobackitem = new ItemStack(Gui.GO_BACK_ITEM);
        ItemMeta gobackitemmeta = gobackitem.getItemMeta();
        gobackitemmeta.setDisplayName(Messages.GUI_GO_BACK);
        gobackitem.setItemMeta(gobackitemmeta);
        ClickItem gobackicon = new ClickItem(gobackitem).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openRegionOwnerGui(player, isMainPageMultipleItems());
            }
        });
        inv.addIcon(gobackicon, getPosition(actitem, itemcounter));
        actitem++;

        inv = Gui.placeFillItems(inv);

        player.openInventory(inv.getInventory());

    }

    public static void openSubRegionManger(Player player, Region region) {

    }

    public static void openRegionFinder(Player player, Boolean withGoBack) {

        int itemcounter = 0;
        int actitem = 1;
        if(withGoBack) {
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_LIMIT)){
            itemcounter++;
        }

        if(RegionKind.DEFAULT.isDisplayInGUI()){
            itemcounter++;
        }

        if(RegionKind.SUBREGION.isDisplayInGUI()){
            itemcounter++;
        }

        for(RegionKind regionKind : RegionKind.getRegionKindList()) {
            if(regionKind.isDisplayInGUI()) {
                itemcounter++;
            }
        }

        int invsize = 0;

        while (itemcounter > invsize) {
            invsize = invsize + 9;
        }

        GuiInventory inv = new GuiInventory(invsize, Messages.GUI_REGION_FINDER_MENU_NAME);
        int itempos = 0;
        if(RegionKind.DEFAULT.isDisplayInGUI()) {
            String displayName = Messages.GUI_REGIONFINDER_REGIONKIND_NAME;
            displayName = displayName.replace("%regionkind%", RegionKind.DEFAULT.getName());
            Material material = RegionKind.DEFAULT.getMaterial();
            ItemStack stack = new ItemStack(material);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(displayName);
            meta.setLore(RegionKind.DEFAULT.getLore());
            stack.setItemMeta(meta);
            ClickItem icon = new ClickItem(stack).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    Gui.openRegionFinderSellTypeSelector(player, RegionManager.getFreeRegions(RegionKind.DEFAULT), new ClickAction() {
                        @Override
                        public void execute(Player player) throws InputException {
                            Gui.openRegionFinder(player, withGoBack);
                        }
                    });
                }
            });
            inv.addIcon(icon, 0);
            itempos++;
        }

        if(RegionKind.SUBREGION.isDisplayInGUI()){
            String displayName = Messages.GUI_REGIONFINDER_REGIONKIND_NAME;
            displayName = displayName.replace("%regionkind%", RegionKind.SUBREGION.getName());
            Material material = RegionKind.SUBREGION.getMaterial();
            ItemStack stack = new ItemStack(material);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(displayName);
            meta.setLore(RegionKind.SUBREGION.getLore());
            stack.setItemMeta(meta);
            ClickItem icon = new ClickItem(stack).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    Gui.openRegionFinderSellTypeSelector(player, RegionManager.getFreeRegions(RegionKind.SUBREGION), new ClickAction() {
                        @Override
                        public void execute(Player player) throws InputException {
                            Gui.openRegionFinder(player, withGoBack);
                        }
                    });
                }
            });
            inv.addIcon(icon, 1);
            itempos++;
        }


        for(int i = 0; i < RegionKind.getRegionKindList().size(); i++) {
            if(RegionKind.getRegionKindList().get(i).isDisplayInGUI()) {
                String displayName = Messages.GUI_REGIONFINDER_REGIONKIND_NAME;
                displayName = displayName.replace("%regionkind%", RegionKind.getRegionKindList().get(i).getDisplayName());
                Material material = RegionKind.getRegionKindList().get(i).getMaterial();
                if(player.hasPermission(Permission.ARM_BUYKIND + RegionKind.getRegionKindList().get(i).getName())){
                    ItemStack stack = new ItemStack(material);
                    ItemMeta meta = stack.getItemMeta();
                    meta.setDisplayName(displayName);
                    meta.setLore(RegionKind.getRegionKindList().get(i).getLore());
                    stack.setItemMeta(meta);
                    int finalI = i;
                    ClickItem icon = new ClickItem(stack).addClickAction(new ClickAction() {
                        @Override
                        public void execute(Player player) throws InputException {
                            Gui.openRegionFinderSellTypeSelector(player, RegionManager.getFreeRegions(RegionKind.getRegionKindList().get(finalI)), new ClickAction() {
                                @Override
                                public void execute(Player player) throws InputException {
                                    Gui.openRegionFinder(player, withGoBack);
                                }
                            });
                        }
                    });
                    inv.addIcon(icon, itempos);
                    itempos++;
                }
            }

        }

        if(player.hasPermission(Permission.MEMBER_LIMIT)){
            ItemStack goBack = new ItemStack(Gui.INFO_ITEM);
            ItemMeta goBackMeta = goBack.getItemMeta();
            goBackMeta.setDisplayName(Messages.GUI_MY_LIMITS_BUTTON);
            goBack.setItemMeta(goBackMeta);

            ClickItem gobackButton = new ClickItem(goBack).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    LimitGroup.getLimitChat(player);
                }
            });

            inv.addIcon(gobackButton, (invsize - 2));
        }


        if(withGoBack) {
            ItemStack goBack = new ItemStack(Gui.GO_BACK_ITEM);
            ItemMeta goBackMeta = goBack.getItemMeta();
            goBackMeta.setDisplayName(Messages.GUI_GO_BACK);
            goBack.setItemMeta(goBackMeta);

            ClickItem gobackButton = new ClickItem(goBack).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    Gui.openARMGui(player);
                }
            });

            inv.addIcon(gobackButton, (invsize - 1));
        }

        inv = Gui.placeFillItems(inv);

        player.openInventory(inv.getInventory());

    }

    public static void openRegionFinderSellTypeSelector(Player player, List<Region> regions, ClickAction goBackAction) throws InputException {
        //TODO change name
        GuiInventory inv = new GuiInventory(9, Messages.GUI_REGION_FINDER_MENU_NAME);
        List<ClickItem> sellRegionClickItems = new ArrayList<>();
        List<ClickItem> rentRegionClickItems = new ArrayList<>();
        List<ClickItem> contractRegionClickItems = new ArrayList<>();
        ClickItem sellRegionList = null;
        ClickItem rentRegionList = null;
        ClickItem contractRegionList = null;




        for(Region region : regions) {
            //Todo add to config
            List<String> newRentregionLore = new ArrayList<String>(Arrays.asList("%regionid%", "Price: %price%", "Extend per click: %extendperclick%","Max. extended time: %maxrenttime%", "Dimensions: %dimensions%", "World: %world%"));
            List<String> newSellRegionLore = new ArrayList<String>(Arrays.asList(region.getRegion().getId(), "Price: %price%", "Dimensions: %dimensions%", "World: %world%"));
            List<String> newContractRegionLore = new ArrayList<String>(Arrays.asList(region.getRegion().getId(), "Price: %price%", "Automatic extend time: %extend%","Dimensions: %dimensions%", "World: %world%"));

            ItemStack itemStack = getRegionDisplayItem(region, newRentregionLore, newSellRegionLore, newContractRegionLore);
            ClickItem clickItem = new ClickItem(itemStack).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    Gui.openRegionFinderTeleportLocationSeceltor(player, region);
                }
            });
            if(region instanceof SellRegion) {
                sellRegionClickItems.add(clickItem);
            } else if(region instanceof RentRegion) {
                rentRegionClickItems.add(clickItem);
            } else if(region instanceof ContractRegion) {
                contractRegionClickItems.add(clickItem);
            }
        }
        //TODO items anordnen
        if(sellRegionClickItems.size() > 0) {
            ItemStack sellRegionItem = new ItemStack(Material.BRICKS);
            //TODO Change name
            ItemMeta sellRegionItemMeta = sellRegionItem.getItemMeta();
            sellRegionItemMeta.setDisplayName("SellRegion");
            sellRegionItem.setItemMeta(sellRegionItemMeta);
            ClickItem clickItem = new ClickItem(sellRegionItem);
            clickItem.addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    Gui.openInfiniteGuiList(player, sellRegionClickItems, 0, Messages.GUI_REGION_FINDER_MENU_NAME, goBackAction);
                }
            });
            if((rentRegionClickItems.size() == 0) && (contractRegionClickItems.size() == 0)) {
                Gui.openInfiniteGuiList(player, sellRegionClickItems, 0, Messages.GUI_REGION_FINDER_MENU_NAME, goBackAction);
                return;
            }
            //TODO Make position dynamic
            inv.addIcon(clickItem, 0);
        }
        if(rentRegionClickItems.size() > 0) {
            ItemStack sellRegionItem = new ItemStack(Material.BRICKS);
            //TODO Change name
            ItemMeta sellRegionItemMeta = sellRegionItem.getItemMeta();
            sellRegionItemMeta.setDisplayName("RentRegion");
            sellRegionItem.setItemMeta(sellRegionItemMeta);
            ClickItem clickItem = new ClickItem(sellRegionItem);
            clickItem.addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    Gui.openInfiniteGuiList(player, rentRegionClickItems, 0, Messages.GUI_REGION_FINDER_MENU_NAME, goBackAction);
                }
            });
            if((sellRegionClickItems.size() == 0) && (contractRegionClickItems.size() == 0)) {
                Gui.openInfiniteGuiList(player, rentRegionClickItems, 0, Messages.GUI_REGION_FINDER_MENU_NAME, goBackAction);
                return;
            }
            inv.addIcon(clickItem, 3);
        }
        if(contractRegionClickItems.size() > 0) {
            ItemStack sellRegionItem = new ItemStack(Material.BRICKS);
            //TODO Change name
            ItemMeta sellRegionItemMeta = sellRegionItem.getItemMeta();
            sellRegionItemMeta.setDisplayName("ContractRegion");
            sellRegionItem.setItemMeta(sellRegionItemMeta);
            ClickItem clickItem = new ClickItem(sellRegionItem);
            clickItem.addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    Gui.openInfiniteGuiList(player, contractRegionClickItems, 0, Messages.GUI_REGION_FINDER_MENU_NAME, goBackAction);
                }
            });
            if((sellRegionClickItems.size() == 0) && (rentRegionClickItems.size() == 0)) {
                Gui.openInfiniteGuiList(player, contractRegionClickItems, 0, Messages.GUI_REGION_FINDER_MENU_NAME, goBackAction);
                return;
            }
            inv.addIcon(clickItem, 3);
        }
        if((sellRegionClickItems.size() == 0) && (rentRegionClickItems.size() == 0) && (contractRegionClickItems.size() == 0)) {
            throw new InputException(player, Messages.NO_FREE_REGION_WITH_THIS_KIND);
        }
        inv = Gui.placeFillItems(inv);
        player.openInventory(inv.getInventory());
    }

    public static void openRegionFinderTeleportLocationSeceltor(Player player, Region region) throws InputException {
        if(!ArmSettings.isAllowTeleportToBuySign()) {
            region.teleport(player, false);
            return;
        }

        //TODO make items and Names changeable
        GuiInventory inv = new GuiInventory(9, "Teleport to sign or region?");
        ItemStack signIcon = new ItemStack(Material.SIGN);
        ItemMeta signIconMeta = signIcon.getItemMeta();
        signIconMeta.setDisplayName("Teleport to buy sign!");
        signIcon.setItemMeta(signIconMeta);
        ClickItem clickSign = new ClickItem(signIcon);
        clickSign.addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) throws InputException {
                region.teleport(player, true);
                player.closeInventory();
            }
        });
        inv.addIcon(clickSign, getPosition(1, 2));

        ItemStack regionIcon = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta regionMeta = regionIcon.getItemMeta();
        regionMeta.setDisplayName("Teleport to region!");
        regionIcon.setItemMeta(regionMeta);
        ClickItem clickRegion = new ClickItem(regionIcon);
        clickRegion.addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) throws InputException {
                region.teleport(player, false);
                player.closeInventory();
            }
        });
        inv.addIcon(clickRegion, getPosition(2, 2));
        inv = Gui.placeFillItems(inv);
        player.openInventory(inv.getInventory());
    }

    private static ItemStack getRegionDisplayItem(Region region, List<String> rentLore, List<String> sellLore, List<String> contractLore) {
        String regionDisplayName = Messages.GUI_REGION_ITEM_NAME;
        regionDisplayName = region.getConvertedMessage(regionDisplayName);
        regionDisplayName = regionDisplayName.replace("%regionkind%", region.getRegionKind().getName());

        ItemStack stack = new ItemStack(region.getRegionKind().getMaterial());
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(regionDisplayName);

        List<String> regionLore = new ArrayList<>();

        if(region instanceof RentRegion) {
            regionLore = new ArrayList<>(rentLore);
        } else if (region instanceof SellRegion) {
            regionLore = new ArrayList<>(sellLore);
        } else if (region instanceof ContractRegion) {
            regionLore = new ArrayList<>(contractLore);
        }

        for (int j = 0; j < regionLore.size(); j++) {
            regionLore.set(j, region.getConvertedMessage(regionLore.get(j)));
        }
        meta.setLore(regionLore);
        stack.setItemMeta(meta);
        return stack;
    }

    public static void openInfiniteGuiList(Player player, List<ClickItem> clickItems, int startitem, String name, ClickAction gobackAction) {

        int invsize = 0;
        int itemsize = 0;

        while (((clickItems.size() - startitem) > itemsize) && (itemsize < 45)) {
            itemsize = itemsize + 9;
        }
        invsize = itemsize;
        if(((gobackAction != null) && (clickItems.size() >= 9)) || ((startitem + 45) < (clickItems.size() - 1)) || (startitem != 0) || (itemsize == 0)) {
            invsize = itemsize + 9;
        }

        GuiInventory inv = new GuiInventory(invsize, name);

        int pos = 0;
        for(int i = startitem; ((i < startitem + itemsize) && (i < clickItems.size())); i++) {
            inv.addIcon(clickItems.get(i), pos);
            pos++;
        }
        if(startitem != 0) {
            int newStartItem = startitem - 45;
            if(newStartItem < 0) {
                newStartItem = 0;
            }
            //TODO Make item and text changeable
            final int finalnewStartItem = newStartItem;
            ClickItem prevPageButton = new ClickItem(new ItemStack(Material.ARROW), "prev Page").addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    Gui.openInfiniteGuiList(player, clickItems, finalnewStartItem, name, gobackAction);
                }
            });
            inv.addIcon(prevPageButton, invsize - 9);
        }
        if((startitem + 45) < (clickItems.size() - 1)) {
            //TODO Make item and text changeable
            int newStartItem = startitem + 45;
            ClickItem nextPageButton = new ClickItem(new ItemStack(Material.ARROW), "next Page").addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    Gui.openInfiniteGuiList(player, clickItems, newStartItem, name, gobackAction);
                }
            });
            inv.addIcon(nextPageButton, invsize - 1);
        }
        if(gobackAction != null) {
            ClickItem gobackButton = new ClickItem(new ItemStack(Gui.GO_BACK_ITEM), Messages.GUI_GO_BACK).addClickAction(gobackAction);
            if(clickItems.size() >= 9) {
                inv.addIcon(gobackButton, invsize - 5);
            } else {
                inv.addIcon(gobackButton, 8);
            }

        }

        inv = Gui.placeFillItems(inv);
        player.openInventory(inv.getInventory());
    }

    public static void openMemberList(Player player, Region region){
        ArrayList<UUID> members = region.getRegion().getMembers();

        List<ClickItem> clickItems = new ArrayList<>();

        String invname =  Messages.GUI_MEMBER_LIST_MENU_NAME.replaceAll("%regionid%", region.getRegion().getId());

        for(int i = 0; i < members.size(); i++) {
            ItemStack membersitem = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
            SkullMeta membersitemmeta = (SkullMeta) membersitem.getItemMeta();
            if(Bukkit.getOfflinePlayer(members.get(i)).getName() != null) {
                membersitemmeta.setOwner(Bukkit.getOfflinePlayer(members.get(i)).getName());
                membersitemmeta.setDisplayName(Bukkit.getOfflinePlayer(members.get(i)).getName());
            }
            membersitem.setItemMeta(membersitemmeta);
            int finalI = i;
            ClickItem membersicon = new ClickItem(membersitem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    Gui.openMemberManager(player, region, Bukkit.getOfflinePlayer(members.get(finalI)));
                }
            });
            clickItems.add(membersicon);
        }

        if(members.size() == 0){
            ItemStack info = new ItemStack(Gui.INFO_ITEM);
            ItemMeta infoMeta = info.getItemMeta();
            infoMeta.setDisplayName(Messages.GUI_OWNER_MEMBER_INFO_ITEM);
            List<String> lore = new ArrayList<>(Messages.GUI_OWNER_MEMBER_INFO_LORE);
            for(int i = 0; i < lore.size(); i++) {
                lore.set(i, region.getConvertedMessage(lore.get(i)));
            }
            infoMeta.setLore(lore);
            info.setItemMeta(infoMeta);
            ClickItem infoButton = new ClickItem(info);
            clickItems.add(infoButton);
        }

        Gui.openInfiniteGuiList(player, clickItems, 0, invname, new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.decideOwnerManager(player, region);
            }
        });

    }

    public static void openMemberManager(Player player, Region region, OfflinePlayer member) {
        GuiInventory inv = new GuiInventory(9, region.getRegion().getId() + " - " + member.getName());

        int itemcounter = 1;
        int actitem = 1;

        if(player.hasPermission(Permission.MEMBER_REMOVEMEMBER)){
            itemcounter++;
        }
        if(player.hasPermission(Permission.MEMBER_PROMOTE)){
            itemcounter++;
        }

        if(player.hasPermission(Permission.MEMBER_PROMOTE)){
            ItemStack makeOwnerItem = new ItemStack(Gui.PROMOTE_MEMBER_TO_OWNER_ITEM);
            ItemMeta makeOwnerItemMeta = makeOwnerItem.getItemMeta();
            makeOwnerItemMeta.setDisplayName(Messages.GUI_MAKE_OWNER_BUTTON);
            makeOwnerItemMeta.setLore(Messages.GUI_MAKE_OWNER_BUTTON_LORE);
            makeOwnerItem.setItemMeta(makeOwnerItemMeta);

            ClickItem makeOwnerMenu = new ClickItem(makeOwnerItem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    Gui.openMakeOwnerWarning(player, region, member, true);
                }
            });
            inv.addIcon(makeOwnerMenu, getPosition(actitem, itemcounter));
            actitem++;
        }

        if(player.hasPermission(Permission.MEMBER_REMOVEMEMBER)){
            ItemStack removeItem = new ItemStack(Gui.REMOVE_MEMBER_ITEM);
            ItemMeta removeItemMeta = removeItem.getItemMeta();
            removeItemMeta.setDisplayName(Messages.GUI_REMOVE_MEMBER_BUTTON);
            removeItemMeta.setLore(Messages.GUI_REMOVE_MEMBER_BUTTON_LORE);
            removeItem.setItemMeta(removeItemMeta);

            ClickItem removeMenu = new ClickItem(removeItem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    region.getRegion().removeMember(member.getUniqueId());
                    player.sendMessage(Messages.PREFIX + Messages.REGION_REMOVE_MEMBER_REMOVED);
                    player.closeInventory();
                }
            });
            inv.addIcon(removeMenu, getPosition(actitem, itemcounter));
            actitem++;
        }


        ItemStack goBack = new ItemStack(Gui.GO_BACK_ITEM);
        ItemMeta goBackMeta = goBack.getItemMeta();
        goBackMeta.setDisplayName(Messages.GUI_GO_BACK);
        goBack.setItemMeta(goBackMeta);

        ClickItem gobackButton = new ClickItem(goBack).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openMemberList(player, region);
            }
        });

        inv.addIcon(gobackButton, getPosition(actitem, itemcounter));
        actitem++;

        inv = Gui.placeFillItems(inv);

        player.openInventory(inv.getInventory());

    }

    public static void openMakeOwnerWarning(Player player, Region region, OfflinePlayer member, Boolean goback) {
        GuiInventory inv = new GuiInventory(9, Messages.GUI_MAKE_OWNER_WARNING_NAME);

        ItemStack yesItem = new ItemStack(Gui.WARNING_YES_ITEM);
        ItemMeta yesItemMeta = yesItem.getItemMeta();
        yesItemMeta.setDisplayName(Messages.GUI_YES);
        yesItem.setItemMeta(yesItemMeta);
        Player onlinemember = Bukkit.getPlayer(member.getUniqueId());
        ClickItem yesButton = new ClickItem(yesItem).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) throws InputException {
                player.closeInventory();
                if(onlinemember == null) {
                    throw new InputException(player, Messages.REGION_TRANSFER_MEMBER_NOT_ONLINE);
                }
                if(LimitGroup.isCanBuyAnother(onlinemember, region)) {
                    region.setNewOwner(onlinemember);
                    player.sendMessage(Messages.PREFIX + Messages.REGION_TRANSFER_COMPLETE_MESSAGE);
                } else {
                    throw new InputException(player, Messages.REGION_TRANSFER_LIMIT_ERROR);
                }
            }
        });
        inv.addIcon(yesButton, 0);

        ItemStack noItem = new ItemStack(Gui.WARNING_NO_ITEM);
        ItemMeta noItemMeta = noItem.getItemMeta();
        noItemMeta.setDisplayName(Messages.GUI_NO);
        noItem.setItemMeta(noItemMeta);

        ClickItem noButton = new ClickItem(noItem).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                if(goback) {
                    Gui.openMemberManager(player, region, member);
                } else {
                    player.closeInventory();
                }

            }
        });
        inv.addIcon(noButton, 8);

        inv = Gui.placeFillItems(inv);

        player.openInventory(inv.getInventory());
    }

    public static void openRegionMemberGui(Player player, Boolean withGoBack) {
        List<Region> regions = RegionManager.getRegionsByMember(player.getUniqueId());
        List<ClickItem> clickItems = new ArrayList<>();

        for (int i = 0; i < regions.size(); i++) {
            ItemStack regionItem = Gui.getRegionDisplayItem(regions.get(i), Messages.GUI_RENT_REGION_LORE, new ArrayList<>(), Messages.GUI_CONTRACT_REGION_LORE);
            int finalI = i;
            ClickItem clickItem = new ClickItem(regionItem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    Gui.openRegionManagerMember(player, regions.get(finalI));
                }
            });
            clickItems.add(clickItem);
        }

        if(regions.size() == 0){
            ItemStack info = new ItemStack(Gui.INFO_ITEM);
            ItemMeta infoMeta = info.getItemMeta();
            infoMeta.setDisplayName(Messages.GUI_MEMBER_INFO_ITEM);
            infoMeta.setLore(Messages.GUI_MEMBER_INFO_LORE);
            info.setItemMeta(infoMeta);
            ClickItem infoButton = new ClickItem(info);
            clickItems.add(infoButton);
        }


        ClickAction goBackAction = null;

        if(withGoBack) {
            goBackAction = new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    Gui.openARMGui(player);
                }
            };
        }

        Gui.openInfiniteGuiList(player, clickItems, 0, Messages.GUI_OWN_REGIONS_MENU_NAME, goBackAction);
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

        GuiInventory inv = new GuiInventory(9 , region.getRegion().getId());

        if(player.hasPermission(Permission.MEMBER_TP)){
            ItemStack teleporteritem = new ItemStack(Gui.TP_ITEM);
            ItemMeta teleporteritemmeta = teleporteritem.getItemMeta();
            teleporteritemmeta.setDisplayName(Messages.GUI_TELEPORT_TO_REGION_BUTTON);
            teleporteritemmeta.setLore(Messages.GUI_TELEPORT_TO_REGION_BUTTON_LORE);
            teleporteritem.setItemMeta(teleporteritemmeta);
            ClickItem teleportericon = new ClickItem(teleporteritem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) throws InputException {
                    Teleporter.teleport(player, region);
                    player.closeInventory();
                }
            });
            inv.addIcon(teleportericon, getPosition(actitem, itemcounter));

            actitem++;
        }

        if(player.hasPermission(Permission.MEMBER_INFO)){
            ItemStack infoitem = new ItemStack(Gui.INFO_ITEM);
            ItemMeta infoitemmeta = infoitem.getItemMeta();
            infoitemmeta.setDisplayName(Messages.GUI_SHOW_INFOS_BUTTON);
            infoitem.setItemMeta(infoitemmeta);
            ClickItem infoicon = new ClickItem(infoitem).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    region.regionInfo(player);
                    player.closeInventory();
                }
            });
            inv.addIcon(infoicon, getPosition(actitem, itemcounter));

            actitem++;
        }

        ItemStack gobackitem = new ItemStack(Gui.GO_BACK_ITEM);
        ItemMeta gobackitemmeta = gobackitem.getItemMeta();
        gobackitemmeta.setDisplayName(Messages.GUI_GO_BACK);
        gobackitem.setItemMeta(gobackitemmeta);
        ClickItem gobackicon = new ClickItem(gobackitem).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                Gui.openRegionMemberGui(player, isMainPageMultipleItems());
            }
        });
        inv.addIcon(gobackicon, getPosition(actitem, itemcounter));
        actitem++;

        inv = Gui.placeFillItems(inv);

        player.openInventory(inv.getInventory());
    }

    public static void openOvertakeGUI(Player player, List<Region> oldRegions){

        int invsize = 0;
        while (oldRegions.size() + 1 > invsize) {
            invsize = invsize + 9;
        }

        GuiInventory inv = new GuiInventory(invsize, Messages.GUI_TAKEOVER_MENU_NAME);
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
            ClickItem icon = new ClickItem(stack).addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    oldRegions.get(finalI).setNewOwner(player);
                    oldRegions.remove(finalI);
                    player.sendMessage(Messages.PREFIX + Messages.REGION_TRANSFER_COMPLETE_MESSAGE);
                    Gui.openOvertakeGUI(player, oldRegions);
                    }
                });
            inv.addIcon(icon, i);
        }

        ItemStack stack = new ItemStack(Gui.GO_BACK_ITEM);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(Messages.GUI_CLOSE);
        stack.setItemMeta(meta);
        ClickItem icon = new ClickItem(stack).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                player.closeInventory();
            }
        });
        inv.addIcon(icon, invsize - 1);

        inv = Gui.placeFillItems(inv);

        player.openInventory(inv.getInventory());
    }

    public static void openRegionResetWarning(Player player, Region region, Boolean goBack){
        GuiInventory inv = new GuiInventory(9, Messages.GUI_RESET_REGION_WARNING_NAME);

        ItemStack yesItem = new ItemStack(Gui.WARNING_YES_ITEM);
        ItemMeta yesItemMeta = yesItem.getItemMeta();
        yesItemMeta.setDisplayName(Messages.GUI_YES);
        yesItem.setItemMeta(yesItemMeta);

        ClickItem yesButton = new ClickItem(yesItem).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                player.closeInventory();
                region.userBlockReset(player);
            }
        });
        inv.addIcon(yesButton, 0);

        ItemStack noItem = new ItemStack(Gui.WARNING_NO_ITEM);
        ItemMeta noItemMeta = noItem.getItemMeta();
        noItemMeta.setDisplayName(Messages.GUI_NO);
        noItem.setItemMeta(noItemMeta);

        ClickItem noButton = new ClickItem(noItem).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                if(goBack){
                    Gui.decideOwnerManager(player, region);
                } else {
                    player.closeInventory();
                }

            }
        });
        inv.addIcon(noButton, 8);

        inv = Gui.placeFillItems(inv);

        player.openInventory(inv.getInventory());
    }

    public static void openSellWarning(Player player, Region region, Boolean goBack){
        GuiInventory inv = new GuiInventory(9, Messages.GUI_USER_SELL_WARNING);

        ItemStack yesItem = new ItemStack(Gui.WARNING_YES_ITEM);
        ItemMeta yesItemMeta = yesItem.getItemMeta();
        yesItemMeta.setDisplayName(Messages.GUI_YES);
        yesItem.setItemMeta(yesItemMeta);

        ClickItem yesButton = new ClickItem(yesItem).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) throws InputException {
                player.closeInventory();
                if(region.getRegion().hasOwner(player.getUniqueId())) {
                    region.userSell(player);
                } else {
                    throw new InputException(player, Messages.REGION_NOT_OWN);
                }
            }
        });
        inv.addIcon(yesButton, 0);

        ItemStack noItem = new ItemStack(Gui.WARNING_NO_ITEM);
        ItemMeta noItemMeta = noItem.getItemMeta();
        noItemMeta.setDisplayName(Messages.GUI_NO);
        noItem.setItemMeta(noItemMeta);

        ClickItem noButton = new ClickItem(noItem).addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {
                if(goBack) {
                    Gui.decideOwnerManager(player, region);
                } else {
                    player.closeInventory();
                }

            }
        });
        inv.addIcon(noButton, 8);

        inv = Gui.placeFillItems(inv);

        player.openInventory(inv.getInventory());
    }

    public static void decideOwnerManager(Player player, Region region) {
        if(region instanceof RentRegion) {
            Gui.openRentRegionManagerOwner(player, (RentRegion) region);
        } else if (region instanceof SellRegion) {
            Gui.openSellRegionManagerOwner(player, region);
        } else if (region instanceof ContractRegion) {
            Gui.openContractRegionManagerOwner(player, (ContractRegion) region);
        }
    }

    private static boolean isMainPageMultipleItems(){
        FileConfiguration config = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getConfig();

        int itemcounter = 0;

        if(config.getBoolean("GUI.DisplayRegionOwnerButton")){
            itemcounter++;
        }
        if(config.getBoolean("GUI.DisplayRegionMemberButton")){
            itemcounter++;
        }
        if(config.getBoolean("GUI.DisplayRegionFinderButton")){
            itemcounter++;
        }
        return (itemcounter > 1);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof GuiInventory) {
            event.setCancelled(true);

            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();

                ItemStack itemStack = event.getCurrentItem();
                if (itemStack == null || itemStack.getType() == Material.AIR) return;

                GuiInventory customHolder = (GuiInventory) event.getView().getTopInventory().getHolder();

                ClickItem icon = customHolder.getIcon(event.getRawSlot());
                if (icon == null) return;

                for (ClickAction clickAction : icon.getClickActions()) {
                    try {
                        clickAction.execute(player);
                    } catch (InputException inputException) {
                        inputException.sendMessages();
                    }
                }
            }
        }
    }

    private static GuiInventory placeFillItems(GuiInventory inv) {
        if(Gui.FILL_ITEM != Material.AIR) {
            ItemStack fillItem = new ItemStack(Gui.FILL_ITEM);
            ItemMeta fillItemMeta = fillItem.getItemMeta();
            fillItemMeta.setDisplayName(" ");
            fillItem.setItemMeta(fillItemMeta);
            for(int i = 0; i < inv.getInventory().getSize(); i++) {
                if(inv.getIcon(i) == null) {
                    ClickItem fillIcon = new ClickItem(fillItem).addClickAction(new ClickAction() {
                        @Override
                        public void execute(Player player) {
                            return;
                        }
                    });
                    inv.addIcon(fillIcon, i);
                }
            }
            return inv;
        }
        return inv;
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

    public static void setFillItem(Material fillItem) {
        if(fillItem != null) {
            Gui.FILL_ITEM = fillItem;
        }
    }

    public static void setContractItem(Material contractItem) {
        if(contractItem != null) {
            Gui.CONTRACT_ITEM = contractItem;
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