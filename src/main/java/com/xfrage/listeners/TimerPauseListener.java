package com.xfrage.listeners;

import com.xfrage.Main;
import com.xfrage.timer.Timer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class TimerPauseListener implements Listener {

    static Timer timer = Main.getInstance().getTimer();

    @EventHandler
    public static boolean onDamage(EntityDamageEvent event) {

        if (!timer.isRunning() && event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }

        return true;
    }

    @EventHandler
    public static boolean onBlockBreak(BlockBreakEvent event) {
        if (!timer.isRunning())
            event.setCancelled(true);
        return true;
    }

    @EventHandler
    public static boolean onBlockPlace(BlockPlaceEvent event) {
        if (!timer.isRunning())
            event.setCancelled(true);
        return true;
    }

}
