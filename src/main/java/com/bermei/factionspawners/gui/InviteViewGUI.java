package com.bermei.factionspawners.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.Set;

public class InviteViewGUI {
    public static void open(Player p, Set<String> invites) {
        Inventory gui = Bukkit.createInventory(null, 27, "Faction invites");

        int i = 0;
        for (String factionName : invites) {
            ItemStack item = new ItemStack(Material.EMPTY_MAP);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + factionName);
            meta.setLore(Collections.singletonList("Click to respond"));
            item.setItemMeta(meta);

            gui.setItem(i++, item);
        }

        p.openInventory(gui);
    }
}
