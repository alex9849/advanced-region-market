package net.alex9849.arm.gui;

import com.sk89q.worldguard.protection.flags.*;
import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.flaggroups.FlagSettings;
import net.alex9849.arm.gui.chathandler.GuiChatInputListener;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.*;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.util.MaterialFinder;
import net.alex9849.arm.util.TimeUtil;
import net.alex9849.inter.WGRegion;
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

import javax.annotation.Nullable;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Gui implements Listener {

    public static void openMainMenu(Player player, @Nullable ClickAction goBackAction) {
        FileConfiguration config = AdvancedRegionMarket.getInstance().getConfig();
        List<ClickItem> items = new ArrayList<>();
        ClickAction goBackActionForClickItems = p -> openMainMenu(p, goBackAction);

        if (config.getBoolean("GUI.DisplayRegionOwnerButton")) {
            items.add(new ClickItem(GuiConstants.getRegionOwnerItem())
                    .setName(Messages.GUI_MY_OWN_REGIONS)
                    .addClickAction((p) -> Gui.openRegionListOwner(p, goBackActionForClickItems)));
        }

        if (config.getBoolean("GUI.DisplayRegionMemberButton")) {
            items.add(new ClickItem(GuiConstants.getRegionMemberItem())
                    .setName(Messages.GUI_MY_MEMBER_REGIONS)
                    .addClickAction((p) -> Gui.openRegionListMember(p, goBackActionForClickItems)));
        }
        if (config.getBoolean("GUI.DisplayRegionFinderButton") && player.hasPermission(Permission.MEMBER_REGIONFINDER)) {
            items.add(new ClickItem(GuiConstants.getRegionFinderItem())
                    .setName(Messages.GUI_SEARCH_FREE_REGION)
                    .addClickAction((p) -> Gui.openRegionFinder(p, goBackActionForClickItems)));
        }
        if (items.size() == 1) {
            items.get(0).getClickActions().forEach(x -> {
                try {
                    x.execute(player);
                } catch (InputException e) {
                    e.sendMessages(Messages.PREFIX);
                }
            });
        }
        GuiUtils.addGoBackItem(items, goBackAction);
        GuiInventory menu = GuiUtils.generateInventory(items, Messages.GUI_MAIN_MENU_NAME);
        GuiUtils.placeClickItems(menu, items, 0);
        GuiUtils.placeFillItems(menu);
        player.openInventory(menu.getInventory());
    }

    public static void openRegionListOwner(Player player, @Nullable ClickAction goBackAction) {
        List<Region> regions = AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByOwner(player.getUniqueId());
        List<ClickItem> clickItems = new ArrayList<>();

        for (Region region : regions) {
            ItemStack regionItem = Gui.getRegionDisplayItem(region, Messages.GUI_RENT_REGION_LORE, new ArrayList<>(), Messages.GUI_CONTRACT_REGION_LORE);
            ClickItem clickItem = new ClickItem(regionItem)
                    .addClickAction(p -> Gui.openRegionOwnerManager(p, region, goBackAction));
            clickItems.add(clickItem);
        }
        GuiUtils.openInfiniteGuiList(player, clickItems, 0, Messages.GUI_OWN_REGIONS_MENU_NAME, goBackAction, null);
    }

    public static void openRegionOwnerManager(Player player, Region region, @Nullable ClickAction goBackAction) {
        ClickAction goBackActionForClickItems = p -> openRegionOwnerManager(player, region, goBackAction);
        List<ClickItem> items = new ArrayList<>();

        ItemStack membersitem = new ItemStack(MaterialFinder.getPlayerHead(), 1, (short) 3);
        SkullMeta membersitemmeta = (SkullMeta) membersitem.getItemMeta();
        FileConfiguration config = AdvancedRegionMarket.getInstance().getConfig();
        if (config.getBoolean("GUI.DisplayPlayerSkins")) {
            membersitemmeta.setOwningPlayer(player);
        }
        membersitemmeta.setDisplayName(Messages.GUI_MEMBERS_BUTTON);
        membersitem.setItemMeta(membersitemmeta);
        ClickItem membersicon = new ClickItem(membersitem).addClickAction(p ->
                Gui.openRegionAddedMembersList(p, region, goBackActionForClickItems));
        items.add(membersicon);

        if (Permission.hasAnySubregionPermission(player) && region.isAllowSubregions()) {
            ClickItem tpItem = new ClickItem(GuiConstants.getSubregionItem())
                    .setName(Messages.GUI_SUBREGION_ITEM_BUTTON)
                    .addClickAction(p -> Gui.openSubregionList(player, region, goBackActionForClickItems));
            items.add(tpItem);
        }

        if (player.hasPermission(Permission.MEMBER_TP)) {
            ClickItem teleportericon = new ClickItem(GuiConstants.getTpItem())
                    .setName(Messages.GUI_TELEPORT_TO_REGION_BUTTON)
                    .setLore(region.replaceVariables(Messages.GUI_TELEPORT_TO_REGION_BUTTON_LORE))
                    .addClickAction(new TeleportToRegionClickAction(region));
            items.add(teleportericon);
        }

        if (player.hasPermission(Permission.MEMBER_RESTORE) && region.isUserRestorable()
                && (!region.isProtectionOfContinuance() || player.hasPermission(Permission.MEMBER_RESTORE_PROTECTION_OF_CONTINUANCE))) {
            String coolDownTime = TimeUtil.timeInMsToString(AdvancedRegionMarket.getInstance().getPluginSettings().getUserResetCooldown(), true, false);
            List<String> lore = region.replaceVariables(Messages.GUI_RESET_REGION_BUTTON_LORE).stream()
                    .map(m -> m.replace("%userrestorecooldown%", coolDownTime)).collect(Collectors.toList());
            ClickItem reseticon = new ClickItem(GuiConstants.getResetItem())
                    .setName(Messages.GUI_RESET_REGION_BUTTON)
                    .setLore(lore)
                    .addClickAction(p -> {
                        if ((new GregorianCalendar().getTimeInMillis()) >= AdvancedRegionMarket.getInstance().getPluginSettings().getUserResetCooldown() + region.getLastreset()) {
                            Gui.openRegionRestoreWarning(player, region, lore, goBackActionForClickItems);
                        } else {
                            String message = region.replaceVariables(Messages.RESET_REGION_COOLDOWN_ERROR);
                            throw new InputException(player, message);
                        }
                    });
            items.add(reseticon);
        }

        if (player.hasPermission(Permission.MEMBER_SELLBACK)) {
            List<String> sellIconWarning = region.replaceVariables(Messages.GUI_USER_SELL_BUTTON_LORE);
            ClickItem sellIcon = new ClickItem(GuiConstants.getSellRegionItem())
                    .setName(Messages.GUI_USER_SELL_BUTTON)
                    .setLore(sellIconWarning)
                    .addClickAction(p ->
                            Gui.openSellWarning(player, region, false, sellIconWarning, goBackActionForClickItems));
            items.add(sellIcon);
        }

        if (player.hasPermission(Permission.MEMBER_FLAGEDITOR) && FlagGroup.isFeatureEnabled()) {
            ClickItem flagEditorItem = new ClickItem(GuiConstants.getFlageditorItem())
                    .setName(region.replaceVariables(Messages.GUI_FLAGEDITOR_BUTTON))
                    .addClickAction(p -> Gui.openFlagEditor(player, region, 0, goBackActionForClickItems));
            items.add(flagEditorItem);
        }

        if (region instanceof RentRegion) {
            RentRegion rentRegion = (RentRegion) region;
            ClickItem extendIcon = new ClickItem(GuiConstants.getExtendItem())
                    .setName(Messages.GUI_EXTEND_BUTTON)
                    .setLore(region.replaceVariables(Messages.GUI_EXTEND_BUTTON_LORE))
                    .addClickAction(p -> {
                        try {
                            rentRegion.extend(p);
                            openRegionOwnerManager(p, region, goBackAction);
                        } catch (NotEnoughMoneyException | RegionNotOwnException
                                | NotSoldException | NoPermissionException e) {
                            if (e.hasMessage()) player.sendMessage(Messages.PREFIX + e.getMessage());
                        }
                    });
            items.add(extendIcon);
        }

        if (region instanceof ContractRegion) {
            ContractRegion cregion = (ContractRegion) region;
            ClickItem extendIcon = new ClickItem(GuiConstants.getContractItem())
                    .setName(Messages.GUI_CONTRACT_ITEM)
                    .setLore(region.replaceVariables(Messages.GUI_CONTRACT_ITEM_LORE))
                    .addClickAction(p -> {
                        try {
                            cregion.changeTerminated(player);
                            Gui.openRegionOwnerManager(player, region, goBackAction);
                        } catch (OutOfLimitExeption | NoPermissionException | NotSoldException | RegionNotOwnException e) {
                            if (e.hasMessage()) player.sendMessage(Messages.PREFIX + e.getMessage());
                        }
                    });
            items.add(extendIcon);
        }

        if (player.hasPermission(Permission.MEMBER_ENTITYLIMIT_CHECK)) {
            ClickItem checkEntityLimitIcon = new ClickItem(getEntityLimtGroupItem(region))
                    .addClickAction(p -> {
                        net.alex9849.arm.entitylimit.commands.InfoCommand.sendInfoToSender(player, region.getEntityLimitGroup());
                        openRegionOwnerManager(player, region, goBackAction);
                    });
            items.add(checkEntityLimitIcon);
        }

        if (player.hasPermission(Permission.MEMBER_INFO)) {
            ClickItem infoIcon = new ClickItem(GuiConstants.getInfoItem())
                    .setName(Messages.GUI_SHOW_INFOS_BUTTON)
                    .addClickAction(p -> {
                        region.regionInfo(player);
                        player.closeInventory();
                    });
            items.add(infoIcon);
        }
        GuiUtils.addGoBackItem(items, goBackAction);

        GuiInventory inv = GuiUtils.generateInventory(items, region.getRegion().getId());
        GuiUtils.placeFillItems(inv);
        player.openInventory(inv.getInventory());
    }

    public static void openFlagEditor(Player player, Region region, int start, ClickAction goBackAction) {
        List<FlagSettings> fullFlagSettingsList = region.getFlagGroup().getFlagSettingsSold();
        if (!region.isSold()) {
            fullFlagSettingsList = region.getFlagGroup().getFlagSettingsAvailable();
        }
        List<FlagSettings> flagSettingsList = new ArrayList<>();
        for (FlagSettings flagSettings : fullFlagSettingsList) {
            if (flagSettings.isEditable() && flagSettings.getApplyTo().contains(region.getSellType())) {
                if (flagSettings.hasEditPermission()) {
                    if (player.hasPermission(flagSettings.getEditPermission())) {
                        flagSettingsList.add(flagSettings);
                    }
                } else {
                    flagSettingsList.add(flagSettings);
                }

            }
        }

        Collections.sort(flagSettingsList, (o1, o2) -> {
            return o1.getFlag().getName().compareTo(o2.getFlag().getName());
        });

        int invsize = ((flagSettingsList.size() * 9) - (start * 9) < 54) ? ((flagSettingsList.size() - start) * 9 + 9) : 54;
        GuiInventory guiInventory = new GuiInventory(invsize, region.replaceVariables(Messages.GUI_FLAGEDITOR_MENU_NAME));

        for (int i = start; (i - start) * 9 < (invsize - 9); i++) {
            FlagSettings flagSettings = flagSettingsList.get(i);
            Flag rgFlag = flagSettings.getFlag();
            int invIndex = (i - start) * 9;

            List<String> flagSettingsDescription = flagSettings.getGuidescription();
            for (int j = 0; j < flagSettingsDescription.size(); j++) {
                flagSettingsDescription.set(j, region.replaceVariables(flagSettingsDescription.get(j)));
            }
            ClickItem flagItem = new ClickItem(GuiConstants.getFlagItem())
                    .setName(rgFlag.getName())
                    .setLore(flagSettingsDescription);
            guiInventory.addIcon(flagItem, invIndex);

            ClickItem[] flagStateButtons = getFlagSettingItem(rgFlag, region, (p) -> {
                openFlagEditor(p, region, start, goBackAction);
            });

            if (flagStateButtons.length > 0) {
                guiInventory.addIcon(flagStateButtons[0], invIndex + 1);
            }
            if (flagStateButtons.length > 1) {
                guiInventory.addIcon(flagStateButtons[1], invIndex + 2);
            }

            ClickItem deleteButton = new ClickItem(GuiConstants.getFlagRemoveItem())
                    .setName(region.replaceVariables(Messages.GUI_FLAGEDITOR_DELETE_FLAG_BUTTON))
                    .addClickAction(pl -> {
                        region.getRegion().deleteFlags(rgFlag);
                        openFlagEditor(pl, region, start, goBackAction);
                        pl.sendMessage(Messages.PREFIX + region.replaceVariables(Messages.FlAGEDITOR_FLAG_HAS_BEEN_DELETED));
                    });
            guiInventory.addIcon(deleteButton, invIndex + 3);

            ClickAction afterFlagSetAction = (pl) -> {
                openFlagEditor(pl, region, start, goBackAction);
            };

            final String[][] flagOptions = new String[][]{
                    {"all", Messages.GUI_FLAGEDITOR_SET_FLAG_GROUP_ALL_BUTTON},
                    {"members", Messages.GUI_FLAGEDITOR_SET_FLAG_GROUP_MEMBERS_BUTTON},
                    {"owners", Messages.GUI_FLAGEDITOR_SET_FLAG_GROUP_OWNERS_BUTTON},
                    {"non_members", Messages.GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_MEMBERS_BUTTON},
                    {"non_owners", Messages.GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_OWNERS_BUTTON}};

            for (int j = 0; j < flagOptions.length; j++) {
                FlagSetter gfsAllButton = new FlagSetter(region, rgFlag.getRegionGroupFlag(), rgFlag, flagOptions[j][0], afterFlagSetAction);
                ClickItem allButton = new ClickItem(gfsAllButton.isInputSelected() ? GuiConstants.getFlagGroupSelectedItem() :
                        GuiConstants.getFlagGroupNotSelectedItem())
                        .setName(region.replaceVariables(flagOptions[j][1]))
                        .addClickAction(gfsAllButton);
                guiInventory.addIcon(allButton, invIndex + 4 + j);
            }
        }

        if (start != 0) {
            final int newstart;
            newstart = (start - 5 < 0) ? 0 : start - 5;
            ClickItem prevButton = new ClickItem(GuiConstants.getPrevPageItem())
                    .setName(Messages.GUI_PREV_PAGE)
                    .addClickAction(p -> openFlagEditor(p, region, newstart, goBackAction));
            guiInventory.addIcon(prevButton, guiInventory.getSize() - 9);
        }

        ClickItem resetButton = new ClickItem(GuiConstants.getFlageditorResetItem())
                .setName(region.replaceVariables(Messages.GUI_FLAGEDITOR_RESET_BUTTON))
                .addClickAction(p -> {
                    try {
                        //Force apply, because menu can only be opened if the flagGroups feature is enabled
                        region.applyFlagGroup(FlagGroup.ResetMode.COMPLETE, true);
                    } catch (FeatureDisabledException e) {
                        //Exception can't be thrown. Ignore
                    }
                    player.sendMessage(Messages.PREFIX + region.replaceVariables(Messages.FLAGEDITOR_FLAG_HAS_BEEN_UPDATED));
                    openFlagEditor(player, region, start, goBackAction);
                });
        guiInventory.addIcon(resetButton, guiInventory.getSize() - 7);

        if (goBackAction != null) {
            ClickItem goBackButton = new ClickItem(GuiConstants.getGoBackItem())
                    .setName(Messages.GUI_GO_BACK)
                    .addClickAction(goBackAction);
            guiInventory.addIcon(goBackButton, guiInventory.getSize() - 3);
        }

        if (flagSettingsList.size() > start + 5) {
            ClickItem prevButton = new ClickItem(GuiConstants.getNextPageItem())
                    .setName(Messages.GUI_NEXT_PAGE)
                    .addClickAction(p -> openFlagEditor(player, region, start + 5, goBackAction));
            guiInventory.addIcon(prevButton, guiInventory.getSize() - 1);
        }

        GuiUtils.placeFillItems(guiInventory);
        player.openInventory(guiInventory.getInventory());
    }

    public static void openRegionMemberManager(Player player, Region region, @Nullable ClickAction goBackAction) {
        List<ClickItem> items = new ArrayList<>();

        if (player.hasPermission(Permission.MEMBER_TP)) {
            ClickItem teleporterIcon = new ClickItem(GuiConstants.getTpItem())
                    .setName(Messages.GUI_TELEPORT_TO_REGION_BUTTON)
                    .setLore(region.replaceVariables(Messages.GUI_TELEPORT_TO_REGION_BUTTON_LORE))
                    .addClickAction(new TeleportToRegionClickAction(region));
            items.add(teleporterIcon);
        }

        if (player.hasPermission(Permission.MEMBER_ENTITYLIMIT_CHECK)) {
            ClickItem infoIcon = new ClickItem(getEntityLimtGroupItem(region))
                    .addClickAction(p -> {
                        net.alex9849.arm.entitylimit.commands.InfoCommand.sendInfoToSender(player, region.getEntityLimitGroup());
                        openRegionMemberManager(p, region, p2 -> openRegionMemberManager(p2, region, goBackAction));
                    });
            items.add(infoIcon);
        }

        if (player.hasPermission(Permission.MEMBER_INFO)) {
            ClickItem infoIcon = new ClickItem(GuiConstants.getInfoItem())
                    .setName(Messages.GUI_SHOW_INFOS_BUTTON)
                    .addClickAction(p -> {
                        region.regionInfo(player);
                        player.closeInventory();
                    });
            items.add(infoIcon);
        }

        GuiUtils.addGoBackItem(items, goBackAction);
        GuiInventory inv = GuiUtils.generateInventory(items, region.getRegion().getId());
        GuiUtils.placeFillItems(inv);
        player.openInventory(inv.getInventory());
    }

    public static void openSubregionList(Player player, Region region, @Nullable ClickAction goBackAction) {
        List<ClickItem> clickItems = new ArrayList<>();
        for (Region subregion : region.getSubregions()) {
            ItemStack subregionItem = Gui.getRegionDisplayItem(subregion, Messages.GUI_SUBREGION_REGION_INFO_RENT,
                    Messages.GUI_SUBREGION_REGION_INFO_SELL, Messages.GUI_SUBREGION_REGION_INFO_CONTRACT);
            ClickItem subregionClickItem = new ClickItem(subregionItem)
                    .addClickAction(p ->
                            Gui.openSubregionManager(p, subregion, region, pl ->
                                    openSubregionList(pl, region, goBackAction)
                            ));
            clickItems.add(subregionClickItem);
        }
        if (clickItems.size() == 0) {
            ClickItem infoItem = new ClickItem(GuiConstants.getInfoItem())
                    .setName(Messages.GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM)
                    .setLore(region.replaceVariables(Messages.GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM_LORE));
            clickItems.add(infoItem);
        }
        GuiUtils.openInfiniteGuiList(player, clickItems, 0, Messages.GUI_SUBREGION_LIST_MENU_NAME, goBackAction, null);
    }

    public static void openSubregionManager(Player player, Region region, Region parentRegion, @Nullable ClickAction goBackAction) {
        List<ClickItem> items = new ArrayList<>();

        if (player.hasPermission(Permission.SUBREGION_SET_IS_HOTEL)) {
            ClickItem isHotelItem = new ClickItem(GuiConstants.getHotelSettingItem())
                    .setName(Messages.GUI_SUBREGION_HOTEL_BUTTON)
                    .setLore(region.replaceVariables(Messages.GUI_SUBREGION_HOTEL_BUTTON_LORE))
                    .addClickAction(p -> {
                        region.setHotel(!region.isHotel());
                        Gui.openSubregionManager(p, region, parentRegion, goBackAction);
                    });
            items.add(isHotelItem);
        }

        if (player.hasPermission(Permission.SUBREGION_TP)) {
            ClickItem tpItem = new ClickItem(GuiConstants.getTpItem())
                    .setName(Messages.GUI_TELEPORT_TO_REGION_BUTTON)
                    .addClickAction(new TeleportToRegionClickAction(region));
            items.add(tpItem);
        }

        if (player.hasPermission(Permission.MEMBER_INFO)) {
            ClickItem infoItem = new ClickItem(GuiConstants.getInfoItem())
                    .setName(Messages.GUI_SHOW_INFOS_BUTTON)
                    .addClickAction(p -> {
                        region.regionInfo(player);
                        player.closeInventory();
                    });
            items.add(infoItem);
        }
        if (player.hasPermission(Permission.SUBREGION_RESTORE) && region.isUserRestorable()
                && (!region.isProtectionOfContinuance() || player.hasPermission(Permission.MEMBER_RESTORE_PROTECTION_OF_CONTINUANCE))) {
            ClickItem resetItem = new ClickItem(GuiConstants.getResetItem())
                    .setName(Messages.GUI_RESET_REGION_BUTTON)
                    .addClickAction(p -> {
                        if (!((new GregorianCalendar().getTimeInMillis()) >= AdvancedRegionMarket.getInstance().getPluginSettings().getUserResetCooldown() + region.getLastreset())) {
                            String message = region.replaceVariables(Messages.RESET_REGION_COOLDOWN_ERROR);
                            throw new InputException(player, message);
                        }
                        Gui.openWarning(player,
                                pl -> {
                                    try {
                                        if (player.hasPermission(Permission.MEMBER_RESTORE_PROTECTION_OF_CONTINUANCE)) {
                                            region.setProtectionOfContinuance(false);
                                        }
                                        region.userRestore(player);
                                        player.sendMessage(Messages.PREFIX + Messages.RESET_COMPLETE);
                                    } catch (SchematicNotFoundException e) {
                                        player.sendMessage(Messages.PREFIX + Messages.SCHEMATIC_NOT_FOUND_ERROR_USER.replace("%regionid%", e.getRegion().getId()));
                                        AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, region.replaceVariables(Messages.COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG));
                                    } catch (ProtectionOfContinuanceException e) {
                                        player.sendMessage(Messages.PREFIX + Messages.REGION_RESTORE_PROTECTION_OF_CONTINUANCE_ERROR);
                                    }
                                },
                                pl -> Gui.openSubregionManager(player, region, parentRegion, goBackAction),
                                Messages.GUI_RESET_REGION_WARNING_NAME,
                                new ArrayList<>(), new ArrayList<>());
                    });
            items.add(resetItem);
        }
        if (player.hasPermission(Permission.SUBREGION_UNSELL)) {
            List<String> unsellButtonLore = region.replaceVariables(Messages.UNSELL_REGION_BUTTON_LORE);
            ClickItem unsellItem = new ClickItem(GuiConstants.getUnsellItem())
                    .setName(Messages.UNSELL_REGION_BUTTON)
                    .setLore(unsellButtonLore)
                    .addClickAction(p ->
                            openWarning(player, pl -> {
                                region.unsell(Region.ActionReason.MANUALLY_BY_PARENT_REGION_OWNER, true, false);
                                player.closeInventory();
                                player.sendMessage(Messages.PREFIX + Messages.REGION_NOW_AVAILABLE);
                            }, pl -> openSubregionManager(player, region, parentRegion, goBackAction),
                                    Messages.UNSELL_REGION_WARNING_NAME, unsellButtonLore, new ArrayList<>()));
            items.add(unsellItem);
        }
        if (player.hasPermission(Permission.SUBREGION_DELETE_AVAILABLE) || player.hasPermission(Permission.SUBREGION_DELETE_SOLD)) {
            ClickItem deleteItem = new ClickItem(GuiConstants.getDeleteItem())
                    .setName(Messages.GUI_SUBREGION_DELETE_REGION_BUTTON)
                    .addClickAction(p -> {
                        if (region.isSold() && (!player.hasPermission(Permission.SUBREGION_DELETE_SOLD))) {
                            throw new InputException(player, Messages.NOT_ALLOWED_TO_REMOVE_SUBREGION_SOLD);
                        }
                        if ((!region.isSold()) && (!player.hasPermission(Permission.SUBREGION_DELETE_AVAILABLE))) {
                            throw new InputException(player, Messages.NOT_ALLOWED_TO_REMOVE_SUBREGION_AVAILABLE);
                        }
                        Gui.openWarning(player, pl -> {
                                    region.delete();
                                    player.closeInventory();
                                    player.sendMessage(Messages.PREFIX + Messages.REGION_DELETED);
                                }, pl -> Gui.openSubregionManager(player, region, parentRegion, goBackAction)
                                , Messages.DELETE_REGION_WARNING_NAME, new ArrayList<>(), new ArrayList<>());
                    });
            items.add(deleteItem);
        }

        GuiInventory inv = GuiUtils.generateInventory(items, region.getRegion().getId());
        GuiUtils.addGoBackItem(items, goBackAction);
        GuiUtils.placeFillItems(inv);
        player.openInventory(inv.getInventory());
    }

    public static void openRegionFinder(Player player, @Nullable ClickAction goBackAction) {
        List<ClickItem> items = new ArrayList<>();

        for (RegionKind regionKind : AdvancedRegionMarket.getInstance().getRegionKindManager()) {
            if (regionKind.isDisplayInRegionfinder()) {
                ClickItem clickItem = new ClickItem(regionKind.getMaterial())
                        .setName(regionKind.replaceVariables(Messages.GUI_REGIONFINDER_REGIONKIND_NAME))
                        .setLore(regionKind.getLore())
                        .setCustomItemModel(regionKind.getCustomItemModel())
                        .addClickAction(p -> {
                            Gui.openRegionFinderSellTypeSelector(player, AdvancedRegionMarket.getInstance()
                                    .getRegionManager().getBuyableRegions(regionKind), pl -> Gui.openRegionFinder(player, goBackAction));
                        });
                items.add(clickItem);
            }
        }


        ClickItem limitItem = null;
        if (player.hasPermission(Permission.MEMBER_LIMIT)) {
            limitItem = new ClickItem(GuiConstants.getInfoItem())
                    .setName(Messages.GUI_MY_LIMITS_BUTTON)
                    .addClickAction(p -> AdvancedRegionMarket.getInstance()
                            .getLimitGroupManager().printLimitInChat(p));
        }

        GuiUtils.openInfiniteGuiList(player, items, 0, Messages.GUI_REGION_FINDER_MENU_NAME, goBackAction, limitItem);
    }

    public static void openRegionFinderSellTypeSelector(Player player, List<Region> regions, ClickAction goBackAction) throws InputException {
        if (regions.isEmpty()) {
            throw new InputException(player, Messages.NO_FREE_REGION_WITH_THIS_KIND);
        }
        List<ClickItem> guiItems = new ArrayList<>();
        Map<SellType, List<Region>> sellTypeMap = regions.stream().collect(Collectors.groupingBy(r -> r.getSellType()));

        for(Map.Entry<SellType, List<Region>> entry : sellTypeMap.entrySet()) {
            ClickItem sellTypeClickItem = new ClickItem(MaterialFinder.getBRICKS())
                    .setName(entry.getKey().getName())
                    .addClickAction(p -> {
                        List<Region> sellTypeRegions = entry.getValue();
                        sellTypeRegions.sort((r1, r2) -> (int) (r1.getPricePerPeriod() - r2.getPricePerPeriod()));
                        List<ClickItem> clickItems = sellTypeMap.get(entry.getKey()).stream()
                                .map(region -> {
                                    ItemStack itemStack = getRegionDisplayItem(region, Messages.GUI_REGIONFINDER_REGION_INFO_RENT,
                                            Messages.GUI_REGIONFINDER_REGION_INFO_SELL, Messages.GUI_REGIONFINDER_REGION_INFO_CONTRACT);
                                    ClickItem item = new ClickItem(itemStack)
                                            .addClickAction(pl -> Gui.openRegionFinderTeleportLocationSelector(pl, region));
                                    return item;
                                }).collect(Collectors.toList());
                        GuiUtils.openInfiniteGuiList(player, clickItems, 0, Messages.GUI_REGION_FINDER_MENU_NAME, goBackAction, null);
                    });
            guiItems.add(sellTypeClickItem);
        }

        if(GuiUtils.executeIfOnlyItem(player, guiItems)) {
            return;
        }
        GuiInventory inv = GuiUtils.generateInventory(guiItems, Messages.GUI_REGION_FINDER_MENU_NAME);
        GuiUtils.placeFillItems(inv);
        player.openInventory(inv.getInventory());
    }

    public static void openRegionFinderTeleportLocationSelector(Player player, Region region) throws InputException {
        List<ClickItem> clickItems = new ArrayList<>();

        boolean hasTpToRegionPermission = player.hasPermission(Permission.MEMBER_REGIONFINDER_TP_TO_REGION);
        boolean hasTpToSignPermission = player.hasPermission(Permission.MEMBER_REGIONFINDER_TP_TO_SIGN);
        boolean hasSign = region.getNumberOfSigns() != 0;

        if(hasTpToRegionPermission && hasTpToSignPermission && hasSign) {
            ClickItem clickSign = new ClickItem(GuiConstants.getTeleportToSignItem())
                    .setName(Messages.GUI_TELEPORT_TO_SIGN)
                    .addClickAction(p -> {
                try {
                    region.teleport(player, true);
                    player.closeInventory();
                } catch (NoSaveLocationException e) {
                    player.sendMessage(Messages.PREFIX + region.replaceVariables(Messages.TELEPORTER_NO_SAVE_LOCATION_FOUND));
                }
            });
            clickItems.add(clickSign);

            ClickItem clickRegion = new ClickItem(GuiConstants.getTeleportToRegionItem())
                    .setName(Messages.GUI_TELEPORT_TO_REGION)
                    .addClickAction(new TeleportToRegionClickAction(region));
            clickItems.add(clickRegion);
        }

        if(clickItems.isEmpty()) {
            return;
        }
        if(GuiUtils.executeIfOnlyItem(player, clickItems)) {
            return;
        }
        GuiInventory inv = GuiUtils.generateInventory(clickItems, Messages.GUI_TELEPORT_TO_SIGN_OR_REGION);
        GuiUtils.placeFillItems(inv);
        player.openInventory(inv.getInventory());
    }

    public static void openRegionAddedMembersList(Player player, Region region, ClickAction goBackAction) {
        ArrayList<UUID> members = region.getRegion().getMembers();
        List<ClickItem> clickItems = new ArrayList<>();

        FileConfiguration config = AdvancedRegionMarket.getInstance().getConfig();
        boolean showPlayerSkins = config.getBoolean("GUI.DisplayPlayerSkins");

        for (UUID memberUUID : members) {
            ItemStack membersitem = new ItemStack(MaterialFinder.getPlayerHead(), 1, (short) 3);
            SkullMeta membersitemmeta = (SkullMeta) membersitem.getItemMeta();
            OfflinePlayer memberPlayer = Bukkit.getOfflinePlayer(memberUUID);
            if (memberPlayer != null) {
                if (showPlayerSkins) {
                    membersitemmeta.setOwningPlayer(player);
                }
                membersitemmeta.setDisplayName(memberPlayer.getName());
            }
            membersitem.setItemMeta(membersitemmeta);
            ClickItem membersicon = new ClickItem(membersitem)
                    .addClickAction(p -> Gui.openaddedMemberManager(player, region, memberPlayer, pl ->
                            openRegionAddedMembersList(pl, region, goBackAction)));
            clickItems.add(membersicon);
        }

        ClickItem howToAddMebersItem = null;
        if (members.size() < region.getMaxMembers() || region.getMaxMembers() == -1) {
            howToAddMebersItem = new ClickItem(GuiConstants.getInfoItem())
                    .setName(Messages.GUI_OWNER_MEMBER_INFO_ITEM)
                    .setLore(region.replaceVariables(Messages.GUI_OWNER_MEMBER_INFO_LORE));
        }

        GuiUtils.openInfiniteGuiList(player, clickItems, 0,
                region.replaceVariables(Messages.GUI_MEMBER_LIST_MENU_NAME), p ->
                openRegionOwnerManager(player, region, goBackAction), howToAddMebersItem);

    }

    public static void openaddedMemberManager(Player player, Region region, OfflinePlayer member, ClickAction goBackAction) {
        List<ClickItem> clickItems = new ArrayList<>();

        if (player.hasPermission(Permission.MEMBER_PROMOTE)) {
            List<String> makeOwnerWarningLore = region.replaceVariables(Messages.GUI_MAKE_OWNER_BUTTON_LORE);
            ClickItem makeOwnerItem = new ClickItem(GuiConstants.getPromoteMemberToOwnerItem())
                    .setName(Messages.GUI_MAKE_OWNER_BUTTON)
                    .setLore(makeOwnerWarningLore)
                    .addClickAction(p -> openMakeOwnerWarning(player, region, member, makeOwnerWarningLore, pl ->
                            openaddedMemberManager(player, region, member, goBackAction)));
            clickItems.add(makeOwnerItem);
        }

        if (player.hasPermission(Permission.MEMBER_REMOVEMEMBER)) {
            ClickItem removeItem = new ClickItem(GuiConstants.getRemoveMemberItem())
                    .setName(Messages.GUI_REMOVE_MEMBER_BUTTON)
                    .setLore(region.replaceVariables(Messages.GUI_REMOVE_MEMBER_BUTTON_LORE))
                    .addClickAction(p -> {
                        region.getRegion().removeMember(member.getUniqueId());
                        player.sendMessage(Messages.PREFIX + Messages.REGION_REMOVE_MEMBER_REMOVED);
                        player.closeInventory();
                    });
            clickItems.add(removeItem);
        }

        GuiUtils.addGoBackItem(clickItems, goBackAction);
        GuiInventory inv = GuiUtils.generateInventory(clickItems, region.getRegion().getId() + " - " + member.getName());
        GuiUtils.placeFillItems(inv);
        player.openInventory(inv.getInventory());
    }

    public static void openMakeOwnerWarning(Player player, Region region, OfflinePlayer member, List<String> yesLore, ClickAction goBackAction) {
        Player onlinemember = Bukkit.getPlayer(member.getUniqueId());

        openWarning(player, p -> {
            player.closeInventory();
            if (onlinemember == null) {
                throw new InputException(player, Messages.REGION_TRANSFER_MEMBER_NOT_ONLINE);
            }
            if (AdvancedRegionMarket.getInstance().getLimitGroupManager().isCanBuyAnother(onlinemember, region.getRegionKind())) {
                WGRegion wgRegion = region.getRegion();
                for (UUID oldOwner : wgRegion.getOwners()) {
                    wgRegion.addMember(oldOwner);
                }
                region.setOwner(onlinemember);
                player.sendMessage(Messages.PREFIX + Messages.REGION_TRANSFER_COMPLETE_MESSAGE);
            } else {
                throw new InputException(player, Messages.REGION_TRANSFER_LIMIT_ERROR);
            }
        }, p -> {
            if (goBackAction != null) {
                goBackAction.execute(player);
            } else {
                player.closeInventory();
            }
        }, Messages.GUI_MAKE_OWNER_WARNING_NAME, yesLore, new ArrayList<>());
    }

    public static void openWarning(Player player, ClickAction yesAction, ClickAction noAction, String title, List<String> yesLore, List<String> noLore) {
        GuiInventory inv = new GuiInventory(GuiConstants.GUI_ROW_SIZE, title);
        ClickItem yesButton = new ClickItem(GuiConstants.getWarningYesItem())
                .setName(Messages.GUI_YES)
                .setLore(yesLore)
                .addClickAction(yesAction);
        inv.addIcon(yesButton, 0);

        ClickItem noButton = new ClickItem(GuiConstants.getWarningNoItem())
                .setName(Messages.GUI_NO)
                .setLore(noLore)
                .addClickAction(noAction);
        inv.addIcon(noButton, GuiConstants.GUI_ROW_SIZE - 1);

        GuiUtils.placeFillItems(inv);
        player.openInventory(inv.getInventory());
    }

    public static void openRegionListMember(Player player, ClickAction goBackAction) {
        List<ClickItem> clickItems = new ArrayList<>();
        List<Region> regions = AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByMember(player.getUniqueId());

        for (Region region : regions) {
            ItemStack regionItem = Gui.getRegionDisplayItem(region, Messages.GUI_RENT_REGION_LORE, new ArrayList<>(), Messages.GUI_CONTRACT_REGION_LORE);
            ClickItem clickItem = new ClickItem(regionItem)
                    .addClickAction(p -> openRegionMemberManager(p, region, pl ->
                            openRegionListMember(pl, goBackAction)));
            clickItems.add(clickItem);
        }

        ClickItem howToBecomeAMemberItem = new ClickItem(GuiConstants.getInfoItem())
                .setName(Messages.GUI_MEMBER_INFO_ITEM)
                .setLore(Messages.GUI_MEMBER_INFO_LORE);

        GuiUtils.openInfiniteGuiList(player, clickItems, 0, Messages.GUI_MEMBER_REGIONS_MENU_NAME, goBackAction, howToBecomeAMemberItem);
    }

    public static void openOvertakeGUI(Player player, List<Region> oldRegions, ClickAction goBackAction) {
        List<ClickItem> clickItems = oldRegions.stream()
                .map(region -> {
                    ClickItem regItem = new ClickItem(getRegionDisplayItem(region, Messages.GUI_TAKEOVER_ITEM_LORE,
                            Messages.GUI_TAKEOVER_ITEM_LORE, Messages.GUI_TAKEOVER_ITEM_LORE))
                            .addClickAction(p -> {
                                UUID oldOwnerUUID = region.getOwner();
                                if(oldOwnerUUID != null) {
                                    region.getRegion().addMember(oldOwnerUUID);
                                }
                                region.setOwner(player);
                                oldRegions.remove(region);
                                player.sendMessage(Messages.PREFIX + Messages.REGION_TRANSFER_COMPLETE_MESSAGE);
                                Gui.openOvertakeGUI(player, oldRegions, goBackAction);
                            });
                    return regItem;
                }).collect(Collectors.toList());

        GuiUtils.openInfiniteGuiList(player, clickItems, 0, Messages.GUI_TAKEOVER_MENU_NAME, goBackAction, null);
    }

    public static void openRegionRestoreWarning(Player player, Region region, List<String> yesLore, ClickAction goBackAction) {
        String coolDownTime = TimeUtil.timeInMsToString(AdvancedRegionMarket.getInstance()
                .getPluginSettings().getUserResetCooldown(), true, false);
        Gui.openWarning(player, p -> {
                    player.closeInventory();
                    try {
                        if (player.hasPermission(Permission.MEMBER_RESTORE_PROTECTION_OF_CONTINUANCE)) {
                            region.setProtectionOfContinuance(false);
                        }
                        region.userRestore(player);
                        player.sendMessage(Messages.PREFIX + Messages.RESET_COMPLETE);
                    } catch (SchematicNotFoundException e) {
                        player.sendMessage(Messages.PREFIX + Messages.SCHEMATIC_NOT_FOUND_ERROR_USER.replace("%regionid%", e.getRegion().getId()));
                        AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, region.replaceVariables(Messages.COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG));
                    } catch (ProtectionOfContinuanceException e) {
                        player.sendMessage(Messages.PREFIX + Messages.REGION_RESTORE_PROTECTION_OF_CONTINUANCE_ERROR);
                    }
                }, p -> {
                    if (goBackAction != null) {
                        goBackAction.execute(p);
                    } else {
                        player.closeInventory();
                    }
                }, Messages.GUI_RESET_REGION_WARNING_NAME,
                yesLore, new ArrayList<>());
    }

    public static void openSellWarning(Player player, Region region, boolean noMoney, List<String> yesLore, ClickAction goBackAction) {
        Gui.openWarning(player, p -> {
                    player.closeInventory();
                    if (region.getRegion().hasOwner(player.getUniqueId())) {
                        String soldSuccessfullyMessage = Messages.REGION_SOLD_BACK_SUCCESSFULLY;
                        if (noMoney) {
                            soldSuccessfullyMessage = soldSuccessfullyMessage.replace("%paybackmoney%", Price.formatPrice(0));
                        }
                        soldSuccessfullyMessage = region.replaceVariables(soldSuccessfullyMessage);
                        try {
                            region.userSell(player, noMoney);
                            player.sendMessage(Messages.PREFIX + soldSuccessfullyMessage);
                        } catch (SchematicNotFoundException e) {
                            AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, region.replaceVariables(Messages.COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG));
                            player.sendMessage(Messages.PREFIX + Messages.SCHEMATIC_NOT_FOUND_ERROR_USER.replace("%regionid%", e.getRegion().getId()));
                        } catch (NotEnoughMoneyException e) {
                            player.sendMessage(Messages.PREFIX + e.getMessage());
                        }
                    } else {
                        throw new InputException(player, Messages.REGION_NOT_OWN);
                    }
                }, p -> {
                    if (goBackAction != null) {
                        goBackAction.execute(p);
                    }
                }, Messages.GUI_USER_SELL_WARNING,
                yesLore, new ArrayList<>());
    }

    private static ItemStack getRegionDisplayItem(Region region, List<String> rentLore, List<String> sellLore, List<String> contractLore) {
        String regionDisplayName = Messages.GUI_REGION_ITEM_NAME;
        regionDisplayName = region.replaceVariables(regionDisplayName);

        ItemStack stack = new ItemStack(region.getRegionKind().getMaterial());
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(regionDisplayName);
        if(region.getRegionKind().getCustomItemModel() != -1) { meta.setCustomModelData(region.getRegionKind().getCustomItemModel()); }

        List<String> regionLore = new ArrayList<>();
        if (region instanceof RentRegion) {
            regionLore = new ArrayList<>(rentLore);
        } else if (region instanceof SellRegion) {
            regionLore = new ArrayList<>(sellLore);
        } else if (region instanceof ContractRegion) {
            regionLore = new ArrayList<>(contractLore);
        }
        meta.setLore(region.replaceVariables(regionLore));
        stack.setItemMeta(meta);
        return stack;
    }

    public static ClickItem[] getFlagSettingItem(Flag flag, Region region, ClickAction afterFlagSetAction) {
        ClickItem[] clickItems;
        if (flag instanceof StateFlag) {
            clickItems = new ClickItem[2];
            Gui.FlagSetter fs0 = new Gui.FlagSetter(region, flag, null, "allow", afterFlagSetAction);
            clickItems[0] = new ClickItem(fs0.isInputSelected() ? GuiConstants.getFlagSettingSelectedItem() :
                    GuiConstants.getFlagSettingNotSelectedItem())
                    .setName(region.replaceVariables(Messages.GUI_FLAGEDITOR_SET_STATEFLAG_ALLOW_BUTTON))
                    .addClickAction(fs0);
            Gui.FlagSetter fs1 = new Gui.FlagSetter(region, flag, null, "deny", afterFlagSetAction);
            clickItems[1] = new ClickItem(fs1.isInputSelected() ? GuiConstants.getFlagSettingSelectedItem() :
                    GuiConstants.getFlagSettingNotSelectedItem())
                    .setName(region.replaceVariables(Messages.GUI_FLAGEDITOR_SET_STATEFLAG_DENY_BUTTON))
                    .addClickAction(fs1);

        } else if (flag instanceof BooleanFlag) {
            clickItems = new ClickItem[2];
            Gui.FlagSetter fs0 = new Gui.FlagSetter(region, flag, null, "true", afterFlagSetAction);
            clickItems[0] = new ClickItem(fs0.isInputSelected() ? GuiConstants.getFlagSettingSelectedItem() :
                    GuiConstants.getFlagSettingNotSelectedItem())
                    .setName(region.replaceVariables(Messages.GUI_FLAGEDITOR_SET_BOOLEANFLAG_TRUE_BUTTON))
                    .addClickAction(fs0);
            Gui.FlagSetter fs1 = new Gui.FlagSetter(region, flag, null, "false", afterFlagSetAction);
            clickItems[1] = new ClickItem(fs1.isInputSelected() ? GuiConstants.getFlagSettingSelectedItem() :
                    GuiConstants.getFlagSettingNotSelectedItem())
                    .setName(region.replaceVariables(Messages.GUI_FLAGEDITOR_SET_BOOLEANFLAG_FALSE_BUTTON))
                    .addClickAction(fs1);

        } else if (flag instanceof StringFlag) {
            clickItems = new ClickItem[1];
            final Gui.FlagSetter flagSetter = new Gui.FlagSetter(region, flag, null, "", afterFlagSetAction);
            clickItems[0] = new ClickItem(GuiConstants.getFlagUserInputItem())
                    .setName(region.replaceVariables(Messages.GUI_FLAGEDITOR_SET_STRINGFLAG_SET_MESSAGE_BUTTON))
                    .addClickAction(p -> {
                        p.closeInventory();
                        p.sendMessage(region.replaceVariables(Messages.FLAGEDITOR_STRINGFLAG_SET_MESSAGE_INFO));
                        GuiChatInputListener gcil = new GuiChatInputListener(p, (s) -> {
                            flagSetter.setInput(s);
                            flagSetter.execute(p);
                        });
                        Bukkit.getPluginManager().registerEvents(gcil, AdvancedRegionMarket.getInstance());
                    });
        } else if (flag instanceof IntegerFlag) {

            clickItems = new ClickItem[1];
            final Gui.FlagSetter flagSetter = new Gui.FlagSetter(region, flag, null, "", afterFlagSetAction);
            clickItems[0] = new ClickItem(GuiConstants.getFlagUserInputItem())
                    .setName(region.replaceVariables(Messages.GUI_FLAGEDITOR_SET_INTEGERFLAG_SET_INTEGER_BUTTON))
                    .addClickAction(p -> {
                        p.closeInventory();
                        p.sendMessage(region.replaceVariables(Messages.FLAGEDITOR_INTEGERFLAG_SET_NUMBER_INFO));
                        GuiChatInputListener gcil = new GuiChatInputListener(p, (s) -> {
                            flagSetter.setInput(s);
                            flagSetter.execute(p);
                        });
                        Bukkit.getPluginManager().registerEvents(gcil, AdvancedRegionMarket.getInstance());
                    });
        } else if (flag instanceof DoubleFlag) {

            clickItems = new ClickItem[1];
            final Gui.FlagSetter flagSetter = new Gui.FlagSetter(region, flag, null, "", afterFlagSetAction);
            clickItems[0] = new ClickItem(GuiConstants.getFlagUserInputItem())
                    .setName(region.replaceVariables(Messages.GUI_FLAGEDITOR_SET_DOUBLEFLAG_SET_DOUBLE_BUTTON))
                    .addClickAction(p -> {
                        p.closeInventory();
                        p.sendMessage(region.replaceVariables(Messages.FLAGEDITOR_DOUBLEFLAG_SET_NUMBER_INFO));
                        GuiChatInputListener gcil = new GuiChatInputListener(p, (s) -> {
                            flagSetter.setInput(s);
                            flagSetter.execute(p);
                        });
                        Bukkit.getPluginManager().registerEvents(gcil, AdvancedRegionMarket.getInstance());
                    });
        } else {
            clickItems = new ClickItem[1];
            final Gui.FlagSetter flagSetter = new Gui.FlagSetter(region, flag, null, "", afterFlagSetAction);
            clickItems[0] = new ClickItem(GuiConstants.getFlagUserInputItem())
                    .setName(region.replaceVariables(Messages.GUI_FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_BUTTON))
                    .addClickAction(p -> {
                        p.closeInventory();
                        p.sendMessage(region.replaceVariables(Messages.FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_INFO));
                        GuiChatInputListener gcil = new GuiChatInputListener(p, (s) -> {
                            flagSetter.setInput(s);
                            flagSetter.execute(p);
                        });
                        Bukkit.getPluginManager().registerEvents(gcil, AdvancedRegionMarket.getInstance());
                    });
        }
        return clickItems;
    }

    public static ItemStack getEntityLimtGroupItem(Region region) {
        ItemStack itemStack = new ItemStack(MaterialFinder.getChickenSpawnEgg());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Messages.GUI_ENTITYLIMIT_ITEM_BUTTON);
        List<String> lore = new ArrayList<>(Messages.GUI_ENTITYLIMIT_ITEM_LORE);
        List<String> limitlist = new ArrayList<>();

        String totalstatus = region.getEntityLimitGroup().replaceVariables(Messages.GUI_ENTITYLIMIT_ITEM_INFO_PATTERN, new ArrayList<>(), region.getExtraTotalEntitys());
        if (region.getEntityLimitGroup().getSoftLimit(region.getExtraTotalEntitys()) < region.getEntityLimitGroup().getHardLimit()) {
            totalstatus = totalstatus.replace("%entityextensioninfo%", region.getEntityLimitGroup().replaceVariables(Messages.GUI_ENTITYLIMIT_ITEM_INFO_EXTENSION_INFO, new ArrayList<>(), region.getExtraTotalEntitys()));
        } else {
            totalstatus = totalstatus.replace("%entityextensioninfo%", "");
        }

        limitlist.add(totalstatus);

        for (EntityLimit entityLimit : region.getEntityLimitGroup().getEntityLimits()) {
            String entitystatus = entityLimit.replaceVariables(Messages.GUI_ENTITYLIMIT_ITEM_INFO_PATTERN, new ArrayList<>(), region.getExtraEntityAmount(entityLimit.getLimitableEntityType()));
            if ((entityLimit.getSoftLimit(region.getExtraEntityAmount(entityLimit.getLimitableEntityType())) < entityLimit.getHardLimit()) && !region.isSubregion()) {
                entitystatus = entitystatus.replace("%entityextensioninfo%", entityLimit.replaceVariables(Messages.GUI_ENTITYLIMIT_ITEM_INFO_EXTENSION_INFO, new ArrayList<>(), region.getExtraEntityAmount(entityLimit.getLimitableEntityType())));
            } else {
                entitystatus = entitystatus.replace("%entityextensioninfo%", "");
            }
            limitlist.add(entitystatus);
        }

        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, region.replaceVariables(lore.get(i)));
            if (lore.get(i).contains("%entityinfopattern%")) {
                lore.remove(i);
                lore.addAll(i, limitlist);
            }
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof GuiInventory) {
            event.setCancelled(true);

            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();

                ItemStack itemStack = event.getCurrentItem();
                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    return;
                }

                GuiInventory customHolder = (GuiInventory) event.getView().getTopInventory().getHolder();

                if (customHolder.getSize() <= event.getRawSlot()) {
                    return;
                }

                ClickItem icon = customHolder.getIcon(event.getRawSlot());
                if (icon == null) return;

                for (ClickAction clickAction : icon.getClickActions()) {
                    try {
                        clickAction.execute(player);
                    } catch (InputException inputException) {
                        inputException.sendMessages(Messages.PREFIX);
                    }
                }
            }
        }
    }

    private static class TeleportToRegionClickAction implements ClickAction {
        private Region region;

        TeleportToRegionClickAction(Region region) {
            this.region = region;
        }

        public void execute(Player player) {
            try {
                Teleporter.teleport(player, this.region);
                player.closeInventory();
            } catch (NoSaveLocationException e) {
                player.sendMessage(Messages.PREFIX + this.region.replaceVariables(Messages.TELEPORTER_NO_SAVE_LOCATION_FOUND));
            }
        }
    }

    private static class FlagSetter implements ClickAction {
        private String input;
        private Region region;
        private Flag flag;
        private Flag parentFlag;
        private ClickAction afterFlagSetAction;

        FlagSetter(Region region, Flag flag, Flag parentFlag, String input, ClickAction afterFlagSetAction) {
            this.input = input;
            this.region = region;
            this.flag = flag;
            this.parentFlag = parentFlag;
            this.afterFlagSetAction = afterFlagSetAction;
        }

        public void setInput(String s) {
            this.input = s;
        }

        public boolean isInputSelected() {
            if (this.flag == null) {
                return false;
            }
            try {
                Object settingsObj = getParsedSettingsObject();
                Object regionFlagSetting = region.getRegion().getFlagSetting(flag);

                if (parentFlag == null) {
                    if (regionFlagSetting == settingsObj) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    if (settingsObj == flag.getDefault()
                            && region.getRegion().getFlagSetting(parentFlag) != null
                            && regionFlagSetting == null) {
                        return true;
                    }
                    if (regionFlagSetting == settingsObj) {
                        return true;
                    }
                    return false;
                }


            } catch (InvalidFlagFormat e) {
                return false;
            }
        }

        private Object getParsedSettingsObject() throws InvalidFlagFormat {
            return AdvancedRegionMarket.getInstance().getWorldGuardInterface().parseFlagInput(flag, region.replaceVariables(this.input));
        }

        @Override
        public void execute(Player player) throws InputException {
            if (parentFlag != null && this.region.getRegion().getFlagSetting(this.parentFlag) == null) {
                throw new InputException(player, Messages.FlAGEDITOR_FLAG_NOT_ACTIVATED);
            }
            try {
                if (flag == null) {
                    throw new InvalidFlagFormat("");
                }
                Object flagSetting = getParsedSettingsObject();
                region.getRegion().setFlag(flag, flagSetting);
                afterFlagSetAction.execute(player);
                player.sendMessage(Messages.PREFIX + region.replaceVariables(Messages.FLAGEDITOR_FLAG_HAS_BEEN_UPDATED));
            } catch (InvalidFlagFormat invalidFlagFormat) {
                String flagname = "";
                if (flag != null) {
                    flagname = flag.getName();
                }
                Bukkit.getLogger().info("Could not modify flag " + flagname + " via player flageditor!");
                throw new InputException(player, Messages.FLAGEDITOR_FLAG_COULD_NOT_BE_MODIFIED.replace("%flag%", flag.getName()));
            }
        }
    }
}