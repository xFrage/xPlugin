package com.xfrage.menu.subMenus;

import com.xfrage.menu.Menu;
import com.xfrage.timer.TimerCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TimerMenu extends Menu implements Listener {

    public static String timerInvName = "Timer Menu";

    @EventHandler
    public static boolean onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(timerInvName)) return false;
        Menu.cancelClick(event, timerInvName);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        switch (slot) {
            case 10 -> {
                TimerCommand.timerResume(player);
                player.closeInventory();
            }
            case 12 -> TimerCommand.timerPause(player);
            case 14 -> {
                player.performCommand("weather clear");
            }
            case 16 -> TimerCommand.timerReset(player);
        }
        return true;
    }

    public static Inventory getInventory(Player player) {
        Inventory timerInv = Bukkit.createInventory(player, 9*3, timerInvName);

        timerInv.setItem(10, Menu.getItem(new ItemStack(Material.LIME_CONCRETE), ChatColor.GREEN + "resume", "")); // /timer resume
        timerInv.setItem(12, Menu.getItem(new ItemStack(Material.LIGHT_BLUE_CONCRETE), ChatColor.BLUE + "pause", "")); // /timer pause
        timerInv.setItem(14, Menu.getItem(new ItemStack(Material.YELLOW_CONCRETE), ChatColor.YELLOW + "set", "")); // /timer set x
        timerInv.setItem(16, Menu.getItem(new ItemStack(Material.RED_CONCRETE), ChatColor.RED + "reset", "")); // /timer reset

        return timerInv;
    }

}
