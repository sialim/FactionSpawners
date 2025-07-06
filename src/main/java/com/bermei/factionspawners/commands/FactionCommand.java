package com.bermei.factionspawners.commands;

import com.bermei.factionspawners.FactionSpawners;
import com.bermei.factionspawners.factions.FactionManager;
import org.bukkit.command.*;

import java.util.*;
import java.util.stream.Collectors;

public class FactionCommand implements TabExecutor {
    public FactionSpawners plugin;
    public FactionManager factionManager;
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public FactionCommand(FactionSpawners plugin) {
        this.plugin = plugin;
        factionManager = plugin.factionManager;
        registerSubCommand(new FactionCreateCommand(this));
        registerSubCommand(new FactionInviteCommand(this));
        registerSubCommand(new FactionInvitesCommand(this));
        registerSubCommand(new FactionDeleteCommand(this));
        //registerSubCommand(new FactionRemoveCommand());
        //registerSubCommand(new FactionAdminCommand());
        //registerSubCommand(new FactionUnadminCommand());
    }

    private void registerSubCommand(SubCommand cmd) {
        subCommands.put(cmd.getName().toLowerCase(), cmd);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        String sub = args[0].toLowerCase();
        SubCommand subCommand = subCommands.get(sub);
        if (subCommand == null) {
            return false;
        }

        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        subCommand.execute(sender, newArgs);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            return subCommands.keySet().stream()
                    .filter(cmd -> cmd.startsWith(args[0].toLowerCase()))
                    .sorted()
                    .collect(Collectors.toList());
        }

        SubCommand sub = subCommands.get(args[0].toLowerCase());
        if (sub != null) {
            return sub.tabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
        }

        return Collections.emptyList();
    }
}
