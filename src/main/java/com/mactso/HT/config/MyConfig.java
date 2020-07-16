package com.mactso.HT.config;

import org.apache.commons.lang3.tuple.Pair;

import com.mactso.HT.Main;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
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
	
//	public static int       aHappyTrailSpeed;
	public static int       aDebugLevel;
	public static boolean   aParticlesOn;
	public static String[]  defaultTrailBlocks;
	public static String    defaultTrailBlocks6464;
	public static final Boolean BOLD = true;
	
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

	public static void pushNewParticlesOn() {
		SERVER.particlesOn.set(MyConfig.aParticlesOn);
	}
	
	public static void pushValues() {
		SERVER.defaultTrailBlocksActual.set(TrailBlockManager.getTrailHashAsString());
	}	
    // for this mod- default color is green.
	public static void sendChat(PlayerEntity p, String chatMessage) {
		StringTextComponent component = new StringTextComponent (chatMessage);
		// set to Dark Green Bold
		Style chatStyle = Style.field_240709_b_.func_240712_a_(TextFormatting.DARK_GREEN);
		p.sendMessage(component.func_230530_a_(chatStyle) , p.getUniqueID());
	}

	// support for any color chattext
	public static void sendChat(PlayerEntity p, String chatMessage, TextFormatting textColor) {
		StringTextComponent component = new StringTextComponent (chatMessage);
		Style chatStyle = Style.field_240709_b_.func_240712_a_(textColor);
		p.sendMessage(component.func_230530_a_(chatStyle) , p.getUniqueID());
	}
	
	// support for any color, optionally bold text.
	public static void sendChat(PlayerEntity p, String chatMessage, TextFormatting textColor, boolean boldText) {
		StringTextComponent component = new StringTextComponent (chatMessage);
		// set to Dark Green Bold
		Style chatStyle = Style.field_240709_b_.func_240712_a_(textColor).func_240713_a_(boldText);
		p.sendMessage(component.func_230530_a_(chatStyle) , p.getUniqueID());
	}	
	public static void bakeConfig()
	{
		aDebugLevel = SERVER.debugLevel.get();
		defaultTrailBlocks6464 = SERVER.defaultTrailBlocksActual.get() ;
		if (aDebugLevel > 0) {
			System.out.println("Happy Trails Debug: " + aDebugLevel );
		}
	}
	
	public static class Server {

//		public final IntValue	  happyTrailSpeed;
		public final IntValue     debugLevel;
		public final BooleanValue particlesOn;
		public final ConfigValue<String> defaultTrailBlocksActual;
		public final String defaultTrailBlocks6464 = 
				  "minecraft:grass_path,2;"
				+ "minecraft:sand,-1;"
				+ "minecraft:stone_brick_slab,3;"
				;

		
		public Server(ForgeConfigSpec.Builder builder) {
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
