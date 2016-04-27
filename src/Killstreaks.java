import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        String killed = event.getEntity().getDisplayName();
        String killer = event.getEntity().getKiller().getDisplayName();

        //event.getDeathMessage(ChatColor.AQUA + killed + ChatColor.GRAY + "was killed by " + ChatColor.AQUA + killer);
    }
}
