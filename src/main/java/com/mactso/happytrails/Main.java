// 15.2 -Happy Trails
package com.mactso.happytrails;




import com.mactso.happytrails.config.MyConfig;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class Main implements ModInitializer {


    public static final String MOD_ID = "happytrails";
    
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(HappyTrailsCommands::register);
		MyConfig.registerConfigs();

	}


}


