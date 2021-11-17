package com.mactso.happytrails.events;
import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.config.TrailBlockManager;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;;


@Mod.EventBusSubscriber()
public class PlayerMoveEvent {


	@SubscribeEvent
    public void PlayerMove(PlayerTickEvent event) { 
    	final int THREE_SECONDS = 60;

    	if (!(event.player instanceof ServerPlayer)) {
    		return;
    	}
   	
		ServerPlayer aPlayer = (ServerPlayer) event.player;
		Level w = aPlayer.level;
		// w.getPlayers().size();
		BlockPos playerBlockPos = aPlayer.blockPosition();
		Block b = w.getBlockState(playerBlockPos).getBlock();
        if (b == Blocks.AIR) {
            b = aPlayer.level.getBlockState(playerBlockPos.below()).getBlock();		            	
        } 
        
		//String registeryName = b.getRegistryName().toString();
			TrailBlockManager.TrailBlockItem t = TrailBlockManager.getTrailBlockInfo(b.getRegistryName().toString());
		
		if (t==null) {  // standing on block with no configuration entry
			return;
		}

		int speed = t.getTrailBlockSpeed();
		if (speed == 0) { // Happy Trails (temporarily) Disabled for Entry
			return;
		}

		if (speed >= 1) {
			if (speed == 11) {
				speed = 22;
			}
			speed = speed - 1; // convert to 0 based.
    		
    		// This is tricky--- if the player has a more powerful effect, it sometimes
    		// sticks "on" and won't expire so remove it once it has half a second left.
 			MobEffectInstance ei = aPlayer.getEffect(MobEffects.MOVEMENT_SPEED);
    		if (ei != null) {
    			// handle going from a slower road to a faster road.
    			if (speed > ei.getAmplifier()) {
    				aPlayer.removeEffect(MobEffects.MOVEMENT_SPEED);
    			} else {
    				if (ei.getDuration() > 10) {
    					return;
    				}
    				if (ei.getAmplifier() > speed) {
        				aPlayer.removeEffect(MobEffects.MOVEMENT_SPEED);
    				}
    			}
    		}
			aPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, THREE_SECONDS, speed, true, MyConfig.aParticlesOn  ));
		} else if (speed <=-1) {
			speed =  (-speed ) - 1; // convert to 0 based positive value.
			MobEffectInstance ei = aPlayer.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
    		if (ei != null) {
    			if (ei.getDuration() > 10) {
    				return;
    			}
    			if (ei.getAmplifier() > speed) {
    				aPlayer.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
    			}
    		}
			aPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, THREE_SECONDS, speed, true, MyConfig.aParticlesOn ));
		}
	}
}
	

