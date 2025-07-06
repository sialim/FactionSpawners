package com.bermei.factionspawners.events;

import com.bermei.factionspawners.FactionSpawners;
import com.bermei.factionspawners.factions.Faction;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.*;

public class ProtectionListener implements Listener {
    public FactionSpawners plugin;

    private final Set<String> protectedChunks = new HashSet<>();

    public ProtectionListener(FactionSpawners plugin) {
        this.plugin = plugin;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                protectedChunks.add(x + "," + z);
            }
        }
    }

    private boolean isProtectedChunk(Chunk chunk) {
        return protectedChunks.contains(chunk.getX() + "," + chunk.getZ());
    }

    @EventHandler public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        e.setCancelled(isProtectedChunk(e.getBlockClicked().getChunk()));
    }

    @EventHandler public void onBlockPlace(BlockPlaceEvent e) {
        Chunk chunk = e.getBlock().getChunk();
        if (chunk.getX() == 0 && chunk.getZ() == 0 && !e.getPlayer().isOp()) {
            e.setCancelled(true);
            e.setBuild(false);
        }
    }

    @EventHandler public void onEntityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        if (e.getCause() == EntityDamageEvent.DamageCause.VOID
        || e.getCause() == EntityDamageEvent.DamageCause.FALL
        || e.getCause() == EntityDamageEvent.DamageCause.HOT_FLOOR) return;

        e.setCancelled(isProtectedChunk(e.getEntity().getLocation().getChunk()));
    }

    @EventHandler public void onBlockFromTo(BlockFromToEvent e) {
        if (isProtectedChunk(e.getBlock().getChunk()) || isProtectedChunk(e.getToBlock().getChunk()))
            e.setCancelled(true);
    }

    @EventHandler public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getDamager() instanceof Player)) return;

        Player damaged = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        if (e.getCause() == EntityDamageEvent.DamageCause.VOID) return;

        Faction factionDamaged = plugin.factionManager.getFactionByMember(damaged.getUniqueId());
        Faction factionDamager = plugin.factionManager.getFactionByMember(damager.getUniqueId());

        if (factionDamaged != null && factionDamager != null && factionDamaged.name.equals(factionDamager.name)) {
            boolean pvpEnabled = plugin.factionManager.isFactionPvPEnabled(factionDamaged.name);
            if (!pvpEnabled) {
                e.setCancelled(true);
            }
        }
    }
}
