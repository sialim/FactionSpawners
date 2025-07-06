package com.bermei.factionspawners;

import com.bermei.factionspawners.commands.FactionCommand;
import com.bermei.factionspawners.events.ChatToSignListener;
import com.bermei.factionspawners.events.ClickGUI;
import com.bermei.factionspawners.events.ProtectionListener;
import com.bermei.factionspawners.factions.FactionManager;
import com.bermei.factionspawners.utilities.ActionBarMessages;
import com.bermei.factionspawners.utilities.InviteStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FactionSpawners extends JavaPlugin {
    public ChatToSignListener signChat;
    public FactionManager factionManager;
    public ActionBarMessages util;
    public FactionCommand factionCommand;
    public InviteStorage invites;
    public ClickGUI clickListener;
    public ProtectionListener protectionListener;

    @Override
    public void onEnable() {
        invites = new InviteStorage();
        signChat = new ChatToSignListener();
        factionManager = new FactionManager(this);
        util = new ActionBarMessages();
        factionCommand = new FactionCommand(this);
        clickListener = new ClickGUI(this);
        protectionListener = new ProtectionListener();

        getCommand("f").setExecutor(factionCommand);
        getCommand("f").setTabCompleter(factionCommand);

        Bukkit.getPluginManager().registerEvents(signChat, this);
        Bukkit.getPluginManager().registerEvents(clickListener, this);
        Bukkit.getPluginManager().registerEvents(protectionListener, this);

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        factionManager.saveFactions();
    }
}
