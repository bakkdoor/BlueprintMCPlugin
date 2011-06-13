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
import org.bukkit.util.Vector;
import org.bukkit.block.Chest;


public class BPBlockListener extends BlockListener {
    private final BlueprintPlugin plugin;
    
    private Location startLoc;

    public BPBlockListener(final BlueprintPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event)
    {
    	Material mat = event.getBlockPlaced().getType();
    	Location loc = event.getBlockPlaced().getLocation();
    	Player p = event.getPlayer();
    	
//    	if(BlueprintManager.isPlayerInBlueprintMode(p))
//    	{
    		if(mat == Material.TORCH)
    		{
    			if(startLoc == null)
    			{
    				startLoc = loc;
    				p.sendMessage("Start location set");
    			}
    			else  
    			{
    				p.sendMessage("Drawing blueprint..");
    				BlueprintManager.createBlueprint(event.getPlayer(), this.plugin, this.startLoc, loc);
    				p.sendMessage("done!");
    				startLoc = null;
    			}
    		}
//    	} else {
    		if(mat == Material.REDSTONE_TORCH_ON)
    		{
    			p.sendMessage("Building blueprint..");
    			BlockFace orientation;
    			Vector d = p.getLocation().getDirection();
    			if(Math.abs(d.getX()) > Math.abs(d.getZ()))
    				if(d.getX() > 0)
    					orientation = BlockFace.SOUTH;
    				else
    					orientation = BlockFace.NORTH;
    			else
    				if(d.getZ() > 0)
    					orientation = BlockFace.WEST;
    				else
    					orientation = BlockFace.EAST;
    					
    			p.sendMessage("direction = " + d);
    			p.sendMessage("orientation = " + orientation);
    		
    			BlueprintManager.getBlueprint(p).placeBlocks(loc, orientation);
    		}
//    	}
    }
}
