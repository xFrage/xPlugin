package com.xfrage.listeners;

import com.xfrage.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public static void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(Main.getInstance().prefix + ChatColor.RED.toString() + ChatColor.BOLD + event.getPlayer().getName() + " left the server!");
    }

}
