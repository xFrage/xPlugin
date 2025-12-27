package com.xfrage.createWorld;

import com.xfrage.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class CreateWorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length < 1) { // "/cw "
            sendUsage(sender);
            return false;
        }

        switch(args[0]) {
            case "create": // create world set
                createWorlds(args[1], sender);
                break;
            case "tp": // teleport all players into world set
                tpWorld(args[1], sender);
                break;
            case "delete": // delete world set
                deleteWorlds(args[1], sender);
                break;
            case "list": // list all active worlds
                listWorlds(sender);
                break;
            case "current": // show current world
                currentWorld(sender);
                break;
            case "archive": // archive world set
                archive(sender, args[1]);
                break;
            default:
                sendUsage(sender);
                break;
        }

        return false;
    }

    public void createWorlds(String baseName, CommandSender sender) {
        if (Bukkit.getWorld(baseName) != null) {
            sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + baseName + " already exists");
            return;
        }

        sender.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "world-set " + baseName + " is being created...");

        WorldCreator world = new WorldCreator(baseName);
        WorldCreator nether = new WorldCreator(baseName + "_nether");
        WorldCreator the_end = new WorldCreator(baseName + "_the_end");

        world.environment(World.Environment.NORMAL);
        nether.environment(World.Environment.NETHER);
        the_end.environment(World.Environment.THE_END);

        world.type(WorldType.NORMAL);

        world.createWorld();
        nether.createWorld();
        the_end.createWorld();

        sender.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "world-set " + baseName + " has been created!");
    }

    public void tpWorld(String baseName, CommandSender sender) {
        if (Bukkit.getWorld(baseName) == null) {
            sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "world-set " + baseName + " does not exist! try /cw list");
            return;
        }
        int x = (int) Objects.requireNonNull(Bukkit.getWorld(baseName)).getSpawnLocation().getX();
        int z = (int) Objects.requireNonNull(Bukkit.getWorld(baseName)).getSpawnLocation().getZ();
        double y = Objects.requireNonNull(Bukkit.getWorld(baseName)).getHighestBlockYAt(x, z) + 1;
        Location targetLoc = new Location(Bukkit.getWorld(baseName), x, y, z);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(targetLoc);
        }
        Bukkit.broadcastMessage(Main.getInstance().prefix + ChatColor.GREEN + "all players have been teleported to world " + baseName + "!");
    }

    public void deleteWorlds(String baseName, CommandSender sender) {
        if (Bukkit.getWorld(baseName) == null) {
            sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "world-set " + baseName + " does not exist! try /cw list");
            return;
        }
        if (baseName.equals("world") || baseName.equals("world_nether") || baseName.equals("world_the_end")) {
            sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "you cannot delete the main world!");
            return;
        }

        if (!Objects.requireNonNull(Bukkit.getWorld(baseName)).getPlayers().isEmpty()) { // es sind noch spieler in der welt
            sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "there are still players in this world! try /cw tp world");
            return;
        }

        Bukkit.unloadWorld(baseName, false);
        Bukkit.unloadWorld(baseName + "_nether", false);
        Bukkit.unloadWorld(baseName + "_the_end", false);

        deleteFolder(new File(Bukkit.getWorldContainer(), baseName ));
        deleteFolder(new File(Bukkit.getWorldContainer(), baseName + "_nether"));
        deleteFolder(new File(Bukkit.getWorldContainer(), baseName + "_the_end"));

        sender.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "world-set " + baseName + " successfully deleted!");
    }

    public void listWorlds(CommandSender sender) {
        sender.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "currently active worlds:");
        for (World world : Bukkit.getWorlds()) {
            sender.sendMessage(world.getName());
        }
    }

    public void currentWorld(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "only players can execute this command!");
            return;
        }

        sender.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "you are currently in " + ((Player) sender).getWorld().getName());
    }

    public void archive(CommandSender sender, String baseName) {
        File archiveDir = createArchiveDir(baseName);
        try {
            if (!totalIsEmpty(baseName)) {
                sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "there are still players in this world! try /cw tp world");
                return;
            }
            archiveWorld(sender, baseName, archiveDir);
            archiveWorld(sender, baseName + "_nether", archiveDir);
            archiveWorld(sender, baseName + "_the_end", archiveDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sender.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "world-set " + baseName + " has been archived successfully!");
    }

    public void archiveWorld(CommandSender sender, String baseName, File archiveDir) throws IOException {
        World world = Bukkit.getWorld(baseName);

        if (world == null) {
            sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + baseName + " does not exist! try /cw list");
            return;
        }

        if (!world.getPlayers().isEmpty()) {
            sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "there are still players in this world! try /cw tp world");
            return;
        }

        Bukkit.unloadWorld(baseName, false);

        File worldFolder = world.getWorldFolder();
        File target = new File(archiveDir, worldFolder.getName());

        Files.move(worldFolder.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public File createArchiveDir(String baseName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH.mm"));
        File dir = new File("archives/" + baseName + "_" + timestamp);
        dir.mkdirs();
        return dir;
    }

    public boolean totalIsEmpty(String baseName) {
        if (Objects.requireNonNull(Bukkit.getWorld(baseName)).getPlayers().isEmpty()) { // overworld empty
            if (Objects.requireNonNull(Bukkit.getWorld(baseName + "_nether")).getPlayers().isEmpty()
                && Objects.requireNonNull(Bukkit.getWorld(baseName + "_the_end")).getPlayers().isEmpty()) { // end und nether empty
                return true;
            }
        }
        return false;
    }

    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "/cw create <worldName>, /cw tp <worldName>, /cw delete <worldName>, /cw list, /cw current");
    }

    private void deleteFolder(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteFolder(f);
                    } else {
                        f.delete();
                    }
                }
            }
            path.delete();
        }
    }
}
