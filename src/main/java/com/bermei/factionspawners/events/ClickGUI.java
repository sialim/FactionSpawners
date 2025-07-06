package com.bermei.factionspawners.events;

import com.bermei.factionspawners.FactionSpawners;
import com.bermei.factionspawners.factions.Faction;
import com.bermei.factionspawners.gui.InviteResponseGUI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ClickGUI implements Listener {
    FactionSpawners plugin;
    public ClickGUI(FactionSpawners plugin) {
        this.plugin = plugin;
    }

    @EventHandler public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        String title = e.getView().getTitle();
        if (title.equals("Faction invites")) {
            e.setCancelled(true);
            String faction = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
            InviteResponseGUI.open(p, faction);
        } else if (title.endsWith("invite")) {
            e.setCancelled(true);
            String faction = ChatColor.stripColor(title).replace(" invite", "");

            if (clicked.getItemMeta().getDisplayName().equals("join")) {
                Faction faction1 = plugin.factionManager.getFaction(faction);
                if (faction1 != null) {
                    faction1.addMember(p.getUniqueId());
                    plugin.invites.removeInvite(p.getUniqueId(), faction);
                    p.closeInventory();
                    plugin.util.actionBar(p, "joined " + faction, ChatColor.GREEN);
                }
            }

            if (clicked.getItemMeta().getDisplayName().equals("delete")) {
                plugin.invites.removeInvite(p.getUniqueId(), faction);
                plugin.util.actionBar(p, "deleted invite from " + faction);
                p.closeInventory();
            }
        }
    }
}
