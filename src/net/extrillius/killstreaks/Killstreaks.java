package net.extrillius.killstreaks;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by TechBug2012 on 4/26/16.
 */
    //

public class Killstreaks extends JavaPlugin implements Listener {
    private HashMap<Integer, String> kills = new HashMap<>();
    Set killSet = kills.entrySet();
    Iterator it = killSet.iterator();
    int killsNumber = 1;
    int id;
    boolean timer;
    Entity hit; // watch out for npe
    Entity hitter; // watch out for npe
    String hitName;
    String hitterName;
    Player killed;
    Player killer;
    String killedName;
    String killerName;

    public void onEnable() {

        getServer().getPluginManager().registerEvents(this, this);
    }
    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
            hit = event.getEntity();
            hitter = event.getDamager();
            hitName = ((Player) hit).getDisplayName();
            hitterName = ((Player) hitter).getDisplayName();
        if (!(hit instanceof Player) && !(hitter instanceof Player))
            return;

        if (id != 0) {
            getServer().getScheduler().cancelTask(id);
        }
        timer = true;

        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                timer = false;
            }
        }, 200L);
    }
    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        killed = event.getEntity();
        killer = event.getEntity().getKiller();
        killedName = killed.getName();
        killerName = killer.getName();


        while (it.hasNext()) {
            Map.Entry mentry = (Map.Entry)it.next();
            if (kills.containsValue(killedName)) {
                it.remove();
                if (timer) { // what's the point of this lol
                    getServer().broadcastMessage(ChatColor.AQUA + killedName + ChatColor.RED + " has been " + ChatColor.RED
                            + ChatColor.BOLD + "SHUTDOWN " + ChatColor.RED + "by " + ChatColor.AQUA + killerName);
                }
                else {
                    getServer().broadcastMessage(ChatColor.AQUA + killedName + ChatColor.RED + " has been " + ChatColor.RED
                            + ChatColor.BOLD + "SHUTDOWN");
                }
            }
        }

        if (hit != null && hit.getLastDamageCause() != null) {
            if (hit.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
                if (timer) {
                    getServer().getScheduler().cancelTask(id);
                    event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY
                            + " was thrown into the void by " + ChatColor.AQUA + killerName);
                    if (!(kills.containsValue(killerName))) {
                        kills.put(killsNumber, killerName);
                    }
                    else {
                        killsNumber++;
                        kills.put(killsNumber, killerName);
                    }
                } else {
                    event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + " fell into the void");

                }
            } else if (hit.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (timer) {
                    getServer().getScheduler().cancelTask(id);
                    event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY
                            + " was thrown off a cliff by " + ChatColor.AQUA + killerName);
                } else {
                    event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + " fell off a cliff");
                }
            } else if (hit.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA) {
                if (timer) {
                    getServer().getScheduler().cancelTask(id);
                    event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY
                            + " was thrown into a pit of of lava by " + ChatColor.AQUA + killerName);
                } else {
                    event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + " jumped into lava");
                }
            } else if (hit.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE) {
                if (timer) {
                    getServer().getScheduler().cancelTask(id);
                    event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY
                            + " was burned by " + ChatColor.AQUA + killerName);
                } else {
                    event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + " burned to death");
                }
            } else if (hit.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                if (timer) {
                    getServer().getScheduler().cancelTask(id);
                    event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY
                            + " was shot by " + ChatColor.AQUA + killerName);
                } else {
                    event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + " shot themselves");
                }
            } else if (hit.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.SUICIDE) {
                event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + " killed themselves.");
            } else {
                event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + " was killed by " + ChatColor.AQUA
                        + killerName);
            }
        }

        if (kills.get(killsNumber).equals("5")) {
            getServer().broadcastMessage(ChatColor.AQUA + killerName + ChatColor.GREEN + " is on a killstreak!");
        }
        if (kills.get(killsNumber).equals("10")) {
            getServer().broadcastMessage(ChatColor.AQUA + killerName + ChatColor.YELLOW + " is on a rampage!");
        }
        if (kills.get(killsNumber).equals("20")) {
            getServer().broadcastMessage(ChatColor.AQUA + killerName + ChatColor.RED + " is " +
                    ChatColor.BOLD + ChatColor.DARK_RED + "UNSTOPPABLE!");
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kills")) {
            if (kills.containsValue(sender.getName())) {
                if (!(kills.get(killsNumber)).equals("1")) {
                    sender.sendMessage(ChatColor.GRAY + "You have " + ChatColor.GREEN + ChatColor.BOLD + kills.get(killsNumber)
                            + ChatColor.GRAY + " kills");
                }
                else {
                    sender.sendMessage(ChatColor.GRAY + "You have " + ChatColor.GREEN + ChatColor.BOLD + kills.get(killsNumber)
                            + ChatColor.GRAY + " kill");
                }
            }
            else {
                sender.sendMessage(ChatColor.GRAY + "You don't have any kills yet.");
            }
        }

        return true;
    }
}