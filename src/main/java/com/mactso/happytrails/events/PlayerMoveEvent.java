package com.mactso.happytrails.events;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;;


@Mod.EventBusSubscriber()
public class PlayerMoveEvent {


	@SubscribeEvent
    public void PlayerMove(PlayerTickEvent event) { 

    	if (!(event.player instanceof ServerPlayer)) {
    		return;
    	}
   	
		ServerPlayer aPlayer = (ServerPlayer) event.player;
		Level level = aPlayer.level;

		int amplifier = Utility.getSpeedAmplifier(aPlayer, level);
		if (Utility.applyMovementSpeedAttribute(aPlayer, amplifier)) {
			return;
		}

		// permanent
		if (amplifier >= 1) {
			amplifier = amplifier - 1; // convert to 0 based.
			Utility.updateEffect((LivingEntity) aPlayer, amplifier, MobEffects.MOVEMENT_SPEED);
		} else if (amplifier <= -1) {
			amplifier = (-amplifier) - 1; // convert to 0 based positive value.
			Utility.updateEffect((LivingEntity) aPlayer, amplifier, MobEffects.MOVEMENT_SLOWDOWN);
		}		
	}
}
	

