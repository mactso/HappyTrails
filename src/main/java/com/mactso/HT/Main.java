// 15.2 - 0.0.0.1 Happy Trails
package com.mactso.HT;

import com.mactso.HT.config.MyConfig;
import com.mactso.HT.events.PlayerMoveEvent;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

@Mod("ht")
public class Main {

	    public static final String MODID = "ht"; 
	    
	    public Main()
	    {

			FMLJavaModLoadingContext.get().getModEventBus().register(this);
	        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER,MyConfig.SERVER_SPEC );
			MinecraftForge.EVENT_BUS.register(this);
			
	    }

	    // Register ourselves for server and other game events we are interested in
		@SubscribeEvent 
		public void preInit (final FMLCommonSetupEvent event) {
			System.out.println("Happy Trails: Registering Handler");
			MinecraftForge.EVENT_BUS.register(new PlayerMoveEvent ());
			
		}       

		@SubscribeEvent 		
		public void onCommandsRegistry(final RegisterCommandsEvent event) {
			HTCommand.register(event.getDispatcher());			
		}
		
		// in 14.4 and later, config file loads when the server starts when the world starts.
		@SubscribeEvent 
		public void onServerStarting (FMLServerStartingEvent event) {

		}
}


