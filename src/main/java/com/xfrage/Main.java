package com.xfrage;

import com.xfrage.challenges.*;
import com.xfrage.challenges.types.*;
import com.xfrage.commands.ResetPlayersCommand;
import com.xfrage.createWorld.CreateWorldCommand;
import com.xfrage.createWorld.CreateWorldTabCompleter;
import com.xfrage.createWorld.PlayerPortalListener;
import com.xfrage.listeners.*;
import com.xfrage.menu.Menu;
import com.xfrage.menu.subMenus.ChallengeMenu;
import com.xfrage.menu.subMenus.TimerMenu;
import com.xfrage.menu.subMenus.challengeMenus.MaxHealthMenu;
import com.xfrage.timer.TimeAccessor;
import com.xfrage.timer.Timer;
import com.xfrage.timer.TimerCommand;
import com.xfrage.timer.TimerTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class Main extends JavaPlugin {

    private static Main instance;
    private Timer timer;
    private RandomEffectChallenge secTimer;

    public String prefix = "[xPlugin] ";

    @Override
    public void onEnable() {

        getLogger().info("--------------------");
        getLogger().info("--------------------");
        getLogger().info("xPlugin initialised");
        getLogger().info("--------------------");
        getLogger().info("--------------------");

        loadWorlds();

        registerTimer(); // set time (from timer.txt)

        registerCommands();
        registerTabCompleters();
        registerListeners();
        registerChallenges();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /*
    Ideen:

    x Sekunden still stehen = damage
    Springen verboten / springen = damage
    verteilter damage = erhaltener damage
    damage verteilen = 50 blöcke nach oben teleportiert

    mobs haben x mal so viel health / machen x mal so viel damage

    chunk wechsel = damage
    chunk/block wechsel = chunk/block wird von -64 bis 320 gelöscht

    randomizer

    nur nach unten

     */

    public void registerChallenges() {
        ChallengeManager.register(new RandomEffectChallenge("Random Effect"));
        ChallengeManager.register(new OnlyHotbarChallenge("Only Hotbar"));
        ChallengeManager.register(new ChunkDecayChallenge("Chunk Decay"));
        ChallengeManager.register(new DamageDealtDamageTakenChallenge("Damage Dealt = Damage Taken"));
        ChallengeManager.register(new NoItemPickupChallenge("No Item Pickup"));

        ChallengeManager.updateChallenges();
    }

    public void registerTimer() {
        TimeAccessor.init(getDataFolder());
        timer = new Timer(false, 0);
        timer.setTime(TimeAccessor.getCurrentTime());
    }

    public void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerJoinListener(), this); // xy joined the server!
        pluginManager.registerEvents(new PlayerLeaveListener(), this); // xy left the server!

        pluginManager.registerEvents(new Menu(), this); // menu click cancel + command
        pluginManager.registerEvents(new TimerMenu(), this); // timer menu
        pluginManager.registerEvents(new ChallengeMenu(), this); // challenge menu
        pluginManager.registerEvents(new MaxHealthMenu(), this); // max health menu

        pluginManager.registerEvents(new PlayerDeathListener(), this); // player death, timer pause
        pluginManager.registerEvents(new FreeTheEndListener(), this); // challenge done

        pluginManager.registerEvents(new EntityDamageListener(), this); // no damage if timer paused
        pluginManager.registerEvents(new PlayerHungerListener(), this); // no hunger loss if timer paused

        pluginManager.registerEvents(new PlayerPortalListener(), this); // cw nether/end linking
    }

    public void registerCommands() {
        Objects.requireNonNull(getCommand("timer")).setExecutor(new TimerCommand());
        Objects.requireNonNull(getCommand("challenge")).setExecutor(new ChallengeCommand());
        Objects.requireNonNull(getCommand("menu")).setExecutor(new Menu());
        Objects.requireNonNull(getCommand("createworld")).setExecutor(new CreateWorldCommand());
        Objects.requireNonNull(getCommand("resetplayers")).setExecutor(new ResetPlayersCommand());
    }

    public void registerTabCompleters() {
        Objects.requireNonNull(getCommand("timer")).setTabCompleter(new TimerTabCompleter());
        Objects.requireNonNull(getCommand("createworld")).setTabCompleter(new CreateWorldTabCompleter());
    }

    public void loadWorlds() {

        File serverDir = Bukkit.getWorldContainer();

        for (File file : Objects.requireNonNull(serverDir.listFiles())) {

            if (!file.isDirectory()) continue;

            if (!new File(file, "level.dat").exists()) continue;

            String worldName = file.getName();

            // Skip default world (die lädt Bukkit selbst)
            if (Bukkit.getWorld(worldName) != null) continue;

            Bukkit.getLogger().info("loading world: " + worldName);

            WorldCreator creator = new WorldCreator(worldName);

            // Umgebung bestimmen
            if (worldName.endsWith("_nether")) {
                creator.environment(World.Environment.NETHER);
            } else if (worldName.endsWith("_the_end")) {
                creator.environment(World.Environment.THE_END);
            } else {
                creator.environment(World.Environment.NORMAL);
            }

            Bukkit.createWorld(creator);
        }

    }


    public static Main getInstance() {
        return instance;
    }

    public void onLoad() {
        instance = this;
    }

    public Timer getTimer() {
        return timer;
    }

}
