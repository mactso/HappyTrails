//1.15.2-2.0

package com.mactso.happytrails.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.lang.String;
import java.text.NumberFormat;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class TrailBlockManager {
	public static Hashtable<String, TrailBlockItem> trailBlockHashtable = new Hashtable<>();
	private static String defaultTrailBlockString = "hbm:default";
	private static String defaultTrailBlockKey = defaultTrailBlockString;


	
	private static record ReportData(String block, int speed)  {
	}

	static record ReportDataBySpeed() implements Comparator<ReportData> {


		public int compare(ReportData o1, ReportData o2) {
			if (o1.speed < o2.speed)
				return -1;
			else if (o1.speed > o2.speed)
				return 1;
			else {
				return o1.block.compareTo(o2.block);
			}
		}

	}

	public static TrailBlockItem getTrailBlockInfo(String key) {
		String iKey = key;

		if (trailBlockHashtable.isEmpty()) {
			trailBlockInit();
		}

		TrailBlockItem t = trailBlockHashtable.get(iKey);

		return t;
	}
	
	public static String getTrailHashAsConfigString() {
		String returnString = "";
		int speed;
			for (String key : trailBlockHashtable.keySet()) {
				speed = trailBlockHashtable.get(key).trailBlockSpeed;
				String tempString = key+","+speed+";";
				returnString += tempString;
			}
		return returnString;
	}

	public static String getTrailBlockReport() {
		String returnString = "";
		String speedString = "";
		String padString = "";
		ArrayList<ReportData> rd = new ArrayList<>();
		
		for (Entry<String, TrailBlockItem> entry : trailBlockHashtable.entrySet()) {
			rd.add(new ReportData(entry.getKey(), entry.getValue().trailBlockSpeed));
		}

		rd.sort(new ReportDataBySpeed());

		for (ReportData item : rd) {
			speedString = getSpeedString(item.speed);
			if (item.speed > 0) {
				padString = "+";
			} else {
				padString = "";
			}
			String tempString = padString + item.speed + ", " + speedString + ",   " + item.block  + ";\n";
			returnString += tempString;
		}	
		return returnString;
	}
	


	public static String getSpeedString(int speed) {
		float temp;
		String padString = " ";
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(false);
		String speedString = "normal (4.33 m/s)";

		if (speed < -10) {
			temp = 4.43f + 2.0f * (speed / 96.0f);
			speedString = "slow (" + padString + nf.format(temp) + " m/s)";
		} else if (speed < 0) {
			temp = 4.38f + (4.33f * 0.17f * speed);
			if (temp < 0)
				temp = 0;
			speedString = "slow (" + nf.format(temp) + " m/s)";
		} else if ((speed == 10) || (speed == 99)) {
			speedString = "plaid";
		} else if (speed > 10) {
			temp = (speed / 3.65f) + 1.5f;
			if (temp >=11) {
				padString = "";
			}
			speedString = "fast (" + padString + nf.format(temp) + " m/s)";
		} else if (speed > 0) {
			temp = 4.3f * (1.0f + 0.2f * speed);
			speedString = "fast ("+  nf.format(temp) + " m/s)";
		}
		return speedString;
	}

	public static void trailBlockInit() {

		List<String> dTL6464 = new ArrayList<>();

		int i = 0;
		String trailBlockLine6464 = "";
		// Forge Issue 6464 patch.
		StringTokenizer st6464 = new StringTokenizer(MyConfig.defaultTrailBlocks6464, ";");
		while (st6464.hasMoreElements()) {
			trailBlockLine6464 = st6464.nextToken().trim();
			if (trailBlockLine6464.isEmpty())
				continue;
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
				if (!modAndBlock.equals("hbm:default")
						&& !ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(modAndBlock))) {
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
