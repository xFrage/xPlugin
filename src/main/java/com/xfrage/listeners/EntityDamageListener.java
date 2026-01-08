package com.xfrage.listeners;

import com.xfrage.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public static boolean onDamage(EntityDamageEvent event) {

        if (!Main.getInstance().getTimer().isRunning() && event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }

        return true;
    }

}
