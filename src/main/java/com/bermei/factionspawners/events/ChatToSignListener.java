package com.bermei.factionspawners.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

public class ChatToSignListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);

        String m = e.getMessage();
        Player p = e.getPlayer();

        p.getInventory().addItem(new ItemStack(Material.SIGN, 1));
    }
}
