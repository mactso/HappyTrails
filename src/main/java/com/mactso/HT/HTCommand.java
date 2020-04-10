package com.mactso.HT;

import com.mactso.HT.config.MyConfig;
import com.mactso.HT.config.TrailBlockManager;
import com.mactso.HT.config.TrailBlockManager.TrailBlockItem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.dispenser.Position;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
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
					World worldName = p.world;
		            ITextComponent component = new StringTextComponent (worldName.getDimension().getType().getRegistryName() 
		            		+ "\n Current Values");
		            component.applyTextStyle(TextFormatting.BOLD);
		            component.applyTextStyle(TextFormatting.DARK_GREEN);
		            p.sendMessage(component);
		            Block b = p.world.getBlockState(p.getPosition()).getBlock();
		            if (b == Blocks.AIR) {
			            b = p.world.getBlockState(p.getPosition().down()).getBlock();		            	
		            }
		            TrailBlockManager.TrailBlockItem t = TrailBlockManager.getTrailBlockInfo(b.getRegistryName().toString());
		            int speed = 0;
		            if (t != null) {
			            speed = t.getTrailBlockSpeed();
		            }
		            component = new StringTextComponent (
		              		  "\n  Speed Level...........: " + speed
		            		+ "\n  Standing On...........: " + b.getRegistryName().toString()
		              		+ "\n  Player Position.......: " + p.getPosition().toString()		            		
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
		MyConfig.pushDebugValue();
		return 1;
	}

	public static int setParticlesOn (boolean newParticlesOn) {
		MyConfig.aParticlesOn = newParticlesOn;
		MyConfig.pushNewParticlesOn();
		return 1;
	}	
	
	public static int setSpeedForBlock (ServerPlayerEntity p, int newSpeedValue) {
        Block b = p.world.getBlockState(p.getPosition()).getBlock();
        if (b == Blocks.AIR) {
            b = p.world.getBlockState(p.getPosition().down()).getBlock();		            	
        }
        String key = b.getRegistryName().toString();
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

