package com.xfrage.challenges.types;

import com.xfrage.Main;
import com.xfrage.challenges.Challenge;
import com.xfrage.listeners.PlayerDeathListener;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

import java.util.*;

public class ForceProximityChallenge extends Challenge {

    private double maxDist = 20.0;
    private double warnDist = maxDist * 0.8;

    private BukkitRunnable task;

    public ForceProximityChallenge(String title) {
        super(title);
    }

    // noch zu implementieren: spieler benachrichtigen

    @Override
    public void startChallenge() {
        startDistanceCheck();
    }

    @Override
    public void stopChallenge() {
        stopDistanceCheck();
        resetScoreboards();
    }

    // check distance of every player to every other player every second (20 ticks)
    private void startDistanceCheck() {
        task = new BukkitRunnable() {

            @Override
            public void run() {
                if (!isEnabled()) return;
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

                if (players.size() < 2) return;

                for (Player p : players) {
                    updateScoreboard(p);
                }

                boolean failed = false;

                outer:
                for (int i = 0; i < players.size(); i++) {
                    for (int j = i + 1; j < players.size(); j++) {

                        Player p1 = players.get(i);
                        Player p2 = players.get(j);

                        if (!p1.getWorld().equals(p2.getWorld())) continue;

                        double dist = p1.getLocation().distance(p2.getLocation());

                        if (dist > warnDist) {
                            spawnParticlesBetween(p1, p2);
                            if (dist > maxDist) {
                                failed = true;
                                break outer;
                            }
                        }

                    }
                }

                if (failed) PlayerDeathListener.failChallenge();

            }
        };
        task.runTaskTimer(Main.getInstance(), 0, 1); // once every 50ms (1/20s)
    }

    private void stopDistanceCheck() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    // scoreboard logic
    private void updateScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective(
                "proximity",
                "dummy",
                "Force Proximity"
        );
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Map<Player, Double> distances = getDistances(player);

        distances.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(5)
                .forEach(entry -> {
                    Player p2 = entry.getKey();
                    int distance = entry.getValue().intValue();

                    // evtl. noch farbe implementieren

                    String line = p2.getName() + ": " + distance + "m";

                    obj.getScore(line).setScore(distance);

                });
        player.setScoreboard(board);
    }

    public static void resetScoreboards() {
        Scoreboard main = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(main);
        }
    }

    // particles if dist > 80% max
    private void spawnParticlesBetween(Player p1, Player p2) {

        Location from = p1.getLocation();
        Location to = p2.getLocation();

        Vector direction = to.toVector().subtract(from.toVector());
        double distance = from.distance(to);
        direction.normalize();

        double step = 1.0;
        Vector stepVector = direction.multiply(step);

        Location current = from.clone();

        for (double d = 0; d < distance; d += step) {
            p1.getWorld().spawnParticle(
                    Particle.DUST,
                    current,
                    1,
                    new Particle.DustOptions(Color.RED, 1.2f)
            );
            current.add(stepVector);
        }

    }

    private Map<Player, Double> getDistances(Player player) {
        Map<Player, Double> map = new HashMap<>();

        for (Player p2 : Bukkit.getOnlinePlayers()) {
            if (player.equals(p2)) continue;
            if (!player.getWorld().equals(p2.getWorld())) continue;

            double distance = player.getLocation().distance(p2.getLocation());
            map.put(p2, distance);
        }

        return map;
    }
}
