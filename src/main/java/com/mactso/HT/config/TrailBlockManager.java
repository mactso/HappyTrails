//1.15.2-2.0

package com.mactso.HT.config;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;



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

	public static String[] getTrailHashAsStringArray() {
		String[] returnStringArray = new String[trailBlockHashtable.size()];
		int speed;
		int i = 0;
		for (String key:trailBlockHashtable.keySet()) {
			speed = trailBlockHashtable.get(key).trailBlockSpeed;
			String tempString = key+","+speed+";";
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
				String modAndBlock = st.nextToken();
				String key = modAndBlock;
				String speed = st.nextToken();
				int tHappyTrailSpeed = Integer.parseInt(speed.trim());
				if ((tHappyTrailSpeed < -11) || (tHappyTrailSpeed > 11)) {
					tHappyTrailSpeed = 2;
				}

				trailBlockHashtable.put(key, new TrailBlockItem(tHappyTrailSpeed));
				if (!modAndBlock.contentEquals("hbm:default") &&
				    !Block.REGISTRY.containsKey(new ResourceLocation(modAndBlock))
				   )  {
					System.out.println("Happy Trails: Block: " + modAndBlock + " not in Forge Registry.  Mispelled?");
				}
			} catch (Exception e) {
				System.out.println("Happy Trails:  Bad Block Config : " + MyConfig.defaultTrailBlocks[i]);
			}
			i++;
		}

//		if (getTrailBlockSpeed(defaultTrailBlockString) == null) {
//			int tHappyTrailsSpeed = 2;
//			trailBlockHashtable.put(defaultTrailBlockString, new TrailBlockItem(tHappyTrailsSpeed));
//		}

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

