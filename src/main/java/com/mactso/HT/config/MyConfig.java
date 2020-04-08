package com.mactso.HT.config;

import java.util.Arrays;
import java.util.List;
import com.mactso.HT.Main;
import com.mactso.HT.config.TrailBlockManager;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfig.Server;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = Main.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MyConfig {
	
	public static final Server SERVER;
	public static final ForgeConfigSpec SERVER_SPEC;
	
	static
	{
		final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
		SERVER_SPEC = specPair.getRight();
		SERVER = specPair.getLeft();
	}	
	
	public static int       aHappyTrailSpeed = 1;
	public static int       aDebugLevel;
	public static String[]  defaultTrailBlocks;
	public static String    defaultTrailBlocks6464;
	
	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent)
	{
		if (configEvent.getConfig().getSpec() == MyConfig.SERVER_SPEC)
		{
			bakeConfig();
			TrailBlockManager.trailBlockInit();
		}
	}	

	public static void pushDebugValue() {
		if (aDebugLevel > 0) {
			System.out.println("dbgL:"+MyConfig.aDebugLevel);
		}
		SERVER.debugLevel.set( MyConfig.aDebugLevel);
	}
	
	public static void pushValues() {
		SERVER.defaultTrailBlocksActual.set(TrailBlockManager.getTrailHashAsString());
	}	
	public static void bakeConfig()
	{
		aDebugLevel = SERVER.debugLevel.get();
		defaultTrailBlocks6464 = SERVER.defaultTrailBlocksActual.get() ;
		System.out.println("Happy Trails Debug: " + aDebugLevel );
	}
	
	public static class Server {
		public final IntValue	 happyTrailSpeed ;
		public final IntValue    debugLevel;
		public final ConfigValue<String> defaultTrailBlocksActual;
		public final String defaultTrailBlocks6464 = 
				  "minecraft:grass_path,2;\n\r"
				+ "minecraft:sand,-1;\n\r"
				+ "minecraft:stone_brick_slab,3\n\r;"
				;
		
		public Server(ForgeConfigSpec.Builder builder) {
			builder.push("Happy Trail Control Values");
			happyTrailSpeed= builder
					.comment("Happy Trail Speed: -11 to 11")
					.translation(Main.MODID + ".config." + "happyTrailSpeed")
					.defineInRange("happyTrailSpeed", () -> -11, 1, 11);
			
			debugLevel = builder
					.comment("Debug Level: 0 = Off, 1 = Log, 2 = Chat+Log")
					.translation(Main.MODID + ".config." + "debugLevel")
					.defineInRange("debugLevel", () -> 0, 0, 2);			
			builder.pop();
			builder.push ("Trail Values 6464");
			
			defaultTrailBlocksActual = builder
					.comment("Trail Block String 6464")
					.translation(Main.MODID + ".config" + "defaultTrailBlocksActual")
					.define("defaultTrailBlocksActual", defaultTrailBlocks6464);
			builder.pop();			
		}
	}
	
}
