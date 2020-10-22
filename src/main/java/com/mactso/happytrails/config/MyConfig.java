package com.mactso.happytrails.config;

import org.apache.commons.lang3.tuple.Pair;

import com.mactso.happytrails.Main;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = Main.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MyConfig {
	
	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	
	static
	{
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}	
	
//	public static int       aHappyTrailSpeed;
	public static int       aDebugLevel;
	public static boolean   aParticlesOn;
	public static String[]  defaultTrailBlocks;
	public static String    defaultTrailBlocks6464;
	public static final Boolean BOLD = true;
	
	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent)
	{
		if (configEvent.getConfig().getSpec() == MyConfig.COMMON_SPEC)
		{
			bakeConfig();
			TrailBlockManager.trailBlockInit();
		}
	}	

	public static void pushDebugValue() {
		if (aDebugLevel > 0) {
			System.out.println("dbgL:"+MyConfig.aDebugLevel);
		}
		COMMON.debugLevel.set( MyConfig.aDebugLevel);
	}

	public static void pushNewParticlesOn() {
		COMMON.particlesOn.set(MyConfig.aParticlesOn);
	}
	
	public static void pushValues() {
		COMMON.defaultTrailBlocksActual.set(TrailBlockManager.getTrailHashAsString());
	}	
	
//    // for this mod- default color is green.
//	public static void sendChat(PlayerEntity p, String chatMessage) {
//		StringTextComponent component = new StringTextComponent (chatMessage);
//		component.func_240701_a_(TextFormatting.DARK_GREEN);
//		p.sendMessage(component, p.getUniqueID());
//	}

	// support for any color chattext
	public static void sendChat(PlayerEntity p, String chatMessage, Color textColor) {
		StringTextComponent component = new StringTextComponent (chatMessage);
		component.getStyle().setColor(textColor);
		p.sendMessage(component, p.getUniqueID());
	}
	
	// support for any color, optionally bold text.
	public static void sendBoldChat(PlayerEntity p, String chatMessage, Color textColor) {
		StringTextComponent component = new StringTextComponent (chatMessage);

		component.getStyle().setBold(true);
		component.getStyle().setColor(textColor);

		p.sendMessage(component, p.getUniqueID());
	}	
	public static void bakeConfig()
	{
		aDebugLevel = COMMON.debugLevel.get();
		defaultTrailBlocks6464 = COMMON.defaultTrailBlocksActual.get() ;
		if (aDebugLevel > 0) {
			System.out.println("Happy Trails Debug: " + aDebugLevel );
		}
	}
	
	public static class Common {

//		public final IntValue	  happyTrailSpeed;
		public final IntValue     debugLevel;
		public final BooleanValue particlesOn;
		public final ConfigValue<String> defaultTrailBlocksActual;
		public final String defaultTrailBlocks6464 = 
				  "minecraft:grass_path,2;"
				+ "minecraft:sand,-1;"
				+ "minecraft:stone_brick_slab,3;"
				+ "minecraft:bricks,11;"
				;

		
		public Common(ForgeConfigSpec.Builder builder) {
			builder.push("Happy Trail Control Values");
//			happyTrailSpeed= builder
//					.comment("Happy Trail Speed: -11 to 11")
//					.translation(Main.MODID + ".config." + "happyTrailSpeed")
//					.defineInRange("happyTrailSpeed", () -> -11, 1, 11);
			
			debugLevel = builder
					.comment("Debug Level: 0 = Off, 1 = Log, 2 = Chat+Log")
					.translation(Main.MODID + ".config." + "debugLevel")
					.defineInRange("debugLevel", () -> 0, 0, 2);

			particlesOn = builder
					.comment("Particles On: [true] / false")
					.translation(Main.MODID + ".config." + "particlesOn")
					.define ("particlesOn", () -> true);
			
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
