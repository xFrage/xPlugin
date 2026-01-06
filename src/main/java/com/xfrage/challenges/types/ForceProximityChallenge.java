package com.xfrage.challenges.types;

import com.xfrage.Main;
import com.xfrage.challenges.Challenge;
import com.xfrage.listeners.PlayerDeathListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.*;

public class ForceProximityChallenge extends Challenge {

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
                double maxDistFound = 0;

                outer:
                for (int i = 0; i < players.size(); i++) {
                    for (int j = i + 1; j < players.size(); j++) {

                        Player p1 = players.get(i);
                        Player p2 = players.get(j);

                        if (!p1.getWorld().equals(p2.getWorld())) continue;

                        double dist = p1.getLocation().distance(p2.getLocation());

                        if (dist > 20) {
                            failed = true;
                            maxDistFound = Math.max(maxDistFound, dist);
                            break outer; // ⬅️ WICHTIG
                        }
                    }
                }

                if (failed) PlayerDeathListener.failChallenge();

            }
        };
        task.runTaskTimer(Main.getInstance(), 0, 1); // 1 second
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

    private void warnPlayer(Player p1, Player p2) {

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
