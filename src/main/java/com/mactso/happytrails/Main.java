// 15.2 -Happy Trails
package com.mactso.happytrails;



import com.mactso.happytrails.config.MyConfig;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;



public class Main implements ModInitializer {


    public static final String MOD_ID = "happytrails";
    
	public void onInitialize() {
		registerEvents();
		MyConfig.registerConfigs();

	}
	
	private void registerEvents() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			HappyTrailsCommands.register(dispatcher);
		});
	}


}


