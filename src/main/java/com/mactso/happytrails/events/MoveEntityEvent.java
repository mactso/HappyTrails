package com.mactso.happytrails.events;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class MoveEntityEvent {

	public static void handleEntityMoveEvents(LivingEntity le) {

		if (le instanceof Player) {
			handlePlayerMove((Player) le);
			return;
		}
		handleSteedMove (le);
		
}
		private static void handlePlayerMove ( Player p) {
	    	if (!(p instanceof ServerPlayer)) {
	    		return;
	    	}
	   	
			ServerPlayer sp = (ServerPlayer) p;
			Level level = sp.level;

			int amplifier = MovementUtility.getSpeedAmplifier(sp, level);
			
			
			if (MovementUtility.applyMovementSpeedAttribute(sp, amplifier)) {
				return;
			}

			// permanent
			if (amplifier >= 1) {
				amplifier = amplifier - 1; // convert to 0 based.
				MovementUtility.updateEffect((LivingEntity) sp, amplifier, MobEffects.MOVEMENT_SPEED);
			} else if (amplifier <= -1) {
				amplifier = (-amplifier) - 1; // convert to 0 based positive value.
				MovementUtility.updateEffect((LivingEntity) sp, amplifier, MobEffects.MOVEMENT_SLOWDOWN);
			}	
		}

		private static void handleSteedMove ( LivingEntity le) {


			Level level = le.level;

			if (level.isClientSide()) {
				return;
			}
			if (le.getFirstPassenger() == null) {
				return;
			}
			if (le.getFirstPassenger() instanceof ServerPlayer spe) {
				int amplifier = MovementUtility.getSpeedAmplifier(le, level);

				if (MovementUtility.applyMovementSpeedAttribute(le, amplifier)) {
					return;
				}

				if (amplifier >= 1) {
					if (amplifier == 10) {
						amplifier = 12; // steed plaid speed
					}
					amplifier = amplifier - 1; // convert to 0 based.
					MovementUtility.updateEffect(le, amplifier, MobEffects.MOVEMENT_SPEED);
				} else if (amplifier <= -1) {
					amplifier = (-amplifier) - 1; // convert to 0 based positive value.
					MovementUtility.updateEffect(le, amplifier, MobEffects.MOVEMENT_SLOWDOWN);
				}
			}
		}

}

