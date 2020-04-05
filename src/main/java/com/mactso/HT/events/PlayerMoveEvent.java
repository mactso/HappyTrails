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
    @SubscribeEvent
    public void PlayerMove(PlayerTickEvent event) { 
    	final int SIX_SECONDS = 120;
    	if (event.player instanceof ServerPlayerEntity) {
    		ServerPlayerEntity aPlayer = (ServerPlayerEntity) event.player;
    		World w = aPlayer.world;    		
    		Block b = w.getBlockState(aPlayer.getPosition().down()).getBlock();
    		if (b == Blocks.GRASS_PATH) {
    			aPlayer.addPotionEffect(new EffectInstance(Effects.SPEED, SIX_SECONDS, MyConfig.aHappyTrailSpeed ));
    		}
    	}
    	
    }
}
