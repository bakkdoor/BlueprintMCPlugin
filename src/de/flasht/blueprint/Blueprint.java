package de.flasht.blueprint;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Blueprint {
	protected Material[][][] blocks;
	protected int sx,sy,sz,ex,ez,dx,dz;
	protected int height;
	protected BlueprintPlugin plugin;
	protected Logger log;
	
	public Blueprint(BlueprintPlugin plugin, Location begin, Location end)
	{		
		this.plugin = plugin;
		this.log = plugin.getServer().getLogger();
		
		sx = (int)Math.min(begin.getX(), end.getX());
		sy = (int)Math.max(begin.getY(), end.getY());
		sz = (int)Math.min(begin.getZ(), end.getZ());
		ex = (int)Math.max(begin.getX(), end.getX());
		ez = (int)Math.max(begin.getZ(), end.getZ());

		dx = Math.abs(ex-sx) +1;
		dz = Math.abs(ez-sz) +1;
		blocks = new Material[dx][128][dz];
		
		updateBlocks(begin.getWorld());
	}
	
	protected void updateBlocks(World world)
	{
		boolean empty = false;
		height = -1;
		
		for(int y = 0; !empty && y+sy < 128; y++)
		{
			empty = true;
			height ++;
			log.info("y="+y);
			
			for(int x = 0; x < dx; x++)
			{
				log.info("x="+x);
				for(int z = 0; z < dz; z++)
				{
					log.info("z="+z);
					Material mat = 
						world.getBlockAt(new Location(world,sx+x,sy+y,sz+z)).getType();
					blocks[x][y][z] = mat;
					empty &= (mat == Material.AIR);
				}
			}
		}
	}
	
	private boolean canBuildAt(World world, double x, double y, double z)
	{
		return world.getBlockAt(new Location(world, x, y, z)).getType() == Material.AIR;
	}
	
	private boolean canPlaceBlocks(World world, Location loc)
	{
		// TODO: Fix this method. Doesn't work correctly yet.
//		for(int x = 0; x < dx; x++)
//		{
//			for(int y = 0; y < height; y++)
//			{
//				for(int z = 0; z < dz; z++)
//				{
//					if(!canBuildAt(world, loc.getX() + x, loc.getY() + y, loc.getZ() + z))
//						return false;
//				}
//			}
//		}
		return true;
	}
	
	public boolean placeBlocks(Player p, Location loc)
	{
		if(canPlaceBlocks(loc.getWorld(), loc))
		{
			p.sendMessage("Starting to build from blueprint...");
			World world = loc.getWorld();
			log.info(dx + ", " + sy + ", " + dz + ", " + height);
			
			for(int y = 0; y < height; y++)
			{
				log.info("y=" + y);
				for(int x = 0; x < dx; x++)
				{
					log.info("x=" + x +", y=" + y);
					for(int z = 0; z < dz; z++)
					{
						log.info("x=" + x +", y=" + y + ", z=" + z);
						world.getBlockAt(new Location(world, loc.getX()+x, loc.getY()+y, loc.getZ()+z))
							.setType(blocks[x][y][z]);
					}
				}
			}
			return true;
		} else {
			p.sendMessage("Can't build from blueprint: Need empty space.");
			return false;
		}
	}
}
