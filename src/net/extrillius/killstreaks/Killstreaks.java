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

/**
 * Created by TechBug2012 on 4/26/16.
 */
public class Killstreaks extends JavaPlugin implements Listener {
    private HashMap<Integer, String> kills = new HashMap<>();
    int killsNumber = 1;
    int id;
    boolean timer = false;
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
        if (hit instanceof Player && hitter instanceof Player) {
            hit = event.getEntity();
            hitter = event.getDamager();
            hitName = ((Player) hit).getDisplayName();
            hitterName = ((Player) hitter).getDisplayName();
        }
        if (id != 0) {
            getServer().getScheduler().cancelTask(id);
        }

        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                timer = true;
            }
        }, 200L);

    }
    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        killedName = event.getEntity().getDisplayName();
        killerName = event.getEntity().getKiller().getDisplayName();
        killed = event.getEntity();
        killer = event.getEntity().getKiller();

        if (hit.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
            if (timer) {
                getServer().getScheduler().cancelTask(id);
                event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY
                        + " was thrown into the void by " + ChatColor.AQUA + killerName);
            }
            else {
                event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + " fell into the void");
            }
        }
        else if (hit.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (timer) {
                getServer().getScheduler().cancelTask(id);
                event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY
                        + " was thrown off a cliff by " + ChatColor.AQUA + killerName);
            }
            else {
                event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + " fell off a cliff");
            }
        }
        else if (hit.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA) {
            if (timer) {
                getServer().getScheduler().cancelTask(id);
                event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY
                        + " was thrown into a pit of of lava by " + ChatColor.AQUA + killerName);
            }
            else {
                event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + " jumped into lava");
            }
        }
        else if (hit.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE) {
            if (timer) {
                getServer().getScheduler().cancelTask(id);
                event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY
                        + " was burned by " + ChatColor.AQUA + killerName);
            }
            else {
                event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + " burned to death");
            }
        }
        else if (hit.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            if (timer) {
                getServer().getScheduler().cancelTask(id);
                event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY
                        + " was shot by " + ChatColor.AQUA + killerName);
            }
            else {
                event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + " shot themselves");
            }
        }
        else if (hit.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.SUICIDE) {
            event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + " killed themselves.");
        }
        else {
            event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + " was killed by " + ChatColor.AQUA
                    + killerName);
        }

        if (!(kills.containsValue(killerName))) {
            kills.put(killsNumber, killerName);
        }
        if (kills.containsValue(killerName)) {
            kills.remove(killsNumber);
            kills.remove(killerName); // stupid warning here
            killsNumber++;
            kills.put(killsNumber, killerName);
        }
        if (killer.getHealth() == 0) {
            kills.remove(killsNumber);
            kills.remove(killerName); // another stupid warning here
            getServer().broadcastMessage(ChatColor.AQUA + killerName + ChatColor.GREEN + " was shutdown with "
                    + ChatColor.RED + kills.get(killsNumber) + ChatColor.GREEN + " kills!");
        }
        if (kills.get(killsNumber).equals("5")) {
            getServer().broadcastMessage(ChatColor.AQUA + killerName + ChatColor.GREEN + " is on a killstreak!");
        }
        if (kills.get(killsNumber).equals("10")) {
            getServer().broadcastMessage(ChatColor.AQUA + killerName + ChatColor.RED + " is on a rampage!");
        }
        if (kills.get(killsNumber).equals("20")) {
            getServer().broadcastMessage(ChatColor.AQUA + killerName + ChatColor.YELLOW + " is " +
                    ChatColor.UNDERLINE + ChatColor.RED + "UNSTOPPABLE!");
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kills")) {
            if (kills.containsValue(killerName) && killerName.equals(sender.getName())) {
                sender.sendMessage(ChatColor.GRAY + "You have " + ChatColor.GREEN + kills.get(killsNumber)
                        + ChatColor.GRAY + " kills");
            }
            else {
                sender.sendMessage(ChatColor.GRAY + "You don't have any kills yet.");
            }
        }

        return true;
    }
}
