package com.bermei.factionspawners.commands;

import com.bermei.factionspawners.factions.Faction;
import com.bermei.factionspawners.utilities.ActionBarMessages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FactionInviteCommand implements SubCommand {
    private final FactionCommand commandManager;
    private final ActionBarMessages util;

    public FactionInviteCommand(FactionCommand commandManager) {
        this.commandManager = commandManager;
        util = commandManager.plugin.util;
    }

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;
        Player p = (Player) sender;

        if (args.length != 1) {
            util.actionBar(p, "/f invite <player>");
            return;
        }

        Faction faction = commandManager.factionManager.getFactionByMember(p.getUniqueId());
        if (faction == null || !faction.isAdmin(p.getUniqueId())) {
            util.actionBar(p, "insufficient permission", ChatColor.RED);
            return;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            util.actionBar(p, "player invalid or offline", ChatColor.RED);
        }

        UUID tUUID = t.getUniqueId();
        if (faction.isMember(tUUID)) {
            util.actionBar(p, "player already in faction", ChatColor.RED);
            return;
        }

        commandManager.plugin.invites.addInvite(tUUID, faction.name);
        util.actionBar(p, "invite sent to " + t.getName(), ChatColor.GREEN);
        util.actionBar(t, "you have invites! /f invites", ChatColor.YELLOW);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length != 1 || !(sender instanceof Player)) return Collections.emptyList();
        Player p = (Player) sender;

        Faction faction = commandManager.factionManager.getFactionByMember(p.getUniqueId());
        if (faction == null) return Collections.emptyList();

        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> !faction.isMember(p.getUniqueId()))
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
    }
}
