package com.mactso.happytrails.events;
import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.config.TrailBlockManager;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;;


@Mod.EventBusSubscriber()
public class PlayerMoveEvent {


	@SubscribeEvent
    public void PlayerMove(PlayerTickEvent event) { 
    	final int THREE_SECONDS = 60;

    	if (!(event.player instanceof ServerPlayerEntity)) {
    		return;
    	}
   	
		ServerPlayerEntity aPlayer = (ServerPlayerEntity) event.player;
		World w = aPlayer.world;
		// w.getPlayers().size();
		BlockPos playerBlockPos = aPlayer.func_233580_cy_();
		Block b = w.getBlockState(playerBlockPos).getBlock();
        if (b == Blocks.AIR) {
            b = aPlayer.world.getBlockState(playerBlockPos.down()).getBlock();		            	
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
 			EffectInstance ei = aPlayer.getActivePotionEffect(Effects.SPEED);
    		if (ei != null) {
    			if (ei.getDuration() > 10) {
    				return;
    			}
    			if (ei.getAmplifier() > speed) {
    				aPlayer.removeActivePotionEffect(Effects.SPEED );
    			}
    		}
			aPlayer.addPotionEffect(new EffectInstance(Effects.SPEED, THREE_SECONDS, speed, true, MyConfig.aParticlesOn  ));
		} else if (speed <=-1) {
			speed =  (-speed ) - 1; // convert to 0 based positive value.
			EffectInstance ei = aPlayer.getActivePotionEffect(Effects.SLOWNESS);
    		if (ei != null) {
    			if (ei.getDuration() > 10) {
    				return;
    			}
    			if (ei.getAmplifier() > speed) {
    				aPlayer.removeActivePotionEffect(Effects.SLOWNESS );
    			}
    		}
			aPlayer.addPotionEffect(new EffectInstance(Effects.SLOWNESS, THREE_SECONDS, speed, true, MyConfig.aParticlesOn ));
		}
	}
}
	

