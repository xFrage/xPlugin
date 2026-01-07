package com.xfrage.challenges.types;

import com.xfrage.Main;
import com.xfrage.challenges.Challenge;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class MobHealthChallenge extends Challenge implements Listener {

    private static final double MAX_HEALTH = 2048.0;
    private static final NamespacedKey SCALED_KEY =
            new NamespacedKey(Main.getInstance(), "mob_scaled");
    private static final NamespacedKey ORIGINAL_HEALTH =
            new NamespacedKey(Main.getInstance(), "original_health");

    public MobHealthChallenge(String title) {
        super(title);
    }

    @Override
    public void startChallenge() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        updateAll();
    }

    @Override
    public void stopChallenge() {
        HandlerList.unregisterAll(this);
        resetAllMobs();
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if (isMob(entity)) { // spawning entity is a mob and not a player
            LivingEntity livingEntity = (LivingEntity) entity;
            setNewHealth(livingEntity);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!isMob(event.getEntity())) return;

        LivingEntity entity = (LivingEntity) event.getEntity();

        Bukkit.getScheduler().runTaskLater(
                Main.getInstance(),
                () -> updateName(entity),
                1L
        );
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent event) {
        Entity entity = event.getEntity();
        if (isMob(entity)) {
            LivingEntity livingEntity = (LivingEntity) entity;
            updateName(livingEntity);
        }
    }

    public void setNewHealth(LivingEntity entity) {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        if (data.has(SCALED_KEY, PersistentDataType.BYTE)) return;

        AttributeInstance attr = entity.getAttribute(Attribute.MAX_HEALTH);

        // save original hp
        data.set(ORIGINAL_HEALTH, PersistentDataType.DOUBLE, attr.getBaseValue());

        double newHealth = Math.min(entity.getHealth() * 100, MAX_HEALTH);
        attr.setBaseValue(newHealth);
        entity.setHealth(newHealth);

        data.set(SCALED_KEY, PersistentDataType.BYTE, (byte) 1);
    }

    public void resetAllMobs() {
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                resetMob(entity);
            }
        }
    }

    public void resetMob(LivingEntity entity) {
        if (entity instanceof Player) return;

        PersistentDataContainer data = entity.getPersistentDataContainer();

        if (!data.has(SCALED_KEY, PersistentDataType.BYTE)) return;

        AttributeInstance attr = entity.getAttribute(Attribute.MAX_HEALTH);
        if (attr == null) return;

        Double originalHealth = data.get(ORIGINAL_HEALTH, PersistentDataType.DOUBLE);
        if (originalHealth == null) return;

        // reset max health
        attr.setBaseValue(originalHealth);

        // set entity hp to original health
        double newHealth = Math.min(entity.getHealth(), originalHealth);
        entity.setHealth(newHealth);

        // remove custom name
        entity.setCustomName(null);
        entity.setCustomNameVisible(false);

        // remove markers
        data.remove(SCALED_KEY);
        data.remove(ORIGINAL_HEALTH);
    }

    public void updateAll() {
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity livingEntity : world.getLivingEntities()) {
                if (livingEntity instanceof Player) continue;
                setNewHealth(livingEntity);
                updateName(livingEntity);
            }
        }
    }

    public void updateName(LivingEntity entity) {
        entity.setCustomNameVisible(true);
        entity.setCustomName(String.valueOf(entity.getHealth()));
    }

    public boolean isMob(Entity entity) {
        return (entity instanceof LivingEntity && !(entity instanceof Player));
    }



}
