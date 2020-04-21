package com.mactso.happytrails.events;

import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.config.TrailBlockManager;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
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
    	final int ONE_SECONDS = 20;
    	final Potion POTIONSPEED = Potion.getPotionById(1);
    	final Potion POTIONSLOW = Potion.getPotionById(2);
    	
    	Block block;
    	IBlockState bs;
    	int meta = 0;
    	
    	if (!(event.player instanceof EntityPlayerMP )) {
    		return;
    	}
    	
    	
    	EntityPlayerMP aPlayer = (EntityPlayerMP ) event.player;
		World w = aPlayer.world;
		// w.getPlayers().size();

		bs = aPlayer.world.getBlockState(aPlayer.getPosition());
		block = bs.getBlock();
		meta = block.getMetaFromState(bs);
		if (block instanceof BlockAir) {
			bs = aPlayer.world.getBlockState(aPlayer.getPosition().down());
			block = bs.getBlock();
			meta = block.getMetaFromState(bs);
		}
		String trailKey = block.getRegistryName().toString() + ">" + Integer.toString(meta);
		
		//String registeryName = b.getRegistryName().toString();
		TrailBlockManager.TrailBlockItem t = TrailBlockManager.getTrailBlockInfo(trailKey);
		
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
			aPlayer.addPotionEffect(new PotionEffect(POTIONSPEED, ONE_SECONDS, speed, true, MyConfig.aParticlesDisplay ));
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
			aPlayer.addPotionEffect(new PotionEffect(POTIONSLOW, ONE_SECONDS, speed, true, MyConfig.aParticlesDisplay ));
		}
	}
}
	

