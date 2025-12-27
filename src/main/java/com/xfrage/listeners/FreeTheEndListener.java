package com.xfrage.listeners;

import com.xfrage.Main;
import com.xfrage.timer.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class FreeTheEndListener implements Listener {

    @EventHandler
    public boolean onWin(PlayerAdvancementDoneEvent event) {
        NamespacedKey key = event.getAdvancement().getKey();
        if (key.getNamespace().equals(NamespacedKey.MINECRAFT) && key.getKey().equals("end/kill_dragon")) {
            Timer timer = Main.getInstance().getTimer();
            timer.setRunning(false);
            Bukkit.broadcastMessage(Main.getInstance().prefix + ChatColor.GREEN + "challenge done!");
            Bukkit.broadcastMessage(Main.getInstance().prefix + ChatColor.GREEN + "final time: " + timer.timeNotation(timer.getTime()));
        };
        return true;
    }

}
