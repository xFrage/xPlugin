package com.xfrage.commands;

import com.xfrage.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length > 1) { // command is more than '/ping <player>'
            sendUsage(sender);
            return false;
        }

        if (args.length == 0) { // '/ping'
            if (!(sender instanceof Player)) {
                sendUsage(sender);
                return false;
            }

            // sender is a Player
            Player player = (Player) sender;
            player.sendMessage(Main.getInstance().prefix + player.getPing() + "ms");
        }

        if (args.length == 1) { // '/ping <player>'
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "player " + args[0] + " does not exist!");
                return false;
            }

            sender.sendMessage(Main.getInstance().prefix + p.getName() + " ping: " + p.getPing());
        }

        return false;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "/ping <player>");
    }
}
