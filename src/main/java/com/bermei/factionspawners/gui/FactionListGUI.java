package com.bermei.factionspawners.gui;

import com.bermei.factionspawners.FactionSpawners;
import com.bermei.factionspawners.factions.Faction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class FactionListGUI {
    private final FactionSpawners plugin;
    private final Map<UUID, OfflinePlayer> offlinePlayerCache;

    public FactionListGUI(FactionSpawners plugin) {
        this.plugin = plugin;
        offlinePlayerCache = new HashMap<>();
    }

    public void preloadOfflinePlayersAsync(Collection<UUID> uuids) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for (UUID uuid : uuids) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                player.getName();
                synchronized (offlinePlayerCache) {
                    offlinePlayerCache.put(uuid, player);
                }
            }
        });
    }

    public OfflinePlayer getCachedOfflinePlayer(UUID uuid) {
        return offlinePlayerCache.getOrDefault(uuid, Bukkit.getOfflinePlayer(uuid));
    }

    public void open(Player p, Faction faction) {
        Inventory gui = Bukkit.createInventory(null, 54, "faction members - " + faction.name);

        ItemStack ownerHead = createPlayerHead(faction.owner, "[owner]");
        gui.addItem(ownerHead);

        for (UUID adminUUID : faction.admins) {
            if (adminUUID.equals(faction.owner)) continue;
            ItemStack adminHead = createPlayerHead(adminUUID, "[admin]");
            gui.addItem(adminHead);
        }

        for (UUID memberUUID : faction.members) {
            if (memberUUID.equals(faction.owner) || faction.admins.contains(memberUUID)) continue;
            ItemStack memberHead = createPlayerHead(memberUUID, "");
            gui.addItem(memberHead);
        }

        p.openInventory(gui);
    }

    private ItemStack createPlayerHead(UUID uuid, String role) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            OfflinePlayer offlinePlayer = getCachedOfflinePlayer(uuid);

            meta.setOwningPlayer(offlinePlayer);
            meta.setDisplayName(ChatColor.YELLOW + offlinePlayer.getName());

            if (!role.isEmpty()) {
                meta.setLore(Collections.singletonList(ChatColor.LIGHT_PURPLE + role));
            } else {
                meta.setLore(Collections.emptyList());
            }

            head.setItemMeta(meta);
        }
        return head;
    }
}
