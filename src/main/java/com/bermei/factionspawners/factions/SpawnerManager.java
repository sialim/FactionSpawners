package com.bermei.factionspawners.factions;

import com.bermei.factionspawners.FactionSpawners;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.*;
import java.util.*;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class SpawnerManager implements Listener {
    private FactionSpawners plugin;
    private final File file;
    private final Map<Location, String> spawnerMap;
    private final Map<String, List<Location>> chunkToSpawners;

    public SpawnerManager(FactionSpawners plugin) {
        this.plugin = plugin;
        spawnerMap = new HashMap<>();
        chunkToSpawners = new HashMap<>();

        File folder = new File(plugin.getDataFolder(), "");
        if(!folder.exists()) folder.mkdirs();

        file = new File(folder, "spawners.txt");

        if (file.exists()) {
            loadSpawners();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().warning("Failed to make spawners.txt");
            }
        }
    }

    public void loadSpawners() {
        spawnerMap.clear();
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 5) continue;

                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                int z = Integer.parseInt(parts[2]);
                World world = Bukkit.getWorld(parts[3]);
                if (world == null) continue;

                Location loc = new Location(world, x, y, z);
                spawnerMap.put(loc, parts[4]);

                String chunkKey = chunkKey(loc);
                chunkToSpawners.computeIfAbsent(chunkKey, k -> new ArrayList<>()).add(loc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSpawners() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Map.Entry<Location, String> entry : spawnerMap.entrySet()) {
                Location loc = entry.getKey();
                String line = loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "," +
                        loc.getWorld().getName() + "," + entry.getValue();
                writer.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String chunkKey(Chunk chunk) {
        return chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
    }

    private String chunkKey(Location loc) {
        Chunk chunk = loc.getChunk();
        return chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
    }
    
    public void addSpawner(Location loc, String faction) {
        Location blockLoc = loc.toBlockLocation();
        spawnerMap.put(blockLoc, faction);
        chunkToSpawners.computeIfAbsent(chunkKey(blockLoc), k -> new ArrayList<>()).add(blockLoc);
        saveSpawners();
    }

    public void removeSpawner(Location loc) {
        Location blockLoc = loc.toBlockLocation();
        spawnerMap.remove(blockLoc);
        List<Location> list = chunkToSpawners.get(chunkKey(blockLoc));
        if (list != null) {
            list.remove(blockLoc);
            if (list.isEmpty()) {
                chunkToSpawners.remove(chunkKey(blockLoc));
            }
        }
        saveSpawners();
    }
    
    public String getFactionFromSpawner(Location loc) {
        return spawnerMap.get(loc);
    }

    public boolean isFactionSpawner(Location loc) {
        return spawnerMap.containsKey(loc);
    }

    @EventHandler public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getBlock().getType() != Material.MOB_SPAWNER) return;

        Player p = e.getPlayer();
        Faction faction = plugin.factionManager.getFactionByMember(p.getUniqueId());
        if (faction == null) return;

        addSpawner(e.getBlock().getLocation(), faction.name);
        plugin.util.actionBar(p, "placed faction spawner", ChatColor.LIGHT_PURPLE);
    }

    @EventHandler public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() != Material.MOB_SPAWNER) return;

        Location loc = e.getBlock().getLocation();
        if (isFactionSpawner(loc)) {
            removeSpawner(loc);
            plugin.util.actionBar(e.getPlayer(), "broke faction spawner", ChatColor.LIGHT_PURPLE);
        }
    }

    @EventHandler public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER) return;

        Location loc = e.getLocation();
        if (loc == null || loc.getWorld() == null) return;

        String closestFaction = null;
        double closestDist = Double.MAX_VALUE;

        int cx = loc.getChunk().getX();
        int cz = loc.getChunk().getZ();
        String worldName = loc.getWorld().getName();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                String key = worldName + "," + (cx + dx) + "," + (cz + dz);
                List<Location> spawners = chunkToSpawners.get(key);
                if (spawners == null) continue;

                for (Location spawner : spawners) {
                    double dist = spawner.distance(loc);
                    if (dist <= 4 && dist < closestDist) {
                        closestDist = dist;
                        closestFaction = spawnerMap.get(spawner);
                    }
                }
            }
        }

        if (closestFaction != null) {
            e.getEntity().setMetadata("faction", new FixedMetadataValue(plugin, closestFaction));
        }
    }

    @EventHandler public void onEntityTarget(EntityTargetLivingEntityEvent e) {
        if (!(e.getTarget() instanceof Player)) return;
        if (!e.getEntity().hasMetadata("faction")) return;

        String faction = e.getEntity().getMetadata("faction").get(0).asString();
        Player target = (Player) e.getTarget();

        Faction faction1 = plugin.factionManager.getFactionByMember(target.getUniqueId());
        if (faction1 != null && faction1.name.equals(faction)) {
            e.setCancelled(true);
        }
    }
}
