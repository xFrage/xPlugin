package com.xfrage.challenges.types;

import com.xfrage.Main;
import com.xfrage.challenges.Challenge;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class OnlyHotbarChallenge extends Challenge implements Listener {

    public OnlyHotbarChallenge(String title) {
        super("Only Hotbar");
    }

    @Override
    public void startChallenge() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        for (Player p : Bukkit.getOnlinePlayers()) {
            for (int i = 9; i <= 35; i++) {
                p.getInventory().setItem(i, new ItemStack(Material.BARRIER));
            }
        }

    }

    @Override
    public  void stopChallenge() {
        InventoryClickEvent.getHandlerList().unregister(this);
        for (Player p : Bukkit.getOnlinePlayers()) {
            for (int i = 9; i <= 35; i++) {
                p.getInventory().setItem(i, new ItemStack(Material.AIR));
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isEnabled()) return;

        int slot = event.getSlot();

        // directly inside inventory
        if (event.getClickedInventory() == event.getView().getBottomInventory()) {
            if (isNotHotbarSlot(slot)) {
                event.setCancelled(true);
                return;
            }
        }

    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!isEnabled()) return;

        if (event.getView().getTopInventory().getType() == InventoryType.CRAFTING) {
            for (int rawSlot : event.getRawSlots()) {
                int convertedSlot = event.getView().convertSlot(rawSlot);
                if (isNotHotbarSlot(convertedSlot)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    private boolean isNotHotbarSlot(int slot) {
        return slot < 0 || slot > 8;
    }
}
