// 15.2 - 0.0.0.1 Happy Trails
package com.mactso.happytrails;

import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.events.PlayerMoveEvent;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

@Mod("happytrails")
public class Main {

	    public static final String MODID = "happytrails"; 
	    
	    public Main()
	    {

			FMLJavaModLoadingContext.get().getModEventBus().register(this);
	        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER,MyConfig.SERVER_SPEC );

			
	    }

	    // Register ourselves for server and other game events we are interested in
		@SubscribeEvent 
		public void preInit (final FMLCommonSetupEvent event) {
			System.out.println("Happy Trails: Registering Handler");
			MinecraftForge.EVENT_BUS.register(new PlayerMoveEvent ());
			
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


