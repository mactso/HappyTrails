// 12.2 - 0.0.0.1 Happy Trails
package com.mactso.happytrails;
import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.config.TrailBlockManager;
import com.mactso.happytrails.events.MovePlayerEvent;
import com.mactso.happytrails.network.Register;
import com.mactso.happytrails.util.Reference;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
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
		MyConfig.startupComplete = true;
		
		System.out.println("Happy Trails: Registering Move Player Event Handler");
		MinecraftForge.EVENT_BUS.register(new MovePlayerEvent ());
		MinecraftForge.EVENT_BUS.register(this);		
	}

	@EventHandler
	public void serverLoad (FMLServerStartingEvent event) {
		System.out.println("Happy Trails: Registering Commands");
		event.registerServerCommand(new HappyTrailsCommand());
	}
	
	@EventHandler
	public void init (FMLInitializationEvent event) {
		Register.initPackets();
	}

	@SubscribeEvent
	public void clientConnectionEvent (PlayerLoggedInEvent event) {
	    if (event.player instanceof EntityPlayerMP)
	    {
	    	MyConfig.serverSide = true;
	    }			
	}
	
//	@EventHandler
//	public void init (FMLInitializationEvent event) {
//		Register.initPackets();
//	}


}


