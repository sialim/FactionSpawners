package com.bermei.factionspawners.commands;

import com.bermei.factionspawners.factions.Faction;
import com.bermei.factionspawners.utilities.ActionBarMessages;
import com.sun.org.apache.regexp.internal.RE;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class FactionRemoveCommand implements SubCommand{
    private final FactionCommand commandManager;
    private final ActionBarMessages util;

    public FactionRemoveCommand(FactionCommand commandManager) {
        this.commandManager = commandManager;
        util = commandManager.plugin.util;
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;
        Player p = (Player) sender;

        if (args.length != 1) {
            util.actionBar(p, "/f remove <player>");
            return;
        }

        UUID pUUID = p.getUniqueId();
        Faction faction = commandManager.factionManager.getFactionByMember(pUUID);
        if (faction == null) {
            util.actionBar(p, "not in faction", ChatColor.RED);
            return;
        }

        else if (!faction.isAdmin(pUUID)) {
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

        else if (tUUID.equals(pUUID)) {
            util.actionBar(p, "cannot remove yourself", ChatColor.RED);
            return;
        }

        else if (tUUID.equals(faction.owner)) {
            util.actionBar(p, "cannot remove faction owner", ChatColor.RED);
            return;
        }

        faction.removeMember(tUUID);
        commandManager.factionManager.saveFactions();
        faction.members.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(plr -> util.actionBar(plr,
                        p.getName() + " has been removed from the faction"));
        util.actionBar(p, "removed " + t.getName(), ChatColor.GREEN);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) || args.length != 1) return Collections.emptyList();
        Player p = (Player) sender;
        Faction faction = commandManager.factionManager.getFactionByMember(p.getUniqueId());
        if (faction == null) return Collections.emptyList();

        return Bukkit.getOnlinePlayers().stream()
                .filter(t -> faction.isMember(t.getUniqueId()) && !t.getUniqueId().equals(faction.owner))
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
    }
}
