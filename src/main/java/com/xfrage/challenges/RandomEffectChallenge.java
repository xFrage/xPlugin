package com.xfrage.challenges;

import com.xfrage.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class RandomEffectChallenge extends Challenge{

    private int time = 30;
    private BukkitRunnable task;
    private PotionEffect effect;
    private int amplifier = 0;

    public RandomEffectChallenge(String title) {
        super(title);
    }

    @Override
    public void startChallenge() {
        generateRandomEffect();
        broadcastEffect();
        start(time);
    }

    @Override
    public void stopChallenge() {
        stop();
    }

    public void generateRandomEffect() {
        Random random = new Random();
        int x = random.nextInt(1, 35);
        int amp = random.nextInt(1, 5);
        int dur = 620;

        if (x == 13 && amp == 5) amp = 4; // instant damage 5 wäre instant kill

        switch (x) {
            case 1 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.ABSORPTION, dur, amp, true), amp);
            case 2 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.BAD_OMEN, dur, amp, true), amp);
            case 3 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.BLINDNESS, dur, amp, true), amp);
            case 4 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, dur, amp, true), amp);
            case 5 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.DARKNESS, dur, amp, true), amp);
            case 6 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, dur, amp, true), amp);
            case 7 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, dur, amp, true), amp);
            case 8 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.GLOWING, dur, amp, true), amp);
            case 9 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.HASTE, dur, amp, true), amp);
            case 10 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, dur, amp, true), amp);
            case 11 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, dur, amp, true), amp);
            case 12 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.HUNGER, dur, amp, true), amp);
            case 13 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE, dur, amp, true), amp);
            case 14 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, dur, amp, true), amp);
            case 15 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.INVISIBILITY, dur, amp, true), amp);
            case 16 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, dur, amp, true), amp);
            case 17 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.LEVITATION, dur, amp, true), amp);
            case 18 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.LUCK, dur, amp, true), amp);
            case 19 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, dur, amp, true), amp);
            case 20 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.NAUSEA, dur, amp, true), amp);
            case 21 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, dur, amp, true), amp);
            case 22 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.POISON, dur, amp, true), amp);
            case 23 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.RAID_OMEN, dur, amp, true), amp);
            case 24 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.REGENERATION, dur, amp, true), amp);
            case 25 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.RESISTANCE, dur, amp, true), amp);
            case 26 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.SATURATION, dur, amp, true), amp);
            case 27 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, dur, amp, true), amp);
            case 28 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.SPEED, dur, amp, true), amp);
            case 29 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.STRENGTH, dur, amp, true), amp);
            case 30 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.TRIAL_OMEN, dur, amp, true), amp);
            case 31 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.UNLUCK, dur, amp, true), amp);
            case 32 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, dur, amp, true), amp);
            case 33 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.WEAKNESS, dur, amp, true), amp);
            case 34 -> giveAllPlayersEffect(new PotionEffect(PotionEffectType.POISON, dur, amp, true), amp);
        }

    }

    public void giveAllPlayersEffect(PotionEffect effect, int amp) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.addPotionEffect(effect);
        }
        this.effect = effect;
        this.amplifier = amp;
    }

    private void start(int seconds) {
        stop(); // beendet alten Timer, damit immer maximal einer läuft
        time = seconds;

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!enabled) {
                    cancel();
                    return;
                }

                if (time <= 0) {
                    // HIER: random Effekt ausführen
                    generateRandomEffect();
                    time = seconds; // bei getTime() == 0 -> auf 10 setzen
                    broadcastEffect();
                    return;
                }
                time--;
            }
        };

        task.runTaskTimer(Main.getInstance(), 0, 20);
    }

    public void broadcastEffect() {
        Bukkit.broadcastMessage(Main.getInstance().prefix + ChatColor.GREEN + "next effect: " + effect.getType().getName() + " " + amplifier);
    }

    private void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}
