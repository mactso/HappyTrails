//1.15.2-2.0

package com.mactso.happytrails.config;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.lang.String;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class TrailBlockManager {
	public static Hashtable<String, TrailBlockItem> trailBlockHashtable = new Hashtable<>();
	private static String defaultTrailBlockString = "hbm:default";
	private static String defaultTrailBlockKey = defaultTrailBlockString;

	public static TrailBlockItem getTrailBlockInfo(String key) {
		String iKey = key;

		if (trailBlockHashtable.isEmpty()) {
			trailBlockInit();
		}

		TrailBlockItem t = trailBlockHashtable.get(iKey);

		return t;
	}

	public static String getTrailHashAsString() {
		String returnString="";
		int speed;
		for (String key:trailBlockHashtable.keySet()) {
			speed = trailBlockHashtable.get(key).trailBlockSpeed;
			String tempString = key+","+speed+";";
			returnString += tempString;
		}
		return returnString;
	
	}

	public static void trailBlockInit() {
		
		List <String> dTL6464 = new ArrayList<>();
		
		int i = 0;
		String trailBlockLine6464 = "";
		// Forge Issue 6464 patch.
		StringTokenizer st6464 = new StringTokenizer(MyConfig.defaultTrailBlocks6464, ";");
		while (st6464.hasMoreElements()) {
			trailBlockLine6464 = st6464.nextToken().trim();
			if (trailBlockLine6464.isEmpty()) continue;
			dTL6464.add(trailBlockLine6464);  
			i++;
		}

		MyConfig.defaultTrailBlocks = dTL6464.toArray(new String[i]);
		
		i = 0;
		trailBlockHashtable.clear();
		while (i < MyConfig.defaultTrailBlocks.length) {
			try {
				StringTokenizer st = new StringTokenizer(MyConfig.defaultTrailBlocks[i], ",");
				String modAndBlock = st.nextToken();
				String key = modAndBlock;
				String speed = st.nextToken();
				int tHappyTrailSpeed = Integer.parseInt(speed.trim());
				if ((tHappyTrailSpeed < -99) || (tHappyTrailSpeed > 99)) {
					tHappyTrailSpeed = 2;
				}


				trailBlockHashtable.put(key, new TrailBlockItem(tHappyTrailSpeed));
				if (!modAndBlock.equals("hbm:default") &&
				    !ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(modAndBlock))
				   )  {
					System.out.println("Happy Trails: Block: " + modAndBlock + " not in Forge Registry.  Mispelled?");
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

