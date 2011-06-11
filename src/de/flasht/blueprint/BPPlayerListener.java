package de.flasht.blueprint;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;


public class BPPlayerListener extends PlayerListener {
    private final BlueprintPlugin plugin;

    public BPPlayerListener(BlueprintPlugin instance) {
        plugin = instance;
    }

    //Insert Player related code here
}
