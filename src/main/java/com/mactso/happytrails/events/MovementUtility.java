package com.mactso.happytrails.events;

import java.util.Optional;

import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.managers.TrailBlockManager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class MovementUtility {
	final static int TWO_SECONDS = 40;

	// from Piglin code to modify baby speed (1.21.1 on)
	private static final Identifier HAPPYMODSPEED_ID = Identifier.fromNamespaceAndPath("happytrails", "speedmod");
	public static final AttributeModifier HAPPYMODSPEED_ATTR = new AttributeModifier(HAPPYMODSPEED_ID, 1.0,
			Operation.ADD_MULTIPLIED_BASE);

	public static boolean applyMovementSpeedAttribute(LivingEntity le, int amplifier) {

		AttributeModifier am = le.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(HAPPYMODSPEED_ID);
		if (Math.abs(amplifier) <= 10) {
			if (am != null) {
				le.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(HAPPYMODSPEED_ATTR);
			}
			return false;
		}

		double amp = 0.0d;
		if (amplifier < -10) {
			amplifier += 10;
		} else if (amplifier > 10) {
			amplifier -= 10;
		}

		if (amplifier == 99)
			amplifier = 225; // plaid speed
		double modAmp = (double) ((amplifier) / 2.0d);
		if (amplifier < 0) {
			modAmp = (double) (amplifier * 0.01d);
		}
		if (modAmp > 0)
			modAmp *= 0.12f;
		if (modAmp < 0)
			modAmp *= 0.25f;

		if (am != null) {
			amp = am.amount();
		}

		if (modAmp == amp) {
			return true;
		}

		if (modAmp == 0.0) {
			le.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(HAPPYMODSPEED_ID);
			return true;
		}
		le.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(HAPPYMODSPEED_ID);
		AttributeModifier hms = new AttributeModifier(HAPPYMODSPEED_ID, modAmp, Operation.ADD_MULTIPLIED_BASE);
		le.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(hms);
		return true;

	}

	public static void applyMovementSlownessAttribute(LivingEntity le, int amplifier) {

	}

	public static void updateEffect(LivingEntity e, int amplifier, Holder<MobEffect> mobEffect) {
		MobEffectInstance ei = e.getEffect(mobEffect);
		if (amplifier == 10) {
			amplifier = 20; // player "plaid" speed.
		}
		if (ei != null) {
			if (amplifier > ei.getAmplifier()) {
				e.removeEffect(mobEffect);
			}
			if (amplifier == ei.getAmplifier() && ei.getDuration() > 10) {
				return;
			}
			if (ei.getDuration() > 10) {
				return;
			}
			e.removeEffect(mobEffect);
		}
		e.addEffect(new MobEffectInstance(mobEffect, TWO_SECONDS, amplifier, true, MyConfig.isParticlesOn()));
		return;
	}

	public static int getSpeedAmplifier(LivingEntity le, Level level) {
	    BlockPos pos = le.blockPosition();

	    int speed = getTrailSpeedAt(level, pos);
	    if (speed != 0) return speed;

	    return getTrailSpeedAt(level, pos.below());
	}
	
	private static int getTrailSpeedAt(Level level, BlockPos pos) {
	    Block block = level.getBlockState(pos).getBlock();
	    Optional<ResourceKey<Block>> optKey = BuiltInRegistries.BLOCK.getResourceKey(block);
	    if (optKey.isPresent()) {
	        TrailBlockManager.TrailBlockItem t = TrailBlockManager.getTrailBlockInfo(optKey.get().identifier().toString());
	        if (t != null) {
	            return t.getTrailBlockSpeed();
	        }
	    }
	    return 0;
	}
}
