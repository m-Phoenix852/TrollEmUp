package com.ThunderKofy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class TrollEmUp extends JavaPlugin implements Listener {
    @Override
    public void onEnable(){
        getLogger().log(Level.INFO, "Thanks for having me as a plugin at your server :)");
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
    }
    public void onDisable(){
        getLogger().log(Level.INFO, "Goodbye!");
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent e) {
        if(!getConfig().getBoolean("trolls.burn.enabled")) return;
        Player player = e.getPlayer();
        Random rand = new Random();
        int chance = rand.nextInt(11);
        if(chance > 3 && chance < 5) {
            int time = getConfig().getInt("trolls.burn.seconds") * 20;
            player.setFireTicks(time);
        }

    }
    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if(!getConfig().getBoolean("trolls.take-my-fish.enabled")) return;
        Player player = e.getPlayer();
        Location location = player.getLocation();
        Random rand = new Random();
        int chance = rand.nextInt(11);
        if(chance > 3 && chance < 5) {

            if (e.isCancelled()) return;
            if (e.getCaught() instanceof Fish) {
                location.getWorld().spawnEntity(location, EntityType.LIGHTNING);
            }
        }
    }
    @EventHandler
    public void onKill(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        Random rand = new Random();
        if(!getConfig().getBoolean("creeper-revenge.enabled")) return;
        int chance = rand.nextInt(11);

            if (entity.getType() == EntityType.CREEPER) {
                if (entity.getKiller() instanceof Player) {
                    Player killer = e.getEntity().getKiller();
                    Location killerloc = killer.getLocation();
                    if (chance > 3 && chance < 5) {
                    killerloc.getWorld().spawnEntity(killerloc, EntityType.CREEPER);
                }
            }
        }
    }



    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("trollemup")) {
            if (args.length == 1) {
                List<String> tabCompleteList = new ArrayList<String>();
                tabCompleteList.add("author");
                tabCompleteList.add("reload");
                tabCompleteList.add("trolls");
                tabCompleteList.add("help");
                return tabCompleteList;
            }
        }
        return null;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("trollemup")) {
            if(args.length == 0) {
                if(!sender.hasPermission("TrollEmUp.main")) return true;
                sender.sendMessage(ChatColor.GREEN + "TrollEmUp from ThunderKofy");
                sender.sendMessage("Version: " + ChatColor.YELLOW + getDescription().getVersion());
                sender.sendMessage("Developed by " + ChatColor.YELLOW + "Phoenix852 | ThunderKofy");
                sender.sendMessage("If you have any issues regarding this plugin, join our discord: " + ChatColor.BLUE + getDescription().getWebsite());
                return true;
            }
            if(args[0].equalsIgnoreCase("reload")){
                if(!sender.hasPermission("TrollEmUp.reload")) return true;
                saveDefaultConfig();
                reloadConfig();
                sender.sendMessage("[TrollEmUp] Config reloaded!");
                return true;
            } else if(args[0].equalsIgnoreCase("trolls")) {
                if(!sender.hasPermission("TrollEmUp.trolls")) return true;
                sender.sendMessage(ChatColor.AQUA + "burn: " + ChatColor.WHITE + getConfig().getBoolean("trolls.burn.enabled"));
                sender.sendMessage(ChatColor.AQUA + "creeper-revenge: " + ChatColor.WHITE + getConfig().getBoolean("trolls.creeper-revenge.enabled"));
                sender.sendMessage(ChatColor.AQUA + "take-my-fish: " + ChatColor.WHITE + getConfig().getBoolean("trolls.take-my-fish.enabled"));
            } else if(args[0].equalsIgnoreCase("author")) {
                if(!sender.hasPermission("TrollEmUp.author")) return true;
                sender.sendMessage("Developed by " + ChatColor.YELLOW + "Phoenix852");
                return true;
            } else if (args[0].equalsIgnoreCase("help")) {
                if(!sender.hasPermission("TrollEmUp.help")) return true;
                sender.sendMessage(ChatColor.GREEN + "/TrollEmUp" + ChatColor.WHITE + " --> Shows the description of TrollEmUp.");
                sender.sendMessage(ChatColor.GREEN + "/TrollEmUp reload" + ChatColor.WHITE + " --> Reloads the configuration file.");
                sender.sendMessage(ChatColor.GREEN + "/TrollEmUp trolls" + ChatColor.WHITE + " --> Shows all the enabled and disabled trolls.");
                sender.sendMessage(ChatColor.GREEN + "/TrollEmUp author" + ChatColor.WHITE + " --> Shows the author description.");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "[ERROR] Unknown command!");
                sender.sendMessage("To see the list of commands and their description, use the " + ChatColor.YELLOW + "/TrollEmUp help" + ChatColor.WHITE + " command!");
                return true;
            }
        }
        return true;
    }
}
