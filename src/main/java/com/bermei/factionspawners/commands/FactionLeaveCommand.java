package com.bermei.factionspawners.commands;

import com.bermei.factionspawners.factions.Faction;
import com.bermei.factionspawners.utilities.ActionBarMessages;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.Collections;
import java.util.List;

public class FactionLeaveCommand implements SubCommand{
    private final FactionCommand commandManager;
    private final ActionBarMessages util;

    public FactionLeaveCommand(FactionCommand commandManager) {
        this.commandManager = commandManager;
        this.util = commandManager.plugin.util;
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;
        Player p = (Player) sender;
        UUID pUUID = p.getUniqueId();

        Faction faction = commandManager.factionManager.getFactionByMember(pUUID);
        if (faction == null) {
            util.actionBar(p, "not in faction", ChatColor.RED);
            return;
        }

        else if (faction.owner.equals(pUUID)) {
            util.actionBar(p, "owner cannot leave faction. use /f delete instead", ChatColor.RED);
            return;
        }

        faction.removeMember(pUUID);
        commandManager.factionManager.saveFactions();
        util.actionBar(p, "left " + faction.name);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
