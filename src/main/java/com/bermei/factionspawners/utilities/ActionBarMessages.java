package com.bermei.factionspawners.utilities;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ActionBarMessages {
    public void actionBar(Player p, String m) {
        p.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(m));
    }
}
