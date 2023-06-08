//1.15.2-2.0

package com.mactso.happytrails.config;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class TrailBlockManager {
	public static Hashtable<String, TrailBlockItem> trailBlockHashtable = new Hashtable<>();

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
		String[]  trailBlockLines;
		
		int i = 0;
		String trailBlockLine6464 = "";
		// Forge Issue 6464 patch.
		StringTokenizer st6464 = new StringTokenizer(MyConfig.getTrailBlockList(), ";");
		while (st6464.hasMoreElements()) {
			trailBlockLine6464 = st6464.nextToken().trim();
			if (trailBlockLine6464.isEmpty()) continue;
			dTL6464.add(trailBlockLine6464);  
			i++;
		}
		
		

		trailBlockLines = dTL6464.toArray(new String[i]);
		
		i = 0;
		trailBlockHashtable.clear();
		while (i < trailBlockLines.length) {
			try {
				StringTokenizer st = new StringTokenizer(trailBlockLines[i], ",");
				String id = st.nextToken();
				String speed = st.nextToken();

				int tHappyTrailSpeed = Integer.parseInt(speed.trim());
				if ((tHappyTrailSpeed < -99) || (tHappyTrailSpeed > 99)) {
					tHappyTrailSpeed = 2;
				}

				trailBlockHashtable.put(id, new TrailBlockItem(tHappyTrailSpeed));
				if (Registry.BLOCK.containsId(new Identifier(id)))  {

				} else {
					System.out.println("Happy Trails: Block: " + id + " not in Registry yet.  It may be a modded block or misspelled.");
					trailBlockHashtable.put(id, new TrailBlockItem(tHappyTrailSpeed));
				}

			} catch (Exception e) {
				System.out.println("Happy Trails:  Line "+i+" has a Bad Block Config : " + trailBlockLines[i]);
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

