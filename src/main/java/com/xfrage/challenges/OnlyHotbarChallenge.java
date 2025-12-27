package com.xfrage.challenges;

import com.xfrage.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class OnlyHotbarChallenge {

    private static boolean enabled = false;
    private static final String title = "Only Hotbar";

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        OnlyHotbarChallenge.enabled = enabled;

        if (enabled) { // hau barrier rein
            startChallenge();
        } else { // lösch barrier wieder raus
            stopChallenge();
        }

    }

    public static void startChallenge() {
        Bukkit.broadcastMessage(Main.getInstance().prefix + ChatColor.GREEN + "Only Hotbar Challenge has been enabled!");

        for (Player p : Bukkit.getOnlinePlayers()) {
            for (int i = 9; i <= 35; i++) {
                p.getInventory().setItem(i, new ItemStack(Material.BARRIER));
            }
        }
        Main.publicChallenges.add(title);

    }

    public static void stopChallenge() {
        Bukkit.broadcastMessage(Main.getInstance().prefix + ChatColor.RED + "Only Hotbar Challenge has been disabled!");
        for (Player p : Bukkit.getOnlinePlayers()) {
            for (int i = 9; i <= 35; i++) {
                p.getInventory().setItem(i, new ItemStack(Material.AIR));
            }
        }
        Main.publicChallenges.remove(title);
    }
}
