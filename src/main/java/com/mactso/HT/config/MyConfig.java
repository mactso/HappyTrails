package com.mactso.HT.config;

import java.util.Arrays;
import java.util.List;
import com.mactso.HT.Main;
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
	
	public static int       aHappyTrailSpeed = 0;

	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent)
	{
		if (configEvent.getConfig().getSpec() == MyConfig.SERVER_SPEC)
		{
			bakeConfig();
		}
	}	
	
	public static void bakeConfig()
	{
		aHappyTrailSpeed = SERVER.aHappyTrailSpeed.get();
		System.out.println("Happy Trails Speed: " + aHappyTrailSpeed );

	}
	
	public static class Server {
		public final IntValue	 aHappyTrailSpeed ;
	
		public Server(ForgeConfigSpec.Builder builder) {
			builder.push("Exhaustion Control Values");
			aHappyTrailSpeed= builder
					.comment("Happy Trail Speed: 0 to 9")
					.translation(Main.MODID + ".config." + "aHappyTrailSpeed")
					.defineInRange("aHappyTrailSpeed", () -> 0, 0, 9);
		}
	}
	
}
