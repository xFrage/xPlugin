package com.xfrage;

import com.xfrage.commands.ChallengeCommand;
import com.xfrage.createWorld.CreateWorldCommand;
import com.xfrage.createWorld.CreateWorldTabCompleter;
import com.xfrage.createWorld.PlayerPortalListener;
import com.xfrage.listeners.PlayerHungerListener;
import com.xfrage.menu.Menu;
import com.xfrage.listeners.EntityDamageListener;
import com.xfrage.listeners.PlayerJoinListener;
import com.xfrage.listeners.PlayerLeaveListener;
import com.xfrage.menu.TimerMenu;
import com.xfrage.timer.TimeAccessor;
import com.xfrage.timer.Timer;
import com.xfrage.timer.TimerCommand;
import com.xfrage.timer.TimerTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    private static Main instance;
    private Timer timer;

    public String prefix = "[xPlugin] ";

    @Override
    public void onEnable() {

        getLogger().info("--------------------");
        getLogger().info("--------------------");
        getLogger().info("xPlugin initialised");
        getLogger().info("--------------------");
        getLogger().info("--------------------");

        registerTimer(); // set time (from timer.txt)

        registerCommands();
        registerTabCompleters();

        registerListeners();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerTimer() {
        timer = new Timer(false, 0);
        timer.setTime(TimeAccessor.getCurrentTime());
    }

    public void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerJoinListener(), this); // xy joined the server!
        pluginManager.registerEvents(new PlayerLeaveListener(), this); // xy left the server!

        pluginManager.registerEvents(new Menu(), this); // menu click cancel + command
        pluginManager.registerEvents(new TimerMenu(), this); // timer menu

        pluginManager.registerEvents(new EntityDamageListener(), this); // no damage if timer paused
        pluginManager.registerEvents(new PlayerHungerListener(), this); // no hunger loss if timer paused

        pluginManager.registerEvents(new PlayerPortalListener(), this); // cw nether/end linking
    }

    public void registerCommands() {
        Objects.requireNonNull(getCommand("timer")).setExecutor(new TimerCommand());
        Objects.requireNonNull(getCommand("challenge")).setExecutor(new ChallengeCommand());
        Objects.requireNonNull(getCommand("menu")).setExecutor(new Menu());
        Objects.requireNonNull(getCommand("createworld")).setExecutor(new CreateWorldCommand());
    }

    public void registerTabCompleters() {
        Objects.requireNonNull(getCommand("timer")).setTabCompleter(new TimerTabCompleter());
        Objects.requireNonNull(getCommand("createworld")).setTabCompleter(new CreateWorldTabCompleter());
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
