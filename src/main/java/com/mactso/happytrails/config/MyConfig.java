package com.mactso.happytrails.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.happytrails.Main;
import com.mactso.happytrails.managers.TrailBlockManager;
import com.mojang.datafixers.util.Pair;

public class MyConfig {

	private static final Logger LOGGER = LogManager.getLogger();
	public static SimpleConfig CONFIG;
	private static ModConfigProvider configs;

	private static final String defaultTrailBlockList = "minecraft:cut_copper_slab,22;minecraft:tall_grass,-30;"
			+ "minecraft:exposed_cut_copper_slab,33;minecraft:fern,-22;"
			+ "minecraft:grass,-22;minecraft:diamond_block,99;" + "minecraft:water,11;minecraft:dirt_path,11;"
			+ "minecraft:stone_brick_slab,33;minecraft:weathered_cut_copper_slab,44;"
			+ "minecraft:stone_bricks,33;minecraft:large_fern,-30;minecraft:sand,-22;";
	private static String trailBlockList;

	public static final Boolean BOLD = true;

	public static int debugLevel;
	public static String particlesOn;

	public static int getDebugLevel() {
		return debugLevel;
	}

	public static void setDebugLevel(int debugLevel) {
		MyConfig.debugLevel = debugLevel;
	}

	public static boolean isParticlesOn() {
		if (particlesOn.equals("true")) {
			return true;
		}
		return false;
	}

	public static void setNewParticlesOn(boolean bool) {
		if (bool) {
			MyConfig.particlesOn = "true";
		} else {
			MyConfig.particlesOn = "false";
		}
	}

	public static String getTrailBlockList() {
		return trailBlockList;
	}

	public static void registerConfigs() {
		configs = new ModConfigProvider();
		createConfigs();
		CONFIG = SimpleConfig.of(Main.MOD_ID + "config").provider(configs).request();
		assignConfigs();
	}

	private static void createConfigs() {
		configs.addKeyValuePair(new Pair<>("key.debugLevel", 0), "int");
		configs.addKeyValuePair(new Pair<>("key.particleson", "true"), "String");
		configs.addKeyValuePair(new Pair<>("key.trailblocklist", defaultTrailBlockList), "String");
	}

	private static void assignConfigs() {

		debugLevel = CONFIG.getOrDefault("key.debugLevel", 0);
		particlesOn = CONFIG.getOrDefault("key.particleson", "true");
		trailBlockList = CONFIG.getOrDefault("key.trailblocklist", defaultTrailBlockList);
		TrailBlockManager.trailBlockInit();
		LOGGER.info("All " + configs.getConfigsList().size() + " have been set properly");
	}

	
	public static void diskSaveDebugValue() {
		// don't save debug value from session to session.
	}

	
	public static void diskSaveParticlesOnValue() {
		String particlesOn = "false";
		if (isParticlesOn()) {
			particlesOn = "true";
		}
		configs.setKeyValuePair("key.particleson", particlesOn);
		CONFIG.diskSaveConfig();
	}

	
	public static void diskSaveTrailBlockListValue() {
		String blockList = TrailBlockManager.getTrailHashAsString();
		configs.setKeyValuePair("key.trailblocklist", blockList);
		CONFIG.diskSaveConfig();
	}

}
