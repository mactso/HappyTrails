package com.mactso.HT;

import com.mactso.HT.config.MyConfig;
import com.mactso.HT.config.TrailBlockManager;
import com.mactso.HT.config.TrailBlockManager.TrailBlockItem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
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
				Commands.argument("setHappyTrailSpeed", IntegerArgumentType.integer(-11,11)).executes(ctx -> {
					return setSpeedForBlock(ctx.getSource().asPlayer(), IntegerArgumentType.getInteger(ctx, "setHappyTrailSpeed"));
			}
			)
			)
			)		
		.then(Commands.literal("info").executes(ctx -> {
					ServerPlayerEntity p = ctx.getSource().asPlayer();
					BlockPos playerBlockPos = p.func_233580_cy_();
					World worldName = p.world;
			        DimensionType dimensionType = p.world.func_230315_m_();
			        int dimensionNumber = dimensionType.func_241513_m_();
			        String dimensionName = p.world.func_234923_W_().func_240901_a_().toString();
			        
			        String chatMessage = "Dimension: " + dimensionName + "\n Current Values";
			        MyConfig.sendChat (p,chatMessage,TextFormatting.DARK_GREEN, MyConfig.BOLD);
			        
		            Block b = p.world.getBlockState(playerBlockPos).getBlock();
		            if (b == Blocks.AIR) {
			            b = p.world.getBlockState(playerBlockPos.down()).getBlock();		            	
		            }
		            TrailBlockManager.TrailBlockItem t = TrailBlockManager.getTrailBlockInfo(b.getRegistryName().toString());
		            int speed = 0;
		            if (t != null) {
			            speed = t.getTrailBlockSpeed();
		            }
		            chatMessage = 
		              		  "\n  Speed Level...........: " + speed
		            		+ "\n  Standing On...........: " + b.getRegistryName().toString()
		              		+ "\n  Player Position.......: " + playerBlockPos.toString()		            		
		              		+ "\n  Debug Level...........: " + MyConfig.aDebugLevel	
		            		;
			        MyConfig.sendChat (p,chatMessage);
					return 1;
					// return 1;
			}
			)
			)		
		);

	}
	
	public static int setDebugLevel (int newDebugLevel) {
		MyConfig.aDebugLevel = newDebugLevel;
		MyConfig.pushDebugValue();
		return 1;
	}

	public static int setParticlesOn (boolean newParticlesOn) {
		MyConfig.aParticlesOn = newParticlesOn;
		MyConfig.pushNewParticlesOn();
		return 1;
	}	
	
	public static int setSpeedForBlock (ServerPlayerEntity p, int newSpeedValue) {
		BlockPos playerBlockPos = p.func_233580_cy_();
        Block block = p.world.getBlockState(playerBlockPos).getBlock();
        if (block == Blocks.AIR) {
            block = p.world.getBlockState(playerBlockPos.down()).getBlock();		            	
        }
        String key = block.getRegistryName().toString();
        TrailBlockManager.TrailBlockItem oldT = null;
        if (newSpeedValue  == 0 ) {
        	TrailBlockManager.trailBlockHashtable.remove(key);
        } else {
        	oldT = TrailBlockManager.trailBlockHashtable.put(key, new TrailBlockItem(newSpeedValue));
        }
		MyConfig.pushValues();
		return 1;
	}
}

