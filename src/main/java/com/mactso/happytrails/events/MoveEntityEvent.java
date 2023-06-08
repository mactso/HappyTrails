package com.mactso.happytrails.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class MoveEntityEvent {

	public static void handleEntityMoveEvents(LivingEntity le) {

		if (le instanceof PlayerEntity) {
			handlePlayerMove((PlayerEntity) le);
			return;
		}
		handleSteedMove (le);
		
}
		private static void handlePlayerMove ( PlayerEntity p) {
	    	if (!(p instanceof ServerPlayerEntity)) {
	    		return;
	    	}
	   	
			ServerPlayerEntity sp = (ServerPlayerEntity) p;
			World level = sp.world;

			int amplifier = MovementUtility.getSpeedAmplifier(sp, level);
			
			
			if (MovementUtility.applyMovementSpeedAttribute(sp, amplifier)) {
				return;
			}

			// permanent
			if (amplifier >= 1) {
				amplifier = amplifier - 1; // convert to 0 based.
				MovementUtility.updateEffect((LivingEntity) sp, amplifier, StatusEffects.SPEED);
			} else if (amplifier <= -1) {
				amplifier = (-amplifier) - 1; // convert to 0 based positive value.
				MovementUtility.updateEffect((LivingEntity) sp, amplifier, StatusEffects.SLOWNESS);
			}	
		}

		private static void handleSteedMove ( LivingEntity le) {


			World level = le.world;

			if (level.isClient()) {
				return;
			}
			if (le.getFirstPassenger() == null) {
				return;
			}
			if (le.getFirstPassenger() instanceof ServerPlayerEntity spe) {
				int amplifier = MovementUtility.getSpeedAmplifier(le, level);

				if (MovementUtility.applyMovementSpeedAttribute(le, amplifier)) {
					return;
				}

				if (amplifier >= 1) {
					if (amplifier == 10) {
						amplifier = 12; // steed plaid speed
					}
					amplifier = amplifier - 1; // convert to 0 based.
					MovementUtility.updateEffect(le, amplifier, StatusEffects.SPEED);
				} else if (amplifier <= -1) {
					amplifier = (-amplifier) - 1; // convert to 0 based positive value.
					MovementUtility.updateEffect(le, amplifier, StatusEffects.SLOWNESS);
				}
			}
		}

}

