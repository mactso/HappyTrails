package com.mactso.HT.events;

import com.mactso.HT.config.MyConfig;
import com.mactso.HT.config.TrailBlockManager;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;


@Mod.EventBusSubscriber()
public class MovePlayerEvent {


	@SubscribeEvent
    public void PlayerMove(PlayerTickEvent event) { 
    	final int THREE_SECONDS = 60;
    	final Block AIR = Block.getBlockFromName("AIR");
    	final Potion POTIONSPEED = Potion.getPotionById(1);
    	final Potion POTIONSLOW = Potion.getPotionById(2);
    	
    	if (!(event.player instanceof EntityPlayerMP )) {
    		return;
    	}
    	
    	
    	EntityPlayerMP aPlayer = (EntityPlayerMP ) event.player;
		World w = aPlayer.world;
		// w.getPlayers().size();

		Block b = w.getBlockState(aPlayer.getPosition()).getBlock();
        if (b == AIR ) {
            b = aPlayer.world.getBlockState(aPlayer.getPosition().down()).getBlock();		            	
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

			speed = speed - 1; // convert to 0 based.
    		
    		// This is tricky--- if the player has a more powerful effect, it sometimes
    		// sticks "on" and won't expire so remove it once it has half a second left.
 			PotionEffect ei = aPlayer.getActivePotionEffect(POTIONSPEED);
    		if (ei != null) {
    			if (ei.getDuration() > 10) {
    				return;
    			}
    			if (ei.getAmplifier() > speed) {
    				aPlayer.removeActivePotionEffect( POTIONSPEED);
    			}
    		}
			aPlayer.addPotionEffect(new PotionEffect(POTIONSPEED, THREE_SECONDS, speed, true, MyConfig.aParticlesDisplay ));
		} else if (speed <=-1) {
			speed =  (-speed ) - 1; // convert to 0 based positive value.
			PotionEffect ei = aPlayer.getActivePotionEffect(POTIONSLOW);
    		if (ei != null) {
    			if (ei.getDuration() > 10) {
    				return;
    			}
    			if (ei.getAmplifier() > speed) {
    				aPlayer.removeActivePotionEffect(POTIONSLOW );
    			}
    		}
			aPlayer.addPotionEffect(new PotionEffect(POTIONSLOW, THREE_SECONDS, speed, true, MyConfig.aParticlesDisplay ));
		}
	}
}
	

