package de.flasht.blueprint;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import de.flasht.blueprint.commands.BlueprintCommand;

/**
 * Sample plugin for Bukkit
 *
 * @author Dinnerbone
 */
public class BlueprintPlugin extends JavaPlugin {
    private final BPPlayerListener playerListener = new BPPlayerListener(this);
    private final BPBlockListener blockListener = new BPBlockListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();

    public void onDisable() {
        System.out.println("Blueprint plugin disabled.");
    }

    public void onEnable() {
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.Normal, this);
        
//        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
//        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
//        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
//        pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener, Priority.Normal, this);

        // Register our commands
        getCommand("blueprint").setExecutor(new BlueprintCommand(this));

        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " enabled.");
    }

    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
}
