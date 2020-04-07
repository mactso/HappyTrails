package com.mactso.HT.events;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.mactso.HT.config.MyConfig;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;;


@Mod.EventBusSubscriber()
public class PlayerMoveEvent {
	private static int performanceImpactLimiter = 0;
	
	@SubscribeEvent
    public void PlayerMove(PlayerTickEvent event) { 
    	final int THREE_SECONDS = 60;

    	// Only check 4 times per second to limit server impact.
    	if (performanceImpactLimiter ++ < 5) {
    		return;
    	}
    	performanceImpactLimiter = 0;
    	
    	if (event.player instanceof ServerPlayerEntity) {
    		ServerPlayerEntity aPlayer = (ServerPlayerEntity) event.player;
    		World w = aPlayer.world;    		
    		Block b = w.getBlockState(aPlayer.getPosition().down()).getBlock();
    		if (b == Blocks.GRASS_PATH) {
    			if (MyConfig.aHappyTrailSpeed == 0) { // disabled
    				return;
    			} else if (MyConfig.aHappyTrailSpeed > 0) {
    				int speed = MyConfig.aHappyTrailSpeed - 1; // effects are 0 based.
    				aPlayer.addPotionEffect(new EffectInstance(Effects.SPEED, THREE_SECONDS, MyConfig.aHappyTrailSpeed ));
    			} else if (MyConfig.aHappyTrailSpeed<0) {
       				int speed = 0 - MyConfig.aHappyTrailSpeed + 1; // effects are 0 based.
    				aPlayer.addPotionEffect(new EffectInstance(Effects.SLOWNESS, THREE_SECONDS, 1 - MyConfig.aHappyTrailSpeed ));
    			}
    		}
    	}
    	
    }
}
