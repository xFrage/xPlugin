package com.xfrage.menu.subMenus;

import com.xfrage.Main;
import com.xfrage.challenges.*;
import com.xfrage.menu.Menu;
import com.xfrage.menu.subMenus.challengeMenus.MaxHealthMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChallengeMenu implements Listener {

    private static final String challengeInvName = "Challenge Menu";

    @EventHandler
    public boolean onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTitle().equals(challengeInvName))) return false;
        Menu.cancelClick(event, challengeInvName);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        switch(slot) {
            case 10 -> player.openInventory(MaxHealthMenu.getInventory(player));
            case 12 -> enableChallenge("Only Hotbar", player);
            case 14 -> enableChallenge("Random Effect", player);
            case 16 -> enableChallenge("Chunk Decay", player);
        }

        return true;
    }

    public void enableChallenge(String name, Player player) {

        if (Main.getInstance().getTimer().isRunning()) {
            player.sendMessage(Main.getInstance().prefix + ChatColor.RED + "challenges can only be enabled/disabled when timer is paused!");
            return;
        }

        Challenge challenge = ChallengeManager.getChallenge(name);

        if (challenge == null) {
            player.sendMessage(Main.getInstance().prefix + ChatColor.RED + "this challenge doesn't exist!");
            return;
        }

        if (!ChallengeManager.getChallengeActive(name)) {
            ChallengeManager.enableChallenge(challenge);
            Bukkit.broadcastMessage(Main.getInstance().prefix + ChatColor.GREEN + challenge.getTitle() + "Challenge has been enabled!");
        } else {
            ChallengeManager.disableChallenge(challenge);
            Bukkit.broadcastMessage(Main.getInstance().prefix + ChatColor.RED + challenge.getTitle() + " Challenge has been disabled!");
        }

    }

    public static Inventory getInventory(Player player) {
        Inventory challengeInv = Bukkit.createInventory(player, 9*6, challengeInvName);

        // init inventory

        challengeInv.setItem(10, Menu.getItem(new ItemStack(Material.APPLE), "max health", "configure maximum player health"));
        challengeInv.setItem(12, Menu.getItem(new ItemStack(Material.BARRIER), "only hotbar", "enable/disable only hotbar challenge"));
        challengeInv.setItem(14, Menu.getItem(new ItemStack(Material.POTION), "random effect", "enable/disable random effect challenge"));
        challengeInv.setItem(16, Menu.getItem(new ItemStack(Material.GRASS_BLOCK), "delete chunk walk", "fully delete chunk from world after player left"));

        return challengeInv;
    }

}
