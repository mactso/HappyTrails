package com.mactso.happytrails.reports;

import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.config.TrailBlockManager;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class Reports {
	
	
	public static void showInfo(EntityPlayer player) {
		IBlockState bs;
		Block block;
		int meta = 0;
		
		ITextComponent component = new TextComponentString ("\n Current Values");
		component.getStyle().setColor(TextFormatting.GREEN);
		player.sendMessage(component);
		bs = player.world.getBlockState(player.getPosition());
		block = bs.getBlock();
		meta = block.getMetaFromState(bs);
		if (block instanceof BlockAir) {
			bs = player.world.getBlockState(player.getPosition().down());
			block = bs.getBlock();
			meta = block.getMetaFromState(bs);
		}

		String trailKey = block.getRegistryName().toString() + ">" + meta;

		TrailBlockManager.TrailBlockItem t = TrailBlockManager.getTrailBlockInfo(trailKey);
		
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
				  "\n  Standing On.............: " + trailKey  
				+ "\n  Speed Level...........: " + speed
				+ "\n  Player Position....: " + player.getPosition().toString()		            		
		  		+ "\n  Debug Level...........: " + MyConfig.aDebugLevel
		  		+ "\n  " + particleState
				);
		component.getStyle().setColor(TextFormatting.GREEN);
		player.sendMessage(component);
	}
}
