package com.mactso.HT;

import com.mactso.HT.config.MyConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class HTCommand {
	String subcommand = "";
	String value = "";
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("happytrails").requires((source) -> 
			{
				return source.hasPermissionLevel(2);
			}
		)
		.then(Commands.literal("debugLevel").then(
				Commands.argument("debugLevel", IntegerArgumentType.integer(0,2)).executes(ctx -> {
					return setDebugLevel(IntegerArgumentType.getInteger(ctx, "debugLevel"));
			}
			)
			)
			)
		// update or add a speed value for the block the player is standing on.
		.then(Commands.literal("setHappyTrailSpeed").then(
				Commands.argument("setHappyTrailSpeed", IntegerArgumentType.integer(-11,11)).executes(ctx -> {
					return setSpeedForBlock(ctx.getSource().asPlayer(), IntegerArgumentType.getInteger(ctx, "setHappyTrailSpeed"));
			}
			)
			)
			)		
		.then(Commands.literal("listSpeeds").executes(ctx -> {
					ServerPlayerEntity p = ctx.getSource().asPlayer();
					World worldName = p.world;
		            ITextComponent component = new StringTextComponent (worldName.getDimension().getType().getRegistryName() 
		            		+ "\n Current Values");
		            component.applyTextStyle(TextFormatting.BOLD);
		            component.applyTextStyle(TextFormatting.DARK_GREEN);
		            p.sendMessage(component);
		            Block b = p.world.getBlockState(p.getPosition().down()).getBlock();
		            component = new StringTextComponent (
		              		  "\n  Speed Level...........: " + MyConfig.aHappyTrailSpeed
		            		+ "\n  Standing On...........: " + b.getRegistryName().toString()
		              		+ "\n  Debug Level...........: " + MyConfig.aDebugLevel		            		
		            		);
		            component.applyTextStyle(TextFormatting.DARK_GREEN);
		            p.sendMessage(component);
					return 1;
					// return 1;
			}
			)
			)		
		);

	}
	
	public static int setDebugLevel (int newDebugLevel) {
		MyConfig.aDebugLevel = newDebugLevel;
		MyConfig.pushValues();
		return 1;
	}
	
	public static int setSpeedForBlock (ServerPlayerEntity p, int newSpeedValue) {
		MyConfig.aHappyTrailSpeed= newSpeedValue;
		MyConfig.pushValues();
		return 1;
	}
}

