package com.mactso.happytrails;


import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.config.TrailBlockManager;
import com.mactso.happytrails.config.TrailBlockManager.TrailBlockItem;
import com.mactso.happytrails.utility.Utility;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class HappyTrailsCommands {
	String subcommand = "";
	String value = "";
	
	
//	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated)
//	{
//		System.out.println("Register Happy Trails Commands");
//		dispatcher.register(CommandManager.literal("happytrails").requires((source) -> 
//			{
//				return source.hasPermissionLevel(2);
//			}
//		)
//		.then(CommandManager.literal("debugLevel").then(
//				CommandManager.argument("debugLevel", IntegerArgumentType.integer(0,2)).executes(ctx -> {
//					return setDebugLevel(IntegerArgumentType.getInteger(ctx, "debugLevel"));
//			}
//			)
//			)
//			)
//		.then(CommandManager.literal("info").executes(ctx -> {
//			ServerPlayerEntity p = ctx.getSource().getPlayer(); // or exception!
//					World world = p.world;
//					String objectInfo = "";
//
//					if (p.world.isClient()) {
//						MinecraftClient mc = MinecraftClient.getInstance();
//	                    HitResult object = mc.crosshairTarget;
//	                    if (object instanceof EntityHitResult) {
//	                    	EntityHitResult ertr = (EntityHitResult) object;
//	                    	Entity tempEntity = ertr.getEntity();
//	                    	objectInfo = Utility.getResourceLocationString(tempEntity);
//	                     } else {
//	                   		objectInfo = "You are not looking at an entity.";	                    	 
//	                     }
//					} else {
//						objectInfo = "Load single player game to see entity you are looking at.";
//					}
//
//
//					Utility.sendBoldChat(p, Utility.getResourceLocationString(world).toUpperCase()
//		            		+ "\n Current Values", TextColor.fromFormatting(Formatting.DARK_GREEN));
//
//		            String msg = 
//		              		  "\n  Happy Trails (Fabric) "  
//		            		+ "\n  Debug Level...........: " + MyConfig.getDebugLevel()
//		            		+ "\n  Looking At................: "  + objectInfo;
//		            Utility.sendChat(p, msg, TextColor.fromFormatting(Formatting.DARK_GREEN));
//					return 1;
//			}
//			)
//			)		
//		);
//		System.out.println("Exit Register");
//	}


	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated)
	{
		System.out.println("Register Happy Trails Commands");
		dispatcher.register(CommandManager.literal("happytrails").requires((source) -> 
			{
				return source.hasPermissionLevel(2);
			}
		)
		.then(CommandManager.literal("debugLevel").then(
				CommandManager.argument("debugLevel", IntegerArgumentType.integer(0,2)).executes(ctx -> {
					return setDebugLevel(IntegerArgumentType.getInteger(ctx, "debugLevel"));
			}
			)
			)
			)
		.then(CommandManager.literal("particlesOn").then(
				CommandManager.literal("true").executes(ctx -> {
					return setParticlesOn(true);
				}
				)).then (
						CommandManager.literal("false").executes(ctx -> {
					return setParticlesOn(false);
			}
			)
			)
			)
		// update or add a speed value for the block the player is standing on.
		.then(CommandManager.literal("setHappyTrailSpeed").then(
				CommandManager.argument("setHappyTrailSpeed", IntegerArgumentType.integer(-99,99)).executes(ctx -> {
					return setSpeedForBlock((ctx.getSource()).getPlayer(), IntegerArgumentType.getInteger(ctx, "setHappyTrailSpeed"));
			}
			)
			)
			)		
		.then(CommandManager.literal("report").executes(ctx -> {
			ServerPlayerEntity p = ctx.getSource().getPlayer();
			String report = "Configured Blocks : " + TrailBlockManager.getTrailHashAsString();
	        Utility.sendChat (p,report,TextColor.fromFormatting(Formatting.GREEN));
			return 1;
		}
		)
		)
		.then(CommandManager.literal("info").executes(ctx -> {
					ServerPlayerEntity p = ctx.getSource().getPlayer();
					BlockPos playerBlockPos = p.getBlockPos();
					World worldName = p.world;
					
					
			        String dimensionName = p.world.getRegistryKey().getValue().toString();
			        String chatMessage = "Dimension: " + dimensionName + "\n Current Values";
			        Utility.sendBoldChat (p,chatMessage, TextColor.fromFormatting(Formatting.DARK_GREEN));
			        BlockState bs = p.world.getBlockState(playerBlockPos);
		            Block b = p.world.getBlockState(playerBlockPos).getBlock();
		            if (bs.isAir()) {
			            b = p.world.getBlockState(playerBlockPos.down()).getBlock();		            	
		            }
		            TrailBlockManager.TrailBlockItem t = TrailBlockManager.getTrailBlockInfo(b.getRegistryEntry().getKey().get().getValue().toString());
		            int speed = 0;
		            if (t != null) {
			            speed = t.getTrailBlockSpeed();
		            }
		            chatMessage = 
		              		  "\n  Speed Level...........: " + speed
		            		+ "\n  Standing On...........: " + b.getRegistryEntry().getKey().get().getValue().toString()
		              		+ "\n  Player Position.......: " + playerBlockPos.toString()		            		
		              		+ "\n  Debug Level...........: " + MyConfig.getDebugLevel()
		            		;
			        Utility.sendChat (p,chatMessage,TextColor.fromFormatting(Formatting.GREEN));
					return 1;
					// return 1;
			}
			)
			)		
		);

	}
	
	public static int setDebugLevel (int i) {
		MyConfig.setDebugLevel (i);
		return 1;
	}

	public static int setParticlesOn (boolean b) {
		MyConfig.setParticlesOn(b);;
		return 1;
	}	
	
	public static int setSpeedForBlock (ServerPlayerEntity p, int newSpeedValue) {
		BlockPos playerBlockPos = p.getBlockPos();
        BlockState bs = p.world.getBlockState(playerBlockPos);
        Block block = p.world.getBlockState(playerBlockPos).getBlock();
        if (bs.isAir()) {
            block = p.world.getBlockState(playerBlockPos.down()).getBlock();		            	
        }
        String key = block.getRegistryEntry().getKey().toString();
        if (newSpeedValue  == 0 ) {
        	TrailBlockManager.trailBlockHashtable.remove(key);
        } else {
        	TrailBlockManager.trailBlockHashtable.put(key, new TrailBlockItem(newSpeedValue));
        }
        
        String chatMessage = 
        		  "Updating configuration file while running not supported yet."
        		+ "\n Change not saved ot configuration file." 	
      		;
      Utility.sendChat (p,chatMessage,TextColor.fromFormatting(Formatting.GREEN));
//		MyConfig.pushValues();
		return 1;
	}
}

