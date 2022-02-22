package com.mactso.happytrails.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SteedMoveEvent {

	@SubscribeEvent
	public void SteedMove(LivingUpdateEvent event) {

		LivingEntity le = event.getEntityLiving();
		Level level = le.level;

		if (level.isClientSide()) {
			return;
		}
		if (le.getFirstPassenger() == null) {
			return;
		}
		if (le.getFirstPassenger() instanceof ServerPlayer spe) {
			int amplifier = Utility.getSpeedAmplifier(le, level);

			if (Utility.applyMovementSpeedAttribute(le, amplifier)) {
				return;
			}

			if (amplifier >= 1) {
				if (amplifier == 10) {
					amplifier = 12; // steed plaid speed
				}
				amplifier = amplifier - 1; // convert to 0 based.
				Utility.updateEffect(le, amplifier, MobEffects.MOVEMENT_SPEED);
			} else if (amplifier <= -1) {
				amplifier = (-amplifier) - 1; // convert to 0 based positive value.
				Utility.updateEffect(le, amplifier, MobEffects.MOVEMENT_SLOWDOWN);
			}
		}
	}
}
