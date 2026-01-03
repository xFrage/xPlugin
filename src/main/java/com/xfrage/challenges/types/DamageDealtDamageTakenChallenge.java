package com.xfrage.challenges.types;

import com.xfrage.Main;
import com.xfrage.challenges.Challenge;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageDealtDamageTakenChallenge extends Challenge implements Listener {

    public DamageDealtDamageTakenChallenge(String title) {
        super(title);
    }

    @Override
    public void startChallenge() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Override
    public void stopChallenge() {
        EntityDamageEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        if (!isEnabled()) return;

        Entity source = event.getDamageSource().getCausingEntity();

        if (source == null) return; // e.g. fall damage, fire, cactus, ... are not entities

        if (source instanceof Player) {
            Player player = (Player) source;
            double damage = event.getDamage();
            dealDamage(player, damage);
        }

    }

    private void dealDamage(Player player, double damage) {
        double newHealth = player.getHealth() - damage;

        if (newHealth < 0) {
            player.setHealth(0);
            return;
        }

        player.playSound(player, Sound.ENTITY_PLAYER_HURT, 1.0F, 1.0F);
        player.playHurtAnimation(player.getLocation().getYaw());
        player.setHealth(newHealth);
    }

}

