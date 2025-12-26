package com.xfrage.listeners;

import com.xfrage.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerHungerListener implements Listener {

    @EventHandler
    public void onPlayerHungerEvent(FoodLevelChangeEvent event) {
        if (!Main.getInstance().getTimer().isRunning()) {
            event.setCancelled(true);
        }
    }

}
