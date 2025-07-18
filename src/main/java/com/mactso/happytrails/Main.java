// 15.2 -Happy Trails
package com.mactso.happytrails;

import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.events.PlayerMoveEvent;
import com.mactso.happytrails.events.SteedMoveEvent;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod("happytrails")
public class Main {

	    public static final String MODID = "happytrails"; 
	    
	    public Main(FMLJavaModLoadingContext context)
	    {
			context.getModEventBus().register(this);
			context.registerConfig(ModConfig.Type.COMMON, MyConfig.COMMON_SPEC);
			
	    }

	    // Register ourselves for server and other game events we are interested in
		@SubscribeEvent 
		public void preInit (final FMLCommonSetupEvent event) {
			System.out.println("Happy Trails: Registering Handler");
			MinecraftForge.EVENT_BUS.register(new PlayerMoveEvent ());
			MinecraftForge.EVENT_BUS.register(new SteedMoveEvent ());			
		}       

	    @Mod.EventBusSubscriber()
	    public static class ForgeEvents
	    {
			@SubscribeEvent 		
			public static void onCommandsRegistry(final RegisterCommandsEvent event) {
				System.out.println("Happy Trails: Registering Command Dispatcher");
				HappyTrailsCommands.register(event.getDispatcher());			
			}

	    }

}


