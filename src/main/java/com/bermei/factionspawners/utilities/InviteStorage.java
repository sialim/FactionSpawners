package com.bermei.factionspawners.utilities;

import java.util.*;

public class InviteStorage {
    private final Map<UUID, Set<String>> invites = new HashMap<>();

    public void addInvite(UUID p, String faction) {
        invites.computeIfAbsent(p, k -> new HashSet<>()).add(faction);
    }

    public Set<String> getInvites(UUID p) {
        return invites.getOrDefault(p, Collections.emptySet());
    }

    public void removeInvite(UUID p, String faction) {
        Set<String> set = invites.get(p);
        if (set != null) {
            set.remove(faction);
            if (set.isEmpty()) invites.remove(p);
        }
    }

    public boolean hasInvite(UUID p, String faction) {
        return invites.getOrDefault(p, Collections.emptySet()).contains(faction);
    }
}
