package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class OptionModifyCommand<OptionClass> extends BasicArmCommand {
    private boolean allowSubregions;
    private String subregionModifyErrorMessage;
    private String regex_massaction;
    private String optionName;

    public OptionModifyCommand(String rootCommand, List<String> permissions, String optionName, String optionRegex,
                               String optionDescriptipn, boolean allowSubregions, String subregionModifyErrorMessage) {
        super(false, rootCommand,
                Arrays.asList("(?i)" + rootCommand + " [^;\n ]+ " + optionRegex,
                        "(?i)" + rootCommand + " rk:[^;\n ]+ " + optionRegex),
                Arrays.asList(rootCommand + " [REGION] " + optionDescriptipn,
                        rootCommand + " rk:[REGIONKIND] " + optionDescriptipn),
                permissions);
        this.allowSubregions = allowSubregions;
        this.subregionModifyErrorMessage = subregionModifyErrorMessage;
        this.regex_massaction = "(?i)" + rootCommand + " rk:[^;\n ]+ " + optionRegex;
        this.optionName = optionName;
    }

    public OptionModifyCommand(String rootCommand, List<String> permissions, String optionName, boolean allowSubregions, String subregionModifyErrorMessage) {
        super(false, rootCommand,
                Arrays.asList("(?i)" + rootCommand + " [^;\n ]+",
                        "(?i)" + rootCommand + " rk:[^;\n ]+"),
                Arrays.asList(rootCommand + " [REGION]",
                        rootCommand + " rk:[REGIONKIND]"),
                permissions);
        this.allowSubregions = allowSubregions;
        this.subregionModifyErrorMessage = subregionModifyErrorMessage;
        this.regex_massaction = "(?i)" + rootCommand + " rk:[^;\n ]+";
        this.optionName = optionName;
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command) throws InputException {
        Player player = (Player) sender;
        String[] args = command.split(" ");
        List<Region> regions = new ArrayList<>();
        String selectedName;

        if (command.matches(regex_massaction)) {
            String[] splittedRegionKindArg = args[1].split(":", 2);

            RegionKind selectedRegionkind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(splittedRegionKindArg[1]);
            if (selectedRegionkind == null) {
                throw new InputException(sender, Messages.REGIONKIND_DOES_NOT_EXIST);
            }
            if (!this.allowSubregions && selectedRegionkind == RegionKind.SUBREGION) {
                throw new InputException(sender, this.subregionModifyErrorMessage);
            }
            regions = AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByRegionKind(selectedRegionkind);
            selectedName = selectedRegionkind.getConvertedMessage(Messages.MASSACTION_SPLITTER);
        } else {
            Region selectedRegion = AdvancedRegionMarket.getInstance().getRegionManager().getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());
            if (selectedRegion == null) {
                throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
            }

            if (!this.allowSubregions && selectedRegion.isSubregion()) {
                throw new InputException(sender, this.subregionModifyErrorMessage);
            }
            regions.add(selectedRegion);
            selectedName = selectedRegion.getRegion().getId();
        }

        OptionClass setting = getSettingFromString(player, args[2]);
        for (Region region : regions) {
            applySetting(region, setting);
        }

        sender.sendMessage(Messages.PREFIX + getSuccessMessage(selectedName, setting, this.optionName));
        return true;
    }

    protected abstract void applySetting(Region region, OptionClass setting);

    protected String getSuccessMessage(String selectedRegions, OptionClass setting, String optionName) {
        String sendmessage = Messages.REGION_MODIFIED;
        sendmessage = sendmessage.replace("%option%", optionName);
        sendmessage = sendmessage.replace("%selectedregions%", selectedRegions);
        return sendmessage;
    }

    /**
     *
     * @param settingsString
     * @return Not null
     */
    protected abstract OptionClass getSettingFromString(Player player, String settingsString) throws InputException;

    protected abstract List<String> tabCompleteSettingsObject(Player player, String setting);

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (args.length == 2) {
            returnme.addAll(AdvancedRegionMarket.getInstance()
                    .getRegionManager().completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, false));
            if ("rk:".startsWith(args[1])) {
                returnme.add("rk:");
            }
            if (args[1].matches("rk:([^;\n]+)?")) {
                returnme.addAll(AdvancedRegionMarket.getInstance()
                        .getRegionKindManager().completeTabRegionKinds(args[1], "rk:"));
            }

        } else if (args.length == 3) {
            returnme.addAll(tabCompleteSettingsObject(player, args[2]));
        }
        return returnme;
    }
}
