package com.bermei.factionspawners.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InviteResponseGUI {
    public static void open(Player p, String factionName) {
        Inventory gui = Bukkit.createInventory(null, 9, factionName + " invite");

        ItemStack join = new ItemStack(Material.WOOL, 1, (short) 5); // Green
        ItemMeta joinMeta = join.getItemMeta();
        joinMeta.setDisplayName("join");
        join.setItemMeta(joinMeta);

        ItemStack del = new ItemStack(Material.WOOL, 1, (short) 14); // Red
        ItemMeta delMeta = del.getItemMeta();
        delMeta.setDisplayName("delete");
        del.setItemMeta(delMeta);

        gui.setItem(0, join);
        gui.setItem(1, del);

        p.openInventory(gui);
    }
}
