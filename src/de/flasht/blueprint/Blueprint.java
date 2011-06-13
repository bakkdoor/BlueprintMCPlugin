package de.flasht.blueprint;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Blueprint {

	protected final static int MAP_HEIGHT = 128;
	protected final static int MAX_BLOCK_COUNT = 4000; // max for default server config

	protected HashMap<Material, Integer> buildingMaterial;
	protected Material[][][] blocks;
	protected int height;
	protected BlueprintPlugin plugin;
	protected Logger log;
	protected boolean buildToTheLeft;
	
	
	public Blueprint(BlueprintPlugin plugin, Location begin, Location end, boolean faceXAxis)
	{		
		// faceXAxis == false => face Z Axis
		
		this.plugin = plugin;
		log = plugin.getServer().getLogger();
		buildingMaterial = new HashMap<Material, Integer>();
		
		int sx = (int)begin.getX();
		int sy = (int)Math.max(begin.getY(), end.getY());
		int sz = (int)begin.getZ();
		int ex = (int)end.getX();
		int ez = (int)end.getZ();
		
		updateBlocks(begin.getWorld(), sx, sy, sz, ex, ez, faceXAxis);
	}
	
	protected void updateBlocks(World world, int sx, int sy, int sz, int ex, int ez, boolean faceXAxis)
	{
		// faceXAxis == false => face Z Axis
		// determine orientation

		int dx = Math.abs(ex-sx);
		int dz = Math.abs(ez-sz);

		boolean decX = ex < sx;
		boolean decZ = ez < sz;
		
		buildToTheLeft = decX ^ decZ ? faceXAxis : !faceXAxis ;
		
		log.info("dx=" + dx +", dz=" + dz);
		
		if(faceXAxis)
		    blocks = new Material[dx+1][MAP_HEIGHT][dz+1];
		else
			blocks = new Material[dz+1][MAP_HEIGHT][dx+1];

		
		log.info("updating blocks..");
		log.info("faceXAxis=" + faceXAxis + ", buildToTheLeft=" + buildToTheLeft);
		
		int blockCount = 0;
		boolean empty = false;
		buildingMaterial.clear();
		
		for(height = 0; !empty && height+sy < 128; height++)
		{
			empty = true;
//			log.info("y=" + height);
			
			for(int x = 0; x <= dx; x++)
			{
				int curX = decX ? sx-x : sx+x;
//				log.info("  x=" + x);
				for(int z = 0; z <= dz; z++)
				{
//					log.info("    z=" + z);
					int curZ = decZ ? sz-z : sz+z;
					Location curLoc = new Location(world, curX, sy+height, curZ);
					Material mat = world.getBlockAt(curLoc).getType();

					int u = faceXAxis? x : z;
					int v = faceXAxis? z : x;
					blocks[u][height][v] = mat;
					
					if(mat != Material.AIR)
					{
						empty = false;
						Integer mc = buildingMaterial.get(mat);
						mc = mc == null ? 0 : mc;
						buildingMaterial.put(mat, v+1);
						blockCount++;
						if(blockCount > MAX_BLOCK_COUNT)
							throw new RuntimeException("too many blocks for blueprint!");
					}
				}
			}
		}
		height--;
		log.info("blueprint updated:");
		
		// print overview of first layer of blueprint
		for(int u = blocks.length-1; u >= 0; u--)
		{
			String line = "[UV-FIELD] ";
			for(int v = 0; v < blocks[u][0].length; v++)
				line += blocks[u][0][v] == Material.AIR ? " " : "X";
			log.info(line);
		}
		
		log.info("    height: " + height);
		log.info("    number of blocks: " +blockCount);
	}
	
	public Map<Material, Integer> placeBlocks(Location loc, BlockFace orientation)
	{
		log.info("placing blocks..");
		
		int sDu = 0;
		int sDv = 0;
		
		boolean mapUtoX = true; // else map U to Y
		
		// transform uv-coordinates to xz
		switch(orientation)
		{
			case NORTH:
				sDu = -1;
				sDv = -1;
				break;
			case SOUTH:
				sDu =  1;
				sDv =  1;
				break;
			case EAST:
				sDu = -1;
				sDv =  1;
				mapUtoX = false;
				break;
			case WEST:
				sDu =  1;
				sDv = -1;
				mapUtoX = false;
				break;
			default:
				log.info("invalid orientation given!");
				throw new RuntimeException("invalid orientation given!");
		}
		if(buildToTheLeft)
			sDv *= -1;
		
		log.info("material to be placed:");
		for(Map.Entry<Material, Integer> entry : buildingMaterial.entrySet())
			log.info("    " + entry.getKey() + ": " + entry.getValue());
		
		log.info("sDu=" + sDu + ", sDv=" + sDv + ", mapUtoX=" + mapUtoX);
		
		World world = loc.getWorld();
		
		boolean blockCreated = true;
		int blockCount = 0;
		HashMap<Material, Integer> result = new HashMap<Material, Integer>();
		
		for(int y = 0; y <= height && blockCreated; y++)
		{
//			log.info("y="+y);
			blockCreated = false;
			
			for(int u = 0; u < blocks.length; u++)
			{
//				log.info("  u="+u);
				for(int v = 0; v < blocks[u][y].length; v++)
				{
//					log.info("    v=" + v);
					Location curLoc;
					int dx, dz;
					
					// further transformation
					if(mapUtoX)
					{
						dx = u*sDu;
						dz = v*sDv;
					}
					else
					{
						dx = v*sDv;
						dz = u*sDu;
					}
					curLoc = new Location(world, loc.getX()+dx, loc.getY()+y, loc.getZ()+dz);

					Block b = world.getBlockAt(curLoc);
					
					Material mat = b.getType();
					if(mat == Material.AIR && blocks[u][y][v] != Material.AIR)
					{
						log.info("setting block: " + blocks[u][y][v]);
						b.setType(blocks[u][y][v]);
						blockCreated = true;
						blockCount++;
						Integer mc = result.get(mat);
						mc = (mc == null) ? 0 : mc;
						result.put(mat,  mc+1);
					}
				}
			}
		}		
		log.info(blockCount + " blocks placed!");
		return result;
	}
}
