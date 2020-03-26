// 15.2 - 0.0.0.1
package com.mactso.HT;

import net.minecraftforge.fml.common.Mod;

@Mod("HT")
public class Main {

	    public static final String MODID = "HT"; 
		int i = 7;
	    
	    public Main()
	    {

//			FMLJavaModLoadingContext.get().getModEventBus().register(this);
//	        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER,MyConfig.SERVER_SPEC );
//			MinecraftForge.EVENT_BUS.register(this);
			
	    }

	    // Register ourselves for server and other game events we are interested in
//		@SubscribeEvent 
//		public void preInit (final FMLCommonSetupEvent event) {
//			System.out.println("HarderBranchMining: Registering Handler");
//			MinecraftForge.EVENT_BUS.register(new BlockBreakHandler ());
//			
//		}       

		// in 14.4 and later, config file loads when the server starts when the world starts.
//		@SubscribeEvent 
//		public void onServerStarting (FMLServerStartingEvent event) {
//			
//			ToolManager.toolInit();
//			HBMCommand.register(event.getCommandDispatcher());
//		}
}


