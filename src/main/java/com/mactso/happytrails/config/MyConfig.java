package com.mactso.happytrails.config;

import com.mactso.happytrails.util.Reference;

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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

@Config(modid=Reference.MOD_ID)
@Mod.EventBusSubscriber
public class MyConfig {
	@Ignore
	final static Block AIR = Block.getBlockFromName("AIR");
	
	@Ignore
	public static boolean serverSide = false;
	@Ignore
	public static boolean startupComplete = false;
	
	@Comment ( { "Debug Level" } )
	@Name ("Debug Level   0 to 2 : 'Off', 'Log', 'Chat & Log'")
	@RangeInt  (min=0, max=2)
	public static int aDebugLevel = 1;

	@Comment ( { "Show Particles" } )
	@Name ("Show Particles")
	public static boolean   aParticlesDisplay = true;
	
	@Comment ( { "Trail Block Values: mod:block, speed [-11,11]" } )
	@Name ( "Trail Block Values:" )
	public static String [] defaultTrailBlocks= {
					"minecraft:grass_path,2",
					"minecraft:sand,-1",
					"minecraft:stone_slab,3"
				};	
	
	@SubscribeEvent
	public static void onModConfigEvent(OnConfigChangedEvent event)
	{
		if(event.getModID().equals(Reference.MOD_ID))
		{
			TrailBlockManager.trailBlockInit();	
			if (serverSide) {
				if (aDebugLevel>0) {
					System.out.println("Server: Happy Trails ConfigurationChangedEvent.");
				}				
			}
			if (!serverSide) {
				if (aDebugLevel>0) {
					System.out.println("Client: Happy Trails ConfigurationChangedEvent.");
				}				
			}
		}
	}

	public static void pushTrailBlockSpeedValues() {
		MyConfig.defaultTrailBlocks = TrailBlockManager.getTrailHashAsStringArray();
		ConfigManager.sync (Reference.MOD_ID, Config.Type.INSTANCE);
	}
	
	public static void setDebugLevel (String[] args, EntityPlayer player) {
		if(args[1] != null) {
			int debugLevel = MyConfig.aDebugLevel;
			try {
		    	debugLevel = Integer.valueOf(args[1]);
		    	if ((debugLevel >= 0) && (debugLevel <= 2)) {
		    		MyConfig.aDebugLevel = debugLevel;
		    		ConfigManager.sync (Reference.MOD_ID, Config.Type.INSTANCE);
		    		ITextComponent component = 
		    				new TextComponentString (
			  		  		  "Debug Value set to : " + debugLevel	
			  				);		        		
			  		component.getStyle().setColor(TextFormatting.GREEN);
			  		player.sendMessage(component);
					if (aDebugLevel>0) {
						System.out.println(component.toString());
					}		
		    	}
			}
			catch (NumberFormatException e){
				ITextComponent component = 
						new TextComponentString (
		  		  		  "Debug Values should be : 0 to 2"	
		  				);		        		
		  		component.getStyle().setColor(TextFormatting.RED);
		  		player.sendMessage(component);	
			}
		}
	}
	
	public static void setParticleDisplay(String[] args, EntityPlayer player) {
		String s;
			if ((args[1].equalsIgnoreCase("true") ||
					args[1].equalsIgnoreCase("on"))) {
				s = "Particles will display";
				MyConfig.aParticlesDisplay = true;
	    		ConfigManager.sync (Reference.MOD_ID, Config.Type.INSTANCE);
			} else if ((args[1].equalsIgnoreCase("false") || 
					args[1].equalsIgnoreCase("off"))) {
				s = "Particles will not display";
				MyConfig.aParticlesDisplay = false;
	    		ConfigManager.sync (Reference.MOD_ID, Config.Type.INSTANCE);
			} else {
				if (MyConfig.aParticlesDisplay) {
					s = "Particles currently on.";
				} else {
					s = "Particles currently off.";
				}
			}
			ITextComponent component = 
					new TextComponentString (s);
			component.getStyle().setColor(TextFormatting.GREEN);
			player.sendMessage(component);
			if (aDebugLevel>0) {
				System.out.println(component.toString());
			}		

	}
	
	public static void setTrailBlockSpeedValue(String[] args, EntityPlayer player) {

		Block b = player.world.getBlockState(player.getPosition()).getBlock();
		if (b == AIR) {
		    b = player.world.getBlockState(player.getPosition().down()).getBlock();		            	
		}
		String keyRegistryDomain = b.getRegistryName().toString();
		try {
			int newSpeedValue =  Integer.valueOf(args[1]);
			String result;
			result = TrailBlockManager.updateRemoveTrailBlockInfo(keyRegistryDomain, newSpeedValue);
			pushTrailBlockSpeedValues();
			ITextComponent component = new TextComponentString (result);
			component.getStyle().setColor(TextFormatting.GREEN);
			if (MyConfig.aDebugLevel >= 0) {
				System.out.println(result);
				if (MyConfig.aDebugLevel >=1) {
 					player.sendMessage(component);				        	
 				}
			}
		}
		catch (NumberFormatException e){
			ITextComponent component = 
					new TextComponentString (
			  		  "Slow Values: -11 to -1, Remove: 0, Speed Values: 1 to 11"	
					);
			component.getStyle().setColor(TextFormatting.RED);
			player.sendMessage(component);	
		}
	}
}



