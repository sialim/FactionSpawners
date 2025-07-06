package com.bermei.factionspawners.commands;

import com.bermei.factionspawners.factions.Faction;
import com.bermei.factionspawners.utilities.ActionBarMessages;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FactionAdminCommand implements SubCommand{
    private final FactionCommand commandManager;
    private final ActionBarMessages util;

    public FactionAdminCommand(FactionCommand commandManager) {
        this.commandManager = commandManager;
        util = commandManager.plugin.util;
    }

    @Override
    public String getName() {
        return "admin";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return;

        Player p = (Player) sender;
        UUID pUUID = p.getUniqueId();

        if (args.length != 1) {
            util.actionBar(p, "/f admin <player>");
            return;
        }

        Faction faction = commandManager.factionManager.getFactionByMember(pUUID);
        if (faction == null) {
            util.actionBar(p, "not in faction", ChatColor.RED);
            return;
        }
        else if (!faction.owner.equals(pUUID)) {
            util.actionBar(p, "insufficient permission", ChatColor.RED);
            return;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            util.actionBar(p, "player invalid or offline", ChatColor.RED);
            return;
        }

        UUID tUUID = t.getUniqueId();

        if (!faction.isMember(tUUID)) {
            util.actionBar(p, "player not in faction", ChatColor.RED);
            return;
        }

        faction.addAdmin(tUUID);
        commandManager.factionManager.saveFactions();
        util.actionBar(p, t.getName() + " is now admin");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) || args.length != 1) return Collections.emptyList();
        Player p = (Player) sender;
        Faction faction = commandManager.factionManager.getFactionByMember(p.getUniqueId());
        if (faction == null) return Collections.emptyList();

        return Bukkit.getOnlinePlayers().stream()
                .filter(t -> faction.isMember(t.getUniqueId()) && !faction.admins.contains(t.getUniqueId()))
                .map(Player::getName)
                .collect(Collectors.toList());
    }
}
