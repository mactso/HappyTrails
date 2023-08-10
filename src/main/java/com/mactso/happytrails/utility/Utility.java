package com.mactso.happytrails.utility;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.happytrails.config.MyConfig;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class Utility {
	private static final Logger LOGGER = LogManager.getLogger();
	

	public static void dbgChatln(Player p, String msg, int level) {
		if (MyConfig.getDebugLevel() > level - 1) {
			sendChat(p, msg, ChatFormatting.YELLOW);
		}
	}
	
	
	public static void debugMsg(int level, String dMsg) {

		if (MyConfig.getDebugLevel() > level - 1) {
			LOGGER.info("L" + level + ":" + dMsg);
		}

	}

	public static void debugMsg(int level, BlockPos pos, String dMsg) {

		if (MyConfig.getDebugLevel() > level - 1) {
			LOGGER.info("L" + level + " (" + pos.getX() + "," + pos.getY() + "," + pos.getZ() + "): " + dMsg);
		}

	}
	
	

	public static void debugMsg(int level, LivingEntity le, String dMsg) {

		if (MyConfig.getDebugLevel() > level - 1) {
			LOGGER.info("L" + level + " (" 
					+ le.blockPosition().getX() + "," 
					+ le.blockPosition().getY() + ","
					+ le.blockPosition().getZ() + "): " + dMsg);
		}

	}

	public static void sendBoldChat(Player p, String chatMessage, ChatFormatting textColor) {

		MutableComponent component = Component.literal(chatMessage);
		component.setStyle(component.getStyle().withBold(true));
		component.setStyle(component.getStyle().withColor(textColor));
		p.sendSystemMessage(component);


	}

	public static void sendChat(Player p, String chatMessage) {
		sendChat (p, chatMessage, ChatFormatting.DARK_GREEN);
	}
	
	public static void sendChat(Player p, String chatMessage, ChatFormatting textColor) {

		MutableComponent component = Component.literal(chatMessage);
		component.setStyle(component.getStyle().withColor(textColor));
		p.sendSystemMessage(component);
	}
}
