package com.xfrage.timer;

import com.xfrage.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class TimerCommand implements CommandExecutor {

    static Timer timer = Main.getInstance().getTimer();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sendUsage(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "resume" -> timerResume(sender);
            case "pause" -> timerPause(sender);
            case "set" -> timerSet(sender, args);
            case "reset" -> timerReset(sender);
            default -> sendUsage(sender);
        }
        return false;
    }

    public static void timerResume(CommandSender sender) {
        if (timer.isRunning()) sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "timer is already running!");
        timer.setRunning(true);
        sender.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "timer started!");
        for (Player player : Bukkit.getOnlinePlayers()) { // remove all potion effects
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    public static void timerPause(CommandSender sender) {
        if (!timer.isRunning()) {
            sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "timer is not running");
            return;
        }
        timer.setRunning(false);
        TimeAccessor.saveTime(timer.getTime());
        sender.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "timer paused!");
    }

    public static void timerSet(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sendUsage(sender);
            return;
        }
        try {
            Timer timer = Main.getInstance().getTimer();
            sender.sendMessage(Main.getInstance().prefix + "time set to " + args[1]);
            timer.setTime(Integer.parseInt(args[1]));
        } catch (Exception e) {
            sender.sendMessage(Main.getInstance().prefix + "number (time) required");
        }

        sender.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "timer stopped!");
    }

    public static void timerReset(CommandSender sender) {
        timer.setRunning(false);

        timer.setTime(0);
        TimeAccessor.saveTime(timer.getTime());
        sender.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "timer reset!");
    }

    private static void sendUsage(CommandSender sender) {
        sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "missing argument: \n /timer resume, /timer pause, /timer set <Zeit in s>, /timer reset");
    }

}
