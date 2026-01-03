package com.xfrage.challenges;

import com.xfrage.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ChallengeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) { // "/challenge"
            if (ChallengeManager.getActiveChallenges().isEmpty()) {
                sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "there are no challenges enabled right now");
                return false;
            }
            sender.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "currently active challenges:");
            printActiveChallenges(sender);
            return true;
        }

        switch(args[0]) {
            case "disable": {
                ChallengeManager.pauseAllChallenges();
                sender.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "all challenges have been disabled!");
            }
        }

        return true;
    }

    public void printActiveChallenges(CommandSender sender) {
        for (Challenge c : ChallengeManager.getActiveChallenges()) {
            sender.sendMessage(Main.getInstance().prefix + " " + c.getTitle());
        }
    }
}
