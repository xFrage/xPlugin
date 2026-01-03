package com.xfrage.challenges.types;

import com.xfrage.Main;
import com.xfrage.challenges.Challenge;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkDecayChallenge extends Challenge implements Listener{

    private Chunk lastChunk;

    public ChunkDecayChallenge(String title) {
        super(title);
    }

    @Override
    public void startChallenge() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Override
    public void stopChallenge() {
        PlayerMoveEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!isEnabled()) return;

        Chunk current = event.getTo().getChunk();

        if (lastChunk == null) {
            lastChunk = current;
            return;
        }

        if (!current.equals(lastChunk)) {
            Chunk toDestroy = lastChunk;
            lastChunk = current;
            yeetChunk(toDestroy);
        }

    }

    private void yeetChunk(Chunk chunk) {

        World world = chunk.getWorld();
        int minY = world.getMinHeight();
        int startY = getHighestY(chunk) + 1;

        new BukkitRunnable() {

            int y = startY;

            @Override
            public void run() {

                if (y < minY) {
                    cancel();
                    return;
                }

                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {

                        int worldX = chunk.getX() * 16 + x;
                        int worldZ = chunk.getZ() * 16 + z;

                        Block block = world.getBlockAt(worldX, y, worldZ);
                        if (block.getType() != Material.AIR) {
                            block.setType(Material.AIR, false);
                        }
                    }
                }

                y--;
            }

        }.runTaskTimer(Main.getInstance(), 0, 2);
    }

    public int getHighestY(Chunk chunk) {
        int highestY = 0;
        World world = chunk.getWorld();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int y = world.getHighestBlockAt(chunk.getX() * 16 + x, chunk.getZ() * 16 + z).getY(); // highest y coordinate block in chunk
                if (y > highestY) {
                    highestY = y;
                }
            }
        }

        return highestY;
    }

}
