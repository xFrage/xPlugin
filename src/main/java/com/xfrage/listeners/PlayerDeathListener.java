package com.xfrage.listeners;

import com.xfrage.Main;
import com.xfrage.challenges.ChallengeManager;
import com.xfrage.timer.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public boolean onPlayerDeath(PlayerDeathEvent event) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.setGameMode(GameMode.SPECTATOR);
            }
            Timer timer = Main.getInstance().getTimer();
            timer.setRunning(false);
            Bukkit.broadcastMessage(Main.getInstance().prefix + ChatColor.RED + "Challenge failed!");
            Bukkit.broadcastMessage(Main.getInstance().prefix + ChatColor.RED + "wasted time: " + timer.timeNotation(timer.getTime()));

            ChallengeManager.pauseAllChallenges();

        return true;
    }

}
