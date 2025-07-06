package com.bermei.factionspawners.events;

import com.bermei.factionspawners.FactionSpawners;
import com.bermei.factionspawners.utilities.ActionBarMessages;
import jdk.nashorn.internal.ir.CatchNode;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinMessageModifier implements Listener {
    private FactionSpawners plugin;
    private ActionBarMessages util;

    public JoinMessageModifier(FactionSpawners plugin) {
        this.plugin = plugin;
        util = plugin.util;
    }

    @EventHandler public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage(null);
        for (Player plr : Bukkit.getOnlinePlayers()) {
            util.actionBar(plr, "--> " + p.getName() + " has joined the server", ChatColor.YELLOW);
        }
    }

    @EventHandler public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        e.setQuitMessage(null);
        for (Player plr : Bukkit.getOnlinePlayers()) {
            util.actionBar(plr, "--> " + p.getName() + " has left the server", ChatColor.YELLOW);
        }
    }

    @EventHandler public void onPlayerDeath(PlayerDeathEvent e) {
        String msg = e.getDeathMessage();
        e.setDeathMessage(null);
        for (Player p : Bukkit.getOnlinePlayers()) {
            util.actionBar(p, msg);
        }
    }
}
