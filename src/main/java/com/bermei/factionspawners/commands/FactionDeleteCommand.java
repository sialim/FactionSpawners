package com.bermei.factionspawners.commands;

import com.bermei.factionspawners.factions.Faction;
import com.bermei.factionspawners.utilities.ActionBarMessages;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FactionDeleteCommand implements SubCommand {
    private final FactionCommand commandManager;
    private final ActionBarMessages util;

    public FactionDeleteCommand(FactionCommand commandManager) {
        this.commandManager = commandManager;
        util = commandManager.plugin.util;
    }

    @Override
    public String getName() {
        return "delete";
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

        if (!faction.owner.equals(p.getUniqueId())) {
            util.actionBar(p, "insufficient permission", ChatColor.RED);
            return;
        }

        faction.members.stream()
                .filter(uuid -> !uuid.equals(faction.owner))
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(plr -> util.actionBar(plr, faction.name + " has been deleted", ChatColor.YELLOW));
        commandManager.factionManager.removeFaction(faction.name);
        util.actionBar(p, "faction deleted", ChatColor.GREEN);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
