package com.xfrage.menu;

import com.xfrage.Main;
import com.xfrage.menu.subMenus.ChallengeMenu;
import com.xfrage.menu.subMenus.TimerMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Menu implements Listener, CommandExecutor {

    private static final String invName = "Menu";

    @EventHandler
    public static boolean onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTitle().equals(invName))) return false;
        Menu.cancelClick(event, invName);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        switch(slot) {
            case 11 -> player.openInventory(TimerMenu.getInventory(player)); // clicked on Timer
            case 13 -> player.openInventory(ChallengeMenu.getInventory(player)); // clicked on Challenge Settings
            case 15 -> handleReload(player); // clicked on Reload Server
        }

        return true;
    }

    // open menu when "menu" compass is clicked (hotbar, any slot)
    @EventHandler
    public void onHotbarClick(PlayerInteractEvent event) {
        ItemStack clickedItem = event.getItem();
        Player player = event.getPlayer();
        if (clickedItem == null) return;
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) { // player clicked item in hotbar, not inventory
            if (Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().equals("menu")) {
                player.performCommand("menu");
            }
        }
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
        inv.setItem(13, getItem(new ItemStack(Material.DIAMOND_SWORD), ChatColor.BLUE + "challenge settings", "configure playing restrictions"));
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

    public static void cancelClick(InventoryClickEvent event, String invName) {
        if (event.getView().getTitle().equals(invName)) {
            event.setCancelled(true);
        }
    }

}
