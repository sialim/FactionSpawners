package com.bermei.factionspawners.commands;

import com.bermei.factionspawners.factions.Faction;
import com.bermei.factionspawners.utilities.ActionBarMessages;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class FactionPvpCommand implements SubCommand{
    private final FactionCommand commandManager;
    private final ActionBarMessages util;

    public FactionPvpCommand(FactionCommand commandManager) {
        this.commandManager = commandManager;
        util = commandManager.plugin.util;
    }

    @Override
    public String getName() {
        return "pvp";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;
        Player p = (Player) sender;

        if (args.length != 1) {
            util.actionBar(p, "/f pvp <on|off>");
            return;
        }

        UUID pUUID = p.getUniqueId();
        Faction faction = commandManager.factionManager.getFactionByMember(pUUID);
        if (faction == null) {
            util.actionBar(p, "not in faction", ChatColor.RED);
            return;
        }

        else if (!faction.owner.equals(pUUID)) {
            util.actionBar(p, "insufficient permission", ChatColor.RED);
            return;
        }

        String arg = args[0].toLowerCase();
        boolean newPvPStatus;

        if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("on")) {
            newPvPStatus = true;
        } else if (arg.equalsIgnoreCase("false") || arg.equals("off")) {
            newPvPStatus = false;
        } else {
            util.actionBar(p, "/f pvp <on|off>");
            return;
        }

        commandManager.factionManager.setFactionPvP(faction.name, newPvPStatus);
        faction.members.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(plr -> util.actionBar(plr,
                        "faction pvp has been " + (newPvPStatus ? "enabled" : "disabled"), ChatColor.DARK_RED));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("true", "false", "on", "off");
        }
        return Collections.emptyList();
    }
}
