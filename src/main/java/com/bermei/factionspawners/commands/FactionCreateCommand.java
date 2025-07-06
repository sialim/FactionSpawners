package com.bermei.factionspawners.commands;

import com.bermei.factionspawners.FactionSpawners;
import com.bermei.factionspawners.factions.Faction;
import com.bermei.factionspawners.utilities.ActionBarMessages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FactionCreateCommand implements SubCommand {
    private final FactionCommand commandManager;
    private final ActionBarMessages util;

    public FactionCreateCommand(FactionCommand commandManager) {
        this.commandManager = commandManager;
        util = commandManager.plugin.util;
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;
        Player p = (Player) sender;

        if (args.length != 1) {
            util.actionBar(p, "/f create <name>");
            return;
        }

        if (commandManager.factionManager.getFactionByMember(p.getUniqueId()) != null) {
            util.actionBar(p, "already in faction", ChatColor.RED);
            return;
        }

        String name = args[0];
        if (commandManager.factionManager.getFaction(name) != null) {
            util.actionBar(p, "faction already exists", ChatColor.RED);
            return;
        }

        Faction faction = new Faction(name, p.getUniqueId());
        commandManager.factionManager.addFaction(faction);
        commandManager.factionManager.saveFactions();
        Bukkit.getOnlinePlayers().forEach(plr -> util.actionBar(plr, "faction " + name + " created", ChatColor.GREEN));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
