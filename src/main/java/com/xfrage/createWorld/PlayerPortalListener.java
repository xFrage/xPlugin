package com.xfrage.createWorld;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerPortalListener implements Listener {

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        World from = event.getFrom().getWorld();
        Player player = event.getPlayer();

        // Nether Portal
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            World target;

            if (from.getEnvironment() == World.Environment.NORMAL) {
                target = getLinkedNether(from);
            } else if (from.getEnvironment() == World.Environment.NETHER) {
                target = getLinkedOverworld(from);
            } else {
                return;
            }

            if (target == null) return;

            Location to = calculateNetherLocation(event.getFrom(), from, target);
            event.setTo(to);
            Bukkit.getLogger().info("teleported " + player.getName() + " from " + from.getName() + " to " + target.getName());
        }

        // End Portal
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            World target = getLinkedEnd(from);
            if (target == null) return;

            event.setTo(new Location(target, 100, 49, 0));
        }

    }

    public Location calculateNetherLocation(Location from, World fromWorld, World toWorld) {
        double scale = fromWorld.getEnvironment() == World.Environment.NETHER ? 8.0 : 1.0/8.0;
        return new Location(toWorld, from.getX() * scale, from.getY(), from.getZ() * scale, from.getYaw(), from.getPitch());
    }

    public World getLinkedOverworld(World netherWorld) {
        if (netherWorld.getEnvironment() != World.Environment.NETHER) return null;

        String netherName = netherWorld.getName();
        if (!netherName.endsWith("_nether")) return null;

        String overworldName = netherName.replace("_nether", "");
        return Bukkit.getWorld(overworldName);
    }

    public World getLinkedNether(World overworld) {
        String name = overworld.getName();
        return Bukkit.getWorld(name + "_nether");
    }

    public World getLinkedEnd(World overworld) {
        String name = overworld.getName();
        return Bukkit.getWorld(name + "_the_end");
    }

}
