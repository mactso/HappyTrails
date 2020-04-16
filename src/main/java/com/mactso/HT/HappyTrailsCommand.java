package com.mactso.HT;

import java.util.ArrayList;
import java.util.List;

import com.mactso.HT.config.MyConfig;
import com.mactso.HT.config.TrailBlockManager;
import com.mactso.HT.config.TrailBlockManager.TrailBlockItem;

import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class HappyTrailsCommand implements ICommand {
	final Block AIR = Block.getBlockFromName("AIR");
	
	@Override
	public int compareTo(ICommand o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		return "/HappyTrails";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return ("Gives Happy Trails Info.");
	}

	@Override
	public List<String> getAliases() {
		List<String> commandAliases = new ArrayList<>();
		commandAliases.add("ht");
		commandAliases.add("happytrails");
		commandAliases.add("/ht");
		commandAliases.add("/happytrails");
		return commandAliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {
			EntityPlayer player = (EntityPlayer) sender;
//			World worldName = player.world;
			if (args[0].equalsIgnoreCase("info")) {
	            showInfo(player);			
			} else if (args[0].equalsIgnoreCase("debug")) {
		        setDebugLevel(args, player);
			} else if (args[0].equalsIgnoreCase("set")) {
	    		setSpeed(args, player);		        	
			} else if (args[0].equalsIgnoreCase("particles")) {
		        	setParticleDisplay(args, player);	
			}
		}
	}

	private void setParticleDisplay(String[] args, EntityPlayer player) {
		String s;
			if ((args[1].equalsIgnoreCase("true") ||
					args[1].equalsIgnoreCase("on"))) {
				s = "Particles will display";
				MyConfig.aParticlesDisplay = true;
			} else if ((args[1].equalsIgnoreCase("false") || 
					args[1].equalsIgnoreCase("off"))) {
				s = "Particles will not display";
				MyConfig.aParticlesDisplay = false;
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
	}

	private void setSpeed(String[] args, EntityPlayer player) {

		Block b = player.world.getBlockState(player.getPosition()).getBlock();
		if (b == AIR) {
		    b = player.world.getBlockState(player.getPosition().down()).getBlock();		            	
		}
		String blockKey = b.getRegistryName().toString();
		try {
			int newSpeedValue = 0;
			newSpeedValue = Integer.valueOf(args[1]);
			String s;
			TrailBlockManager.TrailBlockItem oldT = null;
			if (newSpeedValue == 0) {
				s = "Removed Happy Trails Entry for block: " + blockKey;
	        	TrailBlockManager.trailBlockHashtable.remove(blockKey);
	        	MyConfig.pushValues();
			} else
			if (newSpeedValue < 0) {
				s = "Added Happy Trails Slowness: " + newSpeedValue + " added for block: " + blockKey.toString().trim();
		       	oldT = TrailBlockManager.trailBlockHashtable.put(blockKey, new TrailBlockItem(newSpeedValue));
			} else {
				s = "Speed: " + newSpeedValue + " added for block: " + blockKey.toString().trim();
		       	oldT = TrailBlockManager.trailBlockHashtable.put(blockKey, new TrailBlockItem(newSpeedValue));

			}
			ITextComponent component = new TextComponentString (s);
			component.getStyle().setColor(TextFormatting.GREEN);
			player.sendMessage(component);				        	
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

	private void setDebugLevel (String[] args, EntityPlayer player) {
		if(args[1] != null) {
			int debugLevel = MyConfig.aDebugLevel;
			try {
		    	debugLevel = Integer.valueOf(args[1]);
		    	if ((debugLevel >= 0) || (debugLevel <= 2)) {
		    		MyConfig.aDebugLevel = debugLevel;
		    		ITextComponent component = 
		    				new TextComponentString (
			  		  		  "Debug Value set to : " + debugLevel	
			  				);		        		
			  		component.getStyle().setColor(TextFormatting.GREEN);
			  		player.sendMessage(component);	
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

	private void showInfo(EntityPlayer player) {
		ITextComponent component = new TextComponentString ("\n Current Values");
		component.getStyle().setColor(TextFormatting.GREEN);
		player.sendMessage(component);
		Block b = player.world.getBlockState(player.getPosition()).getBlock();
		if (b == AIR) {
		    b = player.world.getBlockState(player.getPosition().down()).getBlock();		            	
		}
		TrailBlockManager.TrailBlockItem t = TrailBlockManager.getTrailBlockInfo(b.getRegistryName().toString());
		int speed = 0;
		if (t != null) {
		    speed = t.getTrailBlockSpeed();
		}
		String particleState;
		
		if (MyConfig.aParticlesDisplay) {
			particleState = "Particles...................: currently ON.";
		} else {
			particleState = "Particles...................: currently OFF.";
			
		}

		component = new TextComponentString (
		  		  "\n  Speed Level...........: " + speed
				+ "\n  Standing On.............: " + b.getRegistryName().toString()
		  		+ "\n  Player Position....: " + player.getPosition().toString()		            		
		  		+ "\n  Debug Level...........: " + MyConfig.aDebugLevel
		  		+ "\n  " + particleState
				);
		component.getStyle().setColor(TextFormatting.GREEN);
		player.sendMessage(component);
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if (sender.canUseCommand(3,"SetHappyTrailsSpeed")) {
			return true;
		}
		return false;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		List<String> commandTabCompletions = new ArrayList();
		commandTabCompletions.add("info");
		commandTabCompletions.add("set");
		commandTabCompletions.add("particles");
		commandTabCompletions.add("debug");
		return commandTabCompletions;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}

}
