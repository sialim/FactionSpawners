package com.bermei.factionspawners.factions;

import com.bermei.factionspawners.FactionSpawners;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.*;

public class FactionManager {
    private FactionSpawners plugin;
    private final Map<String, Faction> factions;
    private final File file;

    public FactionManager(FactionSpawners plugin) {
        this.plugin = plugin;
        factions = new HashMap<>();

        File folder = new File(plugin.getDataFolder(), "");
        if(!folder.exists()) folder.mkdirs();

        file = new File(folder, "factions.txt");

        if (file.exists()) {
            loadFactions();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().warning("Failed to make factions.json");
            }
        }
    }

    public void loadFactions() {
        factions.clear();
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\|");
                if (parts.length != 4) continue;

                String name = parts[0];
                UUID owner = UUID.fromString(parts[1]);

                List<UUID> admins = new ArrayList<>();
                if (!parts[2].isEmpty()) {
                    for (String adminStr : parts[2].split(",")) {
                        admins.add(UUID.fromString(adminStr));
                    }
                }

                List<UUID> members = new ArrayList<>();
                if (!parts[3].isEmpty()) {
                    for (String memberStr : parts[3].split(",")) {
                        members.add(UUID.fromString(memberStr));
                    }
                }

                factions.put(name, new Faction(name, owner, members, admins));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFactions() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
            for (Faction faction : factions.values()) {
                String name = faction.name;
                String owner = faction.owner.toString();

                String admins = String.join(",", faction.admins.stream()
                        .map(UUID::toString)
                        .toArray(String[]::new));

                String members = String.join(",", faction.members.stream()
                        .map(UUID::toString)
                        .toArray(String[]::new));

                writer.println(name + "|" + owner + "|" + admins + "|" + members);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFaction(Faction faction) {
        factions.put(faction.name, faction);
        saveFactions();
    }

    public Faction getFaction(String name) {
        return factions.get(name);
    }

    public Faction getFactionByMember(UUID uuid) {
        for (Faction faction : factions.values()) {
            if (faction.members.contains(uuid)) return faction;
        }
        return null;
    }

    public Collection<Faction> getAllFactions() {
        return factions.values();
    }
}
