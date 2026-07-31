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
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

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
    public static boolean onPickUp(EntityTargetLivingEntityEvent event) {
        if (!timer.isRunning())
            event.setCancelled(true);
        return true;
    }

    @EventHandler
    public static boolean onDropItem(PlayerDropItemEvent event) {
        if (!timer.isRunning())
            event.setCancelled(true);
        return true;
    }

    @EventHandler
    public static boolean onPickUp(EntityPickupItemEvent event) {
        if (!timer.isRunning())
            event.setCancelled(true);
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
