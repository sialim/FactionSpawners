package com.bermei.factionspawners.commands;

import com.bermei.factionspawners.factions.FactionManager;
import com.bermei.factionspawners.gui.InviteViewGUI;
import com.bermei.factionspawners.utilities.ActionBarMessages;
import com.bermei.factionspawners.utilities.InviteStorage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class FactionInvitesCommand implements SubCommand {
    private final FactionCommand commandManager;
    private final ActionBarMessages util;
    private final FactionManager factionManager;
    private final InviteStorage inviteStorage;

    public FactionInvitesCommand(FactionCommand commandManager) {
        this.commandManager = commandManager;
        util = commandManager.plugin.util;
        factionManager = commandManager.factionManager;
        inviteStorage = commandManager.plugin.invites;
    }

    @Override
    public String getName() {
        return "invites";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;
        Player p = (Player) sender;

        Set<String> invites = inviteStorage.getInvites(p.getUniqueId());
        if (invites.isEmpty()) {
            util.actionBar(p, "no invites :(");
            return;
        }

        InviteViewGUI.open(p, invites);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
