package com.xfrage.listeners;

import com.xfrage.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public static boolean onDamage(EntityDamageEvent event) {

        if (!Main.getInstance().getTimer().isRunning()) {
            event.setCancelled(true);
        }

        return true;
    }

}
