package com.xfrage.listeners;

import com.xfrage.challenges.OnlyHotbarChallenge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public boolean onInventoryClick(InventoryClickEvent event) {

        // Only Hotbar Challenge (Barriers nicht aus Inventar clicken)
        if (!OnlyHotbarChallenge.isEnabled()) return false;
        if (event.getSlot() > 8) { // hotbar ausgenommen
            event.setCancelled(true);
        }
        return true;

    }

}
