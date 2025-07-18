package com.mactso.happytrails.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;

import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SteedMoveEvent {

	@SubscribeEvent
	public void SteedMove(LivingTickEvent event) {

		LivingEntity le = event.getEntity();
		Level level = le.level();

		if (level.isClientSide()) {
			return;
		}
		if (le.getFirstPassenger() == null) {
			return;
		}
		if (le.getFirstPassenger() instanceof ServerPlayer spe) {
			int amplifier = HappyUtility.getSpeedAmplifier(le, level);

			if (HappyUtility.applyMovementSpeedAttribute(le, amplifier)) {
				return;
			}

			if (amplifier >= 1) {
				if (amplifier == 10) {
					amplifier = 12; // steed plaid speed
				}
				amplifier = amplifier - 1; // convert to 0 based.
				HappyUtility.updateEffect(le, amplifier, MobEffects.SPEED);
			} else if (amplifier <= -1) {
				amplifier = (-amplifier) - 1; // convert to 0 based positive value.
				HappyUtility.updateEffect(le, amplifier, MobEffects.SLOWNESS);
			}
		}
	}
}
