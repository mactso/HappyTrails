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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;



public class HappyTrailsCommands {
	String subcommand = "";
	String value = "";
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		System.out.println("Register "+Main.MOD_ID +" Commands");
		dispatcher.register(Commands.literal(Main.MOD_ID).requires((source) -> 
			{
				return source.hasPermission(2);
			}
		)
		.then(Commands.literal("debugLevel").then(
				Commands.argument("debugLevel", IntegerArgumentType.integer(0,2)).executes(ctx -> {
					return setDebugLevel(IntegerArgumentType.getInteger(ctx, "debugLevel"));
			}
			)
			)
			)
		.then(Commands.literal("particlesOn").then(
				Commands.literal("true").executes(ctx -> {
					return setParticlesOn(true);
				}
				)).then (
						Commands.literal("false").executes(ctx -> {
					return setParticlesOn(false);
			}
			)
			)
			)
		// update or add a speed value for the block the player is standing on.
		.then(Commands.literal("setHappyTrailSpeed").then(
				Commands.argument("setHappyTrailSpeed", IntegerArgumentType.integer(-99,99)).executes(ctx -> {
					return setSpeedForBlock((ctx.getSource()).getPlayer(), IntegerArgumentType.getInteger(ctx, "setHappyTrailSpeed"));
			}
			)
			)
			)		
		.then(Commands.literal("report").executes(ctx -> {
			ServerPlayer p = ctx.getSource().getPlayer();
			String report = "Configured Blocks : " + TrailBlockManager.getTrailHashAsString();
	        Utility.sendChat(p, report, ChatFormatting.GREEN);
			return 1;
		}
		)
		)
		.then(Commands.literal("info").executes(ctx -> {
			ServerPlayer p = ctx.getSource().getPlayerOrException();
			doInfoReport(p);
			return 1;
			}
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
		Block b = p.level().getBlockState(playerBlockPos).getBlock();
		if (bs.isAir()) {
			b = p.level().getBlockState(playerBlockPos.below()).getBlock();
		}
		ResourceLocation key = b.builtInRegistryHolder().key().location();
		TrailBlockManager.TrailBlockItem t = TrailBlockManager.getTrailBlockInfo(key.toString());
		int speed = 0;
		if (t != null) {
			speed = t.getTrailBlockSpeed();
		}

		float temp = 0;

		String speedString = TrailBlockManager.getSpeedString(speed);

		chatMessage = "  Speed Level..............: " + speed 
				+ "\n  Speed ...........................: " + speedString
				+ "\n  Standing On...............: " + key.toString()
				+ "\n  Player Position......: " + playerBlockPos.toString() 
				+ "\n  Debug Level.............: "
				+ MyConfig.getDebugLevel();
        Utility.sendChat(p, chatMessage, ChatFormatting.AQUA);
	}
	
	public static int setDebugLevel (int i) {
		MyConfig.setDebugLevel (i);
		return 1;
	}

	public static int setParticlesOn (boolean b) {
		MyConfig.setParticlesOn(b);;
		return 1;
	}	
	
	public static int setSpeedForBlock (ServerPlayer p, int newSpeedValue) {
		BlockPos playerBlockPos = p.blockPosition();
        BlockState bs = p.level().getBlockState(playerBlockPos);
        Block block = p.level().getBlockState(playerBlockPos).getBlock();
        if (bs.isAir()) {
            block = p.level().getBlockState(playerBlockPos.below()).getBlock();		            	
        }
		String key = block.builtInRegistryHolder().key().location().toString();
        if (newSpeedValue  == 0 ) {
        	TrailBlockManager.trailBlockHashtable.remove(key);
        } else {
        	TrailBlockManager.trailBlockHashtable.put(key, new TrailBlockItem(newSpeedValue));
        }
        
        String chatMessage = 
        		  "Updating configuration file while running not supported yet."
        		+ "\n Change not saved ot configuration file." 	
      		;
        Utility.sendChat(p, chatMessage, ChatFormatting.GREEN);
//		MyConfig.pushValues();
		return 1;
	}
}

