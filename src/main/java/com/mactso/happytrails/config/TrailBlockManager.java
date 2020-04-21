//1.12.2-2.0

package com.mactso.happytrails.config;

import java.util.Hashtable;
import java.util.StringTokenizer;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;



public class TrailBlockManager {
	
	public static Hashtable<String, TrailBlockItem> trailBlockHashtable = new Hashtable<>();

	public static TrailBlockItem getTrailBlockInfo(String trailKey) {

		if (trailBlockHashtable.isEmpty()) {
			trailBlockInit();
		}
		TrailBlockItem t = trailBlockHashtable.get(trailKey);
		return t;
	}

	public static String updateRemoveTrailBlockInfo(String keyRegistryDomain, int meta, int speed) {
		String result = null;
		if (!Block.REGISTRY.containsKey(new ResourceLocation(keyRegistryDomain)))  {
			result = "Happy Trails: Block: " + keyRegistryDomain + " not in Forge Registry.  Mispelled?";
			if (MyConfig.aDebugLevel>0) {
				System.out.println(result);
			}		

			return result;
		}	
		
		String trailKey = keyRegistryDomain + ">" + Integer.toString(meta);

		if (speed == 0) {
			trailBlockHashtable.remove(trailKey);
//        	MyConfig.pushValues();
        	result = "Removed Happy Trails Entry for block: " + trailKey;		
		} else if ((speed >= -11) && (speed <= 11)) {
			TrailBlockItem t = new TrailBlockItem(speed);
			trailBlockHashtable.put (trailKey, t);
//        	MyConfig.pushValues();
        	result = "Happy Trails:Added Trails Entry for block: " + trailKey + ", " + speed +".";	        	
			if (MyConfig.aDebugLevel>0) {
				System.out.println(result);
			}		

		} else {
			result = "Happy Trails: Error speed must be [-11 to 11].";		
			if (MyConfig.aDebugLevel>0) {
				System.out.println(result);
			}		

		}
		return result;
	}	
	
	public static String[] getTrailHashAsStringArray() {
		String[] returnStringArray = new String[trailBlockHashtable.size()];
		int speed;
		int i = 0;
		for (String key:trailBlockHashtable.keySet()) {
			speed = trailBlockHashtable.get(key).trailBlockSpeed;
			String tempString = key+","+speed;
			returnStringArray [i++] = tempString;
		}
		return returnStringArray;
	
	}

	public static void trailBlockInit() {
		
		int i = 0;
		trailBlockHashtable.clear();
		while (i < MyConfig.defaultTrailBlocks.length) {
			try {
				StringTokenizer st = new StringTokenizer(MyConfig.defaultTrailBlocks[i], ",");
				String trailKey = st.nextToken();
				int speed = Integer.parseInt(st.nextToken().trim());
				
				StringTokenizer keySt = new StringTokenizer(trailKey,">");
				String keyRegistryDomain = keySt.nextToken();
				int meta = Integer.parseInt(keySt.nextToken().trim());
				
				String result = updateRemoveTrailBlockInfo(keyRegistryDomain, meta, speed);
				if (MyConfig.aDebugLevel > 0) {
					System.out.println(result);
				}
			} catch (Exception e) {
				System.out.println("Happy Trails:  Bad Block Config : " + MyConfig.defaultTrailBlocks[i]);
			}
			i++;
		}
	}
	

	public static class TrailBlockItem {
		int trailBlockSpeed;

		public TrailBlockItem(int trailBlockSpeed) {
			this.trailBlockSpeed = trailBlockSpeed;
		}

		public int getTrailBlockSpeed() {
			return trailBlockSpeed;
		}

	}

}

