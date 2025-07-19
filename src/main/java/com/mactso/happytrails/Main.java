// 15.2 -Happy Trails
package com.mactso.happytrails;

import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.utility.Utility;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod("happytrails")
public class Main {

	    public static final String MODID = "happytrails"; 
	    
	    public Main(FMLJavaModLoadingContext context)
	    {
			// context.getModEventBus().register(this);  Use when registering several methods.
			context.registerConfig(ModConfig.Type.COMMON, MyConfig.COMMON_SPEC);
	        Utility.debugMsg(0,MODID + ": Registering Mod.");
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


