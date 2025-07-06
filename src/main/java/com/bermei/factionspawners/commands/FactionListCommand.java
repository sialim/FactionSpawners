package com.bermei.factionspawners.commands;

import com.bermei.factionspawners.factions.Faction;
import com.bermei.factionspawners.gui.FactionListGUI;
import com.bermei.factionspawners.utilities.ActionBarMessages;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class FactionListCommand implements SubCommand{
    private final FactionCommand commandManager;
    private final ActionBarMessages util;

    public FactionListCommand(FactionCommand commandManager) {
        this.commandManager = commandManager;
        this.util = commandManager.plugin.util;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;
        Player p = (Player) sender;

        Faction faction = commandManager.factionManager.getFactionByMember(p.getUniqueId());
        if (faction == null) {
            util.actionBar(p, "not in faction", ChatColor.RED);
            return;
        }

        commandManager.plugin.listGUI.open(p, faction);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
