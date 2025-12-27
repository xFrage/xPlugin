package com.xfrage.menu.subMenus.challengeMenus;

import com.xfrage.Main;
import com.xfrage.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class MaxHealthMenu implements Listener {

    private static String maxHealthMenuInvName = "Max Health Menu";

    @EventHandler
    public boolean onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(maxHealthMenuInvName)) return false;
        Menu.cancelClick(event, maxHealthMenuInvName);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        switch(slot) {
            case 12: {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    reduceMaxHealth(p);
                }
                break;
            }
            case 14: {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    increaseMaxHealth(p);

                }
            }
        }


        return true;
    }

    public void reduceMaxHealth(Player player) {
        int currentMaxHealth = (int) Objects.requireNonNull(player.getAttribute(Attribute.MAX_HEALTH)).getValue();
        switch (currentMaxHealth) {
            case 40 -> player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(20); // 10 hearts
            case 20 -> player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(10); // 5 hearts
            case 10 -> player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(6); // 3 hearts
            case 6 -> player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(2); // 1 heart
            case 2 -> player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(1); // 0.5 hearts
            case 1 -> {
                player.sendMessage(Main.getInstance().prefix + ChatColor.RED + "minimum health reached!");
                return;
            }
        }
        player.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "set max health to " + (int) player.getAttribute(Attribute.MAX_HEALTH).getBaseValue());
    }

    public void increaseMaxHealth(Player player) {
        int currentMaxHealth = (int) Objects.requireNonNull(player.getAttribute(Attribute.MAX_HEALTH)).getValue();
        switch (currentMaxHealth) {
            case 1 -> setNewHealth(player, 2); // 1 heart
            case 2 -> setNewHealth(player, 6); // 3 hearts
            case 6 -> setNewHealth(player, 10); // 5 hearts
            case 10 -> setNewHealth(player, 20); // 10 hearts
            case 20 -> setNewHealth(player, 40); // 20 hearts
            case 40 -> {
                player.sendMessage(Main.getInstance().prefix + ChatColor.RED + "maximum health reached!");
                return;
            }
        }
        player.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "set max health to " + (int) player.getAttribute(Attribute.MAX_HEALTH).getBaseValue());
    }

    public static void setNewHealth(Player player, int maxHealth) {
        player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(maxHealth);
        player.setHealth(maxHealth);
    }



    public static Inventory getInventory(Player player) {
        Inventory inv = Bukkit.createInventory(player, 9*3, "Max Health Menu");

        // init menu
        inv.setItem(12, Menu.getItem(new ItemStack(Material.RED_CONCRETE), "REDUCE", "10 -> 5 -> 3 -> 1 -> 0.5"));
        inv.setItem(14, Menu.getItem(new ItemStack(Material.GREEN_CONCRETE), "INCREASE", "10 -> 20"));

        return inv;
    }

}
