package com.mactso.HT.config;

import com.mactso.HT.util.Reference;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Ignore;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;

@Config(modid=Reference.MOD_ID)
@Mod.EventBusSubscriber
public class MyConfig {
	@Ignore
	public static boolean serverSide = false;
	
	@Comment ( { "Debug Level" } )
	@Name ("Debug Level   0 to 2 : 'Off', 'Log', 'Chat & Log'")
	@RangeInt  (min=0, max=2)
	public static int aDebugLevel;

	@Comment ( { "Show Particles" } )
	@Name ("Show Particles")
	public static boolean   aParticlesDisplay = true;
	
	@Comment ( { "Trail Block Values: mod:block, speed [-11,11]" } )
	@Name ( "Trail Block Values:" )
	public static String [] defaultTrailBlocks= {
					"minecraft:grass_path,2",
					"minecraft:sand,-1",
					"minecraft:stone_brick_slab,3"
				};	
	

	@SubscribeEvent
	public static void onModConfigEvent(OnConfigChangedEvent event)
	{
		if(event.getModID().equals(Reference.MOD_ID))
		{
			ConfigManager.sync (event.getModID(), Config.Type.INSTANCE);
			TrailBlockManager.trailBlockInit();			
			if (!serverSide) {
				if (aDebugLevel>0) {
					System.out.println("Happy Trails Configuration Change ");
				}				
//				aDigSpeedModifier = serverDigSpeed;
//				aDownSpeedModifier = serverDownSpeed;
			}
		}
	}
	
	public static void pushValues() {
		MyConfig.defaultTrailBlocks = TrailBlockManager.getTrailHashAsStringArray();
		ConfigManager.sync (Reference.MOD_ID, Config.Type.INSTANCE);
	}
}



