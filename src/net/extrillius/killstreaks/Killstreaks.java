package net.extrillius.killstreaks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by TechBug2012 on 4/26/16.
 */
public class Killstreaks extends JavaPlugin implements Listener {

    public void onEnable() {

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        String killedName = event.getEntity().getDisplayName();
        String killerName = event.getEntity().getKiller().getDisplayName();
        Player killed = event.getEntity();
        Player killer = event.getEntity().getKiller();

        event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + "was killed by " + ChatColor.AQUA + killerName);
        if (killed.getLocation().getY() < 10) {
            killed.setHealthScale(0);
            event.setDeathMessage(ChatColor.AQUA + killedName + ChatColor.GRAY + "fell into the void");
        }
        // check if player as thrown into the void
    }
    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        Player hit = (Player) event.getEntity();
        Player hitter = (Player) event.getDamager();
        String hitName = hit.getDisplayName();
        String hitterName = hitter.getDisplayName();

        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                /*
                if (hit.getHealth() == 0) {
                    I don't know how to do this
                }
                */
            }
        }, 200L);

    }
}
