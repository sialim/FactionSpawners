package com.bermei.factionspawners;

import com.bermei.factionspawners.commands.FactionCommand;
import com.bermei.factionspawners.events.ChatToSignListener;
import com.bermei.factionspawners.factions.FactionManager;
import com.bermei.factionspawners.utilities.ActionBarMessages;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FactionSpawners extends JavaPlugin {
    public ChatToSignListener signChat;
    public FactionManager factionManager;
    public ActionBarMessages util;
    public FactionCommand factionCommand;

    @Override
    public void onEnable() {
        signChat = new ChatToSignListener();
        factionManager = new FactionManager(this);
        util = new ActionBarMessages();
        factionCommand = new FactionCommand(this);

        getCommand("f").setExecutor(factionCommand);
        getCommand("f").setTabCompleter(factionCommand);

        Bukkit.getPluginManager().registerEvents(signChat, this);
    }

    @Override
    public void onDisable() {
        factionManager.saveFactions();
    }
}
