package com.xfrage.commands;

import com.xfrage.Main;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Iterator;

public class ResetPlayersCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) { // nur "/resetplayers"
            sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "/resetplayers confirm");
            return false;
        }

        if (args[0].equals("confirm")) { // "/resetplayers confirm"
            resetPlayers();
            Bukkit.broadcastMessage(Main.getInstance().prefix + ChatColor.GREEN + "all players have been reset!");
            return true;
        }
        return true;
    }

    public void resetPlayers() {

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setGameMode(GameMode.SURVIVAL);

            // saturation
            p.setFoodLevel(20);
            p.setSaturation(20);

            // level
            p.setLevel(0);
            p.setExp(0);
            p.setTotalExperience(0);

            // inventory
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);

            // advancements
            revokeAllAdvancements(p);

            // stats
            for (Statistic stat : Statistic.values()) {
                try {
                    p.setStatistic(stat, 0);
                } catch (Exception ignored) {}
            }
        }
        Player firstPlayer = (Player) Bukkit.getOnlinePlayers().toArray()[0];
        resetWorld(firstPlayer.getWorld());
    }

    public void resetWorld(World world) {
        world.setTime(0);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setStorm(false);
        world.setThundering(false);
        Bukkit.getLogger().info("world '" + world.getName() + "' has been reset!");
    }

    public void revokeAllAdvancements(Player p) {
        Iterator<Advancement> advancementIterator = Bukkit.advancementIterator();
        while (advancementIterator.hasNext()) {
            Advancement adv = advancementIterator.next();
            AdvancementProgress progress = p.getAdvancementProgress(adv);
            for (String s : progress.getAwardedCriteria()) {
                progress.revokeCriteria(s);
            }
        }
    }

}
