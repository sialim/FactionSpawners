package com.bermei.factionspawners.factions;

import java.util.*;

public class Faction {
    public String name;
    public UUID owner;
    public List<UUID> admins = new ArrayList<>();
    public List<UUID> members = new ArrayList<>();

    public Faction(String name, UUID owner) {
        this.name = name;
        this.owner = owner;
        this.members.add(owner);
    }

    public Faction(String name, UUID owner, List<UUID> members) {
        this.name = name;
        this.owner = owner;
        this.members.add(owner);
        this.members = members;
    }

    public Faction(String name, UUID owner, List<UUID> members, List<UUID> admins) {
        this.name = name;
        this.owner = owner;
        this.members.add(owner);
        this.members = members;
        this.admins = admins;
    }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid);
    }

    public boolean isAdmin(UUID uuid) {
        return uuid.equals(owner) || admins.contains(uuid);
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    public void addAdmin(UUID uuid) {
        if (!uuid.equals(owner)) admins.add(uuid);
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
        admins.remove(uuid);
    }

    public void removeAdmin(UUID uuid) {
        admins.remove(uuid);
    }
}
