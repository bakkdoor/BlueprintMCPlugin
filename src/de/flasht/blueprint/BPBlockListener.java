package de.flasht.blueprint;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.block.Chest;


public class BPBlockListener extends BlockListener {
    private final BlueprintPlugin plugin;
    
    private Location startLoc;
    private Blueprint blueprint;

    public BPBlockListener(final BlueprintPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event)
    {
    	Material mat = event.getBlockPlaced().getType();
    	
    	Location loc = event.getBlockPlaced().getLocation();
    	
    	Player p = event.getPlayer();
    	
    	
    	if(mat == Material.TORCH)
    		if(startLoc == null)
    		{
    			startLoc = loc;
    			p.sendMessage("start location set");
    		}
    		else  
    		{
    			p.sendMessage("drawing blueprint..");
    			blueprint = new Blueprint(this.plugin, startLoc, loc);
    			p.sendMessage("done!");
    			startLoc = null;
    		}
    	else if(mat == Material.REDSTONE_TORCH_ON)
    	{
			p.sendMessage("building blueprint..");
    		blueprint.placeBlocks(p, loc);
			p.sendMessage("done!");
    	}
    		
    }
}
