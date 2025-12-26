package com.xfrage.listeners;

import com.xfrage.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public static void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(Main.getInstance().prefix + ChatColor.GREEN + ChatColor.BOLD + event.getPlayer().getName() + " joined the server!");
    }

}
