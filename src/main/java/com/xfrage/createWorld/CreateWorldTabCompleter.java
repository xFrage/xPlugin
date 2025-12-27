package com.xfrage.createWorld;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CreateWorldTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("createworld")) {
            if (args.length == 1) {
                suggestions.add("create");
                suggestions.add("tp");
                suggestions.add("delete");
                suggestions.add("list");
                suggestions.add("current");
                suggestions.add("archive");
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
