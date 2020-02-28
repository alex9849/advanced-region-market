package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.util.Tuple;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class RegionOptionModifyCommand<SettingsObj> extends OptionModifyCommand<Tuple<String, List<Region>>, SettingsObj> {
    private boolean allowSubregions;
    private String subregionModifyErrorMessage;
    private String regex_massaction;
    private String optionName;

    public RegionOptionModifyCommand(String rootCommand, List<String> permissions, String optionName, String optionRegex,
                                     String optionDescriptipn, boolean allowSubregions, String subregionModifyErrorMessage) {
        super(false, rootCommand,
                Arrays.asList("(?i)" + rootCommand + " [^;\n ]+ " + optionRegex, "(?i)" + rootCommand + " rk:[^;\n ]+ " + optionRegex),
                Arrays.asList(rootCommand + " [REGION] " + optionDescriptipn, rootCommand + " rk:[REGIONKIND] " + optionDescriptipn),
                permissions, "", "");

        this.allowSubregions = allowSubregions;
        this.subregionModifyErrorMessage = subregionModifyErrorMessage;
        this.regex_massaction = "(?i)" + rootCommand + " rk:[^;\n ]+ " + optionRegex;
        this.optionName = optionName;
    }

    public RegionOptionModifyCommand(String rootCommand, List<String> permissions, String optionName, boolean allowSubregions, String subregionModifyErrorMessage) {
        super(false, rootCommand,
                Arrays.asList("(?i)" + rootCommand + " [^;\n ]+", "(?i)" + rootCommand + " rk:[^;\n ]+"),
                Arrays.asList(rootCommand + " [REGION]", rootCommand + " rk:[REGIONKIND]"),
                permissions, "", "");

        this.allowSubregions = allowSubregions;
        this.subregionModifyErrorMessage = subregionModifyErrorMessage;
        this.regex_massaction = "(?i)" + rootCommand + " rk:[^;\n ]+";
        this.optionName = optionName;
    }

    @Override
    protected Tuple<String, List<Region>> getObjectFromCommand(CommandSender sender, String command) throws InputException {
        String[] args = command.split(" ");
        Player player = (Player) sender;

        if (command.matches(regex_massaction)) {
            String[] splittedRegionKindArg = args[1].split(":", 2);

            RegionKind selectedRegionkind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(splittedRegionKindArg[1]);
            if (selectedRegionkind == null) {
                throw new InputException(sender, Messages.REGIONKIND_DOES_NOT_EXIST);
            }
            if (!this.allowSubregions && selectedRegionkind == RegionKind.SUBREGION) {
                throw new InputException(sender, this.subregionModifyErrorMessage);
            }
            String selectedName = selectedRegionkind.getConvertedMessage(Messages.MASSACTION_SPLITTER);
            return new Tuple<>(selectedName, AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByRegionKind(selectedRegionkind));
        } else {
            Region selectedRegion = AdvancedRegionMarket.getInstance().getRegionManager().getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());
            if (selectedRegion == null) {
                throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
            }

            if (!this.allowSubregions && selectedRegion.isSubregion()) {
                throw new InputException(sender, this.subregionModifyErrorMessage);
            }
            return new Tuple<>(selectedRegion.getRegion().getId(), Arrays.asList(selectedRegion));
        }
    }

    @Override
    protected SettingsObj getSettingsFromCommand(CommandSender sender, String command) throws InputException {
        return getSettingFromString((Player) sender, command.split(" ")[2]);
    }

    @Override
    protected void sendSuccessMessage(CommandSender sender, Tuple<String, List<Region>> obj, SettingsObj settingsObj) {
        String sendmessage = Messages.REGION_MODIFIED;
        sendmessage = sendmessage.replace("%option%", optionName);
        sendmessage = sendmessage.replace("%selectedregions%", obj.getValue1());
        sender.sendMessage(Messages.PREFIX + sendmessage);
    }

    @Override
    protected void applySetting(Tuple<String, List<Region>> tuple, SettingsObj setting) {
        for(Region region : tuple.getValue2()) {
            applySetting(region, setting);
        }
    }

    protected String getOptionName() {
        return this.optionName;
    }

    @Override
    protected List<String> tabCompleteObject(Player player, String[] args) {
        if (args.length == 2) {
            return new ArrayList<>();
        }

        List<String> returnme = new ArrayList<>();
        returnme.addAll(AdvancedRegionMarket.getInstance()
                .getRegionManager().completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, false));
        if ("rk:".startsWith(args[1])) {
            returnme.add("rk:");
        }
        if (args[1].matches("rk:([^;\n]+)?")) {
            returnme.addAll(AdvancedRegionMarket.getInstance()
                    .getRegionKindManager().completeTabRegionKinds(args[1], "rk:"));
        }
        return returnme;
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String[] args) {
        if (args.length != 3) {
            return new ArrayList<>();
        }
        return tabCompleteSettingsObject(player, args[2]);
    }

    protected abstract void applySetting(Region region, SettingsObj setting);

    protected abstract List<String> tabCompleteSettingsObject(Player player, String setting);

    /**
     * @param player
     * @param settingsString
     * @return Not null
     */
    protected abstract SettingsObj getSettingFromString(Player player, String settingsString) throws InputException;
}
