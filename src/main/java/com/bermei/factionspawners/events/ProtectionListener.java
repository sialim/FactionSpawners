package com.bermei.factionspawners.events;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.*;

public class ProtectionListener implements Listener {
    private final Set<String> protectedChunks = new HashSet<>();

    public ProtectionListener() {
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
        e.setCancelled(chunk.getX() == 0 && chunk.getZ() == 0 && !e.getPlayer().isOp());
    }

    @EventHandler public void onEntityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        if (e.getCause() == EntityDamageEvent.DamageCause.VOID) return;

        e.setCancelled(isProtectedChunk(e.getEntity().getLocation().getChunk()));
    }

    @EventHandler public void onBlockFromTo(BlockFromToEvent e) {
        if (isProtectedChunk(e.getBlock().getChunk()) || isProtectedChunk(e.getToBlock().getChunk()))
            e.setCancelled(true);
    }
}
