package com.mactso.happytrails;

import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.config.TrailBlockManager;
import com.mactso.happytrails.config.TrailBlockManager.TrailBlockItem;
import com.mactso.happytrails.utility.Utility;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class HappyTrailsCommands {
	String subcommand = "";
	String value = "";

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("happytrails").requires((source) -> {
			return source.hasPermission(2);
		}).then(Commands.literal("debugLevel")
				.then(Commands.argument("debugLevel", IntegerArgumentType.integer(0, 2)).executes(ctx -> {
					return setDebugLevel(IntegerArgumentType.getInteger(ctx, "debugLevel"));
				}))).then(Commands.literal("particlesOn").then(Commands.literal("true").executes(ctx -> {
					return setParticlesOn(true);
				})).then(Commands.literal("false").executes(ctx -> {
					return setParticlesOn(false);
				})))
				// update or add a speed value for the block the player is standing on.
				.then(Commands.literal("setHappyTrailSpeed").then(
						Commands.argument("setHappyTrailSpeed", IntegerArgumentType.integer(-99, 99)).executes(ctx -> {
							return setSpeedForBlock(ctx.getSource().getPlayerOrException(),
									IntegerArgumentType.getInteger(ctx, "setHappyTrailSpeed"));
				})))
				.then(Commands.literal("report").executes(ctx -> {
					ServerPlayer serverPlayer = ctx.getSource().getPlayerOrException();
					String chatMessage = "\nConfigured Blocks :" ;
					Utility.sendChat( serverPlayer, chatMessage, ChatFormatting.AQUA);
					chatMessage = "Spd  Meters per Sec.  Blocks" ;
					Utility.sendChat( serverPlayer, chatMessage, ChatFormatting.AQUA);
					chatMessage = TrailBlockManager.getTrailBlockReport();
					Utility.sendChat( serverPlayer, chatMessage, ChatFormatting.GREEN);
					return 1;
				}))
				.then(Commands.literal("info").executes(ctx -> {
					ServerPlayer serverPlayer = ctx.getSource().getPlayerOrException();
					doInfoReport(serverPlayer);
					return 1;
					// return 1;
				})));

	}

	private static void doInfoReport(ServerPlayer serverPlayer) {

		BlockPos playerBlockPos = serverPlayer.blockPosition();
		String dimensionName = serverPlayer.level().dimension().location().getPath();

		String chatMessage = "\nDimension: " + dimensionName + "\n Current Values";
		Utility.sendChat( serverPlayer, chatMessage, ChatFormatting.GREEN);

		BlockState bs = serverPlayer.level().getBlockState(playerBlockPos);
		Block block = bs.getBlock();
		if (bs.isAir()) {
			block = serverPlayer.level().getBlockState(playerBlockPos.below()).getBlock();
		}

		String keyString = BuiltInRegistries.BLOCK.getKey(block).toString();
		TrailBlockManager.TrailBlockItem t = TrailBlockManager.getTrailBlockInfo(keyString);
		int speed = 0;
		if (t != null) {
			speed = t.getTrailBlockSpeed();
		}


		chatMessage = "  Speed Level..............: " + speed 
				+ "\n  Speed ...........................: " + TrailBlockManager.getSpeedString(speed)
				+ "\n  Standing On...............: " + keyString
				+ "\n  Player Position......: " + playerBlockPos.toString() 
				+ "\n  Debug Level.............: "
				+ MyConfig.aDebugLevel 	;
		Utility.sendChat( serverPlayer, chatMessage, ChatFormatting.AQUA);
	}


	public static int setDebugLevel(int newDebugLevel) {
		MyConfig.aDebugLevel = newDebugLevel;
		MyConfig.pushDebugValue();
		return 1;
	}

	public static int setParticlesOn(boolean newParticlesOn) {
		MyConfig.aParticlesOn = newParticlesOn;
		MyConfig.pushNewParticlesOn();
		return 1;
	}

	public static int setSpeedForBlock(ServerPlayer serverPlayer, int newSpeedValue) {
		BlockPos playerBlockPos = serverPlayer.blockPosition();
		BlockState bs = serverPlayer.level().getBlockState(playerBlockPos);
		Block block = serverPlayer.level().getBlockState(playerBlockPos).getBlock();
		if (bs.isAir()) {
			block = serverPlayer.level().getBlockState(playerBlockPos.below()).getBlock();
		}
		String keyString = BuiltInRegistries.BLOCK.getKey(block).toString();
		if (newSpeedValue == 0) {
			TrailBlockManager.trailBlockHashtable.remove(keyString);
		} else {
			TrailBlockManager.trailBlockHashtable.put(keyString, new TrailBlockItem(newSpeedValue));
		}
		MyConfig.pushValues();
		doInfoReport(serverPlayer);
		return 1;
	}
}
