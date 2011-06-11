package de.flasht.blueprint;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BlueprintManager {
	protected static HashMap<Player, Blueprint> currentBlueprints = new HashMap<Player, Blueprint>();
	protected static HashMap<Player, Boolean> inBlueprintMode = new HashMap<Player, Boolean>();
	
	public static void startBlueprintMode(Player player)
	{
		inBlueprintMode.put(player, true);
	}

	public static void endBlueprintMode(Player player)
	{
		inBlueprintMode.remove(player);
	}
	
	public static boolean isPlayerInBlueprintMode(Player player)
	{
		if(inBlueprintMode.containsKey(player))
			return true;
		return false;
	}
	
	public static Blueprint createBlueprint(Player player, BlueprintPlugin plugin, Location begin, Location end)
	{
		Blueprint bp = new Blueprint(plugin, begin, end);
		currentBlueprints.put(player, bp);
		return bp;
	}
	
	public static Blueprint getBlueprint(Player player)
	{
		if(currentBlueprints.containsKey(player))
			return currentBlueprints.get(player);
		return null;
	}
	
	public static void destroyBlueprint(Player player)
	{
		currentBlueprints.remove(player);
	}
	
}
