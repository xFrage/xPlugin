package com.xfrage.createWorld;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CreateWorldTabCompleter implements TabCompleter {

    // /cw tp <world> <everyone>
    //     0   1      2

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("createworld") || command.getName().equalsIgnoreCase("cw")) {
            if (args.length == 1) {
                suggestions.add("create");
                suggestions.add("tp");
                suggestions.add("delete");
                suggestions.add("list");
                suggestions.add("current");
                suggestions.add("archive");
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("tp") ||
                        args[0].equalsIgnoreCase("delete") ||
                        args[0].equalsIgnoreCase("archive")) {
                    for (World w : Bukkit.getWorlds()) {
                        if (!w.getName().contains("_")) suggestions.add(w.getName());
                    }
                }
            }

            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("tp")) {
                    suggestions.add("everyone");
                    for (Player p : Bukkit.getOnlinePlayers())
                        suggestions.add(p.getName());
                }
            }
        }
        return filterSuggestions(args[args.length - 1], suggestions);
    }

    // Methode zur Filterung basierend auf dem eingegebenen Text
    private List<String> filterSuggestions(String input, List<String> suggestions) {
        List<String> filtered = new ArrayList<>();
        for (String suggestion : suggestions) {
            if (suggestion.toLowerCase().startsWith(input.toLowerCase())) {
                filtered.add(suggestion);
            }
        }
        return filtered;
    }
}
