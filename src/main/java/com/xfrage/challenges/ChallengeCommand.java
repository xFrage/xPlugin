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
            if (Main.publicChallenges.isEmpty()) {
                sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "there are no challenges enabled right now");
                return false;
            }
            sender.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "currently active challenges: \n " +
                    Main.publicChallenges);
        }

        switch(args[0]) {
            case "disable" -> Main.getInstance().disableAllChallenges();
        }

        return true;
    }
}
