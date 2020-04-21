package com.mactso.happytrails.reports;

import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.config.TrailBlockManager;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class Reports {
	final static Block AIR = Block.getBlockFromName("AIR");
	
	public static void showInfo(EntityPlayer player) {
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
			particleState = "Particles...................: ON.";
		} else {
			particleState = "Particles...................: OFF.";
			
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
}
