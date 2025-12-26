package com.xfrage.menu;

import com.xfrage.Main;
import com.xfrage.timer.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Menu implements Listener, CommandExecutor {

    private static String invName = "menu";
    private static Timer timer = Main.getInstance().getTimer();

    @EventHandler
    public static boolean onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(invName)) {
            event.setCancelled(true);
        }

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        switch(slot) {
            case 11 -> player.openInventory(TimerMenu.getInventory(player)); // clicked on Timer
            case 15 -> handleReload(player); // clicked on Reload Server
        }

        return true;
    }

    public static void handleReload(Player player) {
        player.closeInventory();
        Bukkit.reload();
        player.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "reload complete!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getInstance().prefix + "only players can execute this command!");
            return true;
        }

        Player player = (Player) sender;
        Inventory inv = Bukkit.createInventory(player, 9*3, invName);
        player.openInventory(inv);

        inv.setItem(11, getItem(new ItemStack(Material.CLOCK), ChatColor.GOLD + "timer", "configure timer"));
        inv.setItem(15, getItem(new ItemStack(Material.REDSTONE), ChatColor.RED + "reload server", ""));

        return true;
    }

    public static ItemStack getItem(ItemStack item, String name, String ... lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        List<String> lores = new ArrayList<>(Arrays.asList(lore));
        meta.setLore(lores);

        item.setItemMeta(meta);
        return item;
    }

}
