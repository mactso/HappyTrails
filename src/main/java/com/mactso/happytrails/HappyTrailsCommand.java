package com.mactso.happytrails;

import java.util.ArrayList;
import java.util.List;

import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.config.TrailBlockManager;
import com.mactso.happytrails.config.TrailBlockManager.TrailBlockItem;
import com.mactso.happytrails.reports.Reports;

import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;


public class HappyTrailsCommand implements ICommand {

	
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
	            Reports.showInfo(player);	
			} else if (args[0].equalsIgnoreCase("debug")) {
		        MyConfig.setDebugLevel(args, player);
			} else if (args[0].equalsIgnoreCase("set")) {
				MyConfig.setTrailBlockSpeedValue(args, player);		        	
			} else if (args[0].equalsIgnoreCase("particles")) {
		        MyConfig.setParticleDisplay(args, player);	
			}
		}
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
		List<String> commandTabCompletions = new ArrayList<String>();
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
