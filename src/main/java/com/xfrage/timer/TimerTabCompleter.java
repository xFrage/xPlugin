package com.xfrage.timer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TimerTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("timer")) {
            if (args.length == 1) {
                suggestions.add("resume");
                suggestions.add("pause");
                suggestions.add("set");
                suggestions.add("reset");
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
