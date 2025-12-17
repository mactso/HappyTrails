package com.mactso.happytrails.commands;

import com.mactso.happytrails.Main;
import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.managers.TrailBlockManager;
import com.mactso.happytrails.managers.TrailBlockManager.TrailBlockItem;
import com.mactso.happytrails.utility.Utility;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ModCommands {

	private static final String MOD_VERSION = " 1.21.9 ";

	private static final String ARG_DEBUG_LEVEL = "level";
	private static final String ARG_SPEED = "speed";

	// ---------------------------
	// Permission constants
	// ---------------------------
	public final class PermissionLevel {
		public static final int ALL = 0; // All players; /say, /help, /spawnpoint
		public static final int MOD = 1; // Moderators; /gamemode <mode>, /teleport <player>
		public static final int OP = 2; // Operators; /setblock, /time set, /regrowth
		public static final int ADMIN = 3; // Admins; /stop, /ban, /kick
		public static final int OWNER = 4; // Server owner; /op <player>, /deop <player>, /reload
		// No constructor needed because the class is final and contains only static
		// members
	}

	// ---------------------------
	// Command result constants
	// ---------------------------
	public final class CommandResult {
		public static final int NONE = 0; // Command had no effect
		public static final int SUCCESS = 1; // Standard success
		public static final int MULTIPLE = 2; // Multiple entities/things affected
		// No constructor needed because the class is final and contains only static
		// members
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

	    Utility.debugMsg(0, "Register " + Main.MOD_ID + " Commands");

	    dispatcher.register(
	        Commands.literal(Main.MOD_ID)
	            .requires(
	                    source ->
	                    source.isPlayer()
	                    && source.hasPermission(PermissionLevel.OP)
	            )
	            .then(
	                Commands.literal("info")
	                    .executes(ctx -> {
	                        ServerPlayer p = ctx.getSource().getPlayerOrException();
	                        doInfoReport(p);
	                        return 1;
	                    })
	            )
	            .then(
	                Commands.literal("report")
	                    .executes(ctx -> {
	                        ServerPlayer p = ctx.getSource().getPlayer();
	                        String report =
	                            "Configured Blocks : "
	                                + TrailBlockManager.getTrailHashAsString();
	                        Utility.sendChat(p, report, ChatFormatting.GREEN);
	                        return 1;
	                    })
	            )
	            .then(
	                Commands.literal("setDebugLevel")
	                    .then(
	                        Commands.argument(
	                            ARG_DEBUG_LEVEL,
	                            IntegerArgumentType.integer(0, 2)
	                        )
	                        .executes(ctx -> {
	                            ServerPlayer p = ctx.getSource().getPlayer();
	                            return setDebugLevel(
	                                p,
	                                IntegerArgumentType.getInteger(ctx, ARG_DEBUG_LEVEL)
	                            );
	                        })
	                    )
	            )
	            .then(
	                Commands.literal("setParticlesOn")
	                    .then(
	                        Commands.literal("true")
	                            .executes(ctx -> {
	                                ServerPlayer p = ctx.getSource().getPlayer();
	                                return setParticlesOn(p, true);
	                            })
	                    )
	                    .then(
	                        Commands.literal("false")
	                            .executes(ctx -> {
	                                ServerPlayer p = ctx.getSource().getPlayer();
	                                return setParticlesOn(p, false);
	                            })
	                    )
	            )
	            .then(
	                Commands.literal("setHappyTrailSpeed")
	                    .then(
	                        Commands.argument(
	                            ARG_SPEED,
	                            IntegerArgumentType.integer(-99, 99)
	                        )
	                        .executes(ctx ->
	                            setSpeedForBlock(
	                                ctx.getSource().getPlayer(),
	                                IntegerArgumentType.getInteger(ctx, ARG_SPEED)
	                            )
	                        )
	                    )
	            )
	    );
	}


	private static void doInfoReport(ServerPlayer p) {
		BlockPos playerBlockPos = p.blockPosition();
		Level worldName = p.level();

		String dimensionName = p.level().dimension().location().getPath();

		String chatMessage = "\nDimension: " + dimensionName + "\n Current Values";
		Utility.sendChat(p, chatMessage, ChatFormatting.GREEN);
		BlockState bs = p.level().getBlockState(playerBlockPos);
		Block block = p.level().getBlockState(playerBlockPos).getBlock();
		if (bs.isAir()) {
			block = p.level().getBlockState(playerBlockPos.below()).getBlock();
		}
		String key = BuiltInRegistries.BLOCK.getKey(block).toString();
		TrailBlockManager.TrailBlockItem t = TrailBlockManager.getTrailBlockInfo(key.toString());
		int speed = 0;
		if (t != null) {
			speed = t.getTrailBlockSpeed();
		}

		float temp = 0;

		String speedString = TrailBlockManager.getSpeedString(speed);

		chatMessage = "  Speed Level..............: " + speed + "\n  Speed ...........................: " + speedString
				+ "\n  Standing On...............: " + key.toString() + "\n  Player Position......: "
				+ playerBlockPos.toString() + "\n  Particles On.............: " + MyConfig.isParticlesOn()
				+ "\n  Debug Level.............: " + MyConfig.getDebugLevel();
		Utility.sendChat(p, chatMessage, ChatFormatting.AQUA);
	}

	public static int setDebugLevel(ServerPlayer p, int i) {
		MyConfig.setDebugLevel(i);
		doInfoReport(p);
		return 1;
	}

	public static int setParticlesOn(ServerPlayer p, boolean b) {
		MyConfig.setNewParticlesOn(b);
		MyConfig.diskSaveParticlesOnValue();
		doInfoReport(p);
		return 1;
	}

	public static int setSpeedForBlock(ServerPlayer p, int newSpeedValue) {
		BlockPos playerBlockPos = p.blockPosition();
		BlockState bs = p.level().getBlockState(playerBlockPos);
		Block block = p.level().getBlockState(playerBlockPos).getBlock();
		if (bs.isAir()) {
			block = p.level().getBlockState(playerBlockPos.below()).getBlock();
		}
		String key = BuiltInRegistries.BLOCK.getKey(block).toString();
		if (newSpeedValue == 0) {
			TrailBlockManager.trailBlockHashtable.remove(key);
		} else {
			TrailBlockManager.trailBlockHashtable.put(key, new TrailBlockItem(newSpeedValue));
		}

		MyConfig.diskSaveTrailBlockListValue();
		;
		doInfoReport(p);
		return 1;

	}
}
