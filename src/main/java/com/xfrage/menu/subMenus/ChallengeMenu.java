package com.xfrage.menu.subMenus;

import com.xfrage.challenges.DamageDealtEqualsResisted;
import com.xfrage.challenges.OnlyHotbarChallenge;
import com.xfrage.challenges.RandomEffectChallenge;
import com.xfrage.menu.Menu;
import com.xfrage.menu.subMenus.challengeMenus.MaxHealthMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
            case 12 -> OnlyHotbarChallenge.setEnabled(!OnlyHotbarChallenge.isEnabled());
            case 14 -> RandomEffectChallenge.setEnabled(!RandomEffectChallenge.isEnabled(), 30); // noch menü implementieren
            case 16 -> DamageDealtEqualsResisted.setEnabled(!DamageDealtEqualsResisted.isEnabled());
        }

        return true;
    }

    public static Inventory getInventory(Player player) {
        Inventory challengeInv = Bukkit.createInventory(player, 9*6, challengeInvName);

        // init inventory

        challengeInv.setItem(10, Menu.getItem(new ItemStack(Material.APPLE), "max health", "configure maximum player health"));
        challengeInv.setItem(12, Menu.getItem(new ItemStack(Material.BARRIER), "only hotbar", "enable/disable only hotbar challenge"));
        challengeInv.setItem(14, Menu.getItem(new ItemStack(Material.POTION), "random effect", "enable/disable random effect challenge"));

        return challengeInv;
    }

}
