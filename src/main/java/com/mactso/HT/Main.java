// 15.2 - 0.0.0.1 Happy Trails
package com.mactso.HT;
import com.mactso.HT.config.TrailBlockManager;
import com.mactso.HT.events.MovePlayerEvent;
import com.mactso.HT.util.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;


@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main
{
	// , serverSideOnly=true  (research this tag more)
	@Instance
	public static Main instance;
   public static SimpleNetworkWrapper network;		

	@EventHandler
	public void preInit (FMLPreInitializationEvent event) {
		TrailBlockManager.trailBlockInit();
		System.out.println("Happy Trails: Registering Handler");
		MinecraftForge.EVENT_BUS.register(new MovePlayerEvent ());
		MinecraftForge.EVENT_BUS.register(this);		
	}
	
//	@EventHandler
//	public void init (FMLInitializationEvent event) {
//		Register.initPackets();
//	}


}


