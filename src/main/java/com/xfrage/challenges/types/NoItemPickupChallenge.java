package com.xfrage.challenges.types;

import com.xfrage.Main;
import com.xfrage.challenges.Challenge;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class NoItemPickupChallenge extends Challenge implements Listener {


    public NoItemPickupChallenge(String title) {
        super(title);
    }

    @Override
    public void startChallenge() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Override
    public void stopChallenge() {
        EntityPickupItemEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!isEnabled()) return;

        if (event.getEntity() instanceof Player) { // player picked up item
            event.setCancelled(true);
        }
    }

}
