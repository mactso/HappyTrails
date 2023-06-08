package com.mactso.happytrails.events;

import java.util.UUID;

import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.config.TrailBlockManager;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;


public class MovementUtility {
	final static int TWO_SECONDS = 40;
	
	static UUID UUID_HAPPYMODSPEED = UUID.fromString("793fcced-972d-45cb-b385-84694056001a");
	public static final EntityAttributeModifier HAPPYMODSPEED_ATTR = new EntityAttributeModifier(UUID.fromString("8d2c8d25-7a8c-4fbc-be91-8dba276ebbe0"), "happytrailsspeed", 1.0, Operation.MULTIPLY_BASE);
	
	public static boolean applyMovementSpeedAttribute(LivingEntity le, int amplifier) {


		EntityAttributeModifier am = le.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getModifier(UUID_HAPPYMODSPEED);
		if (Math.abs(amplifier)<=10) {
			if (am != null) {
				le.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).removeModifier(UUID_HAPPYMODSPEED); 
			}
			return false;
		}

		double amp = 0.0d;
		double modAmp = (double)((amplifier)/15.0d);
		if (amplifier < 0) {
			modAmp = (double)(amplifier*0.01d);
		}
		if (modAmp > 0) modAmp -= 0.32;
		if (modAmp < 0) modAmp += 0.10;
		
		if (am != null) {
			amp = am.getValue();
		}

		if (modAmp == amp) {
			return true;
		}

		if (modAmp == 0.0) {
			le.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).removeModifier(UUID_HAPPYMODSPEED);
			return true;
		}
		le.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).removeModifier(UUID_HAPPYMODSPEED); 
		EntityAttributeModifier hms = new EntityAttributeModifier(UUID_HAPPYMODSPEED, "happytrailsspeed", modAmp, Operation.MULTIPLY_BASE);
		le.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addPersistentModifier(hms);  
		return true;

	}

	public static void applyMovementSlownessAttribute(LivingEntity le, int amplifier) {
		
	}

	public static void updateEffect(LivingEntity e, int amplifier,  StatusEffect mobEffect) {
		StatusEffectInstance ei = e.getStatusEffect(mobEffect);
		if (amplifier == 10) {
			amplifier = 20;  // player "plaid" speed.
		}
		if (ei != null) {
			if (amplifier > ei.getAmplifier()) {
				e.removeStatusEffect(mobEffect);
			} 
			if (amplifier == ei.getAmplifier() && ei.getDuration() > 10) {
				return;
			}
			if (ei.getDuration() > 10) {
				return;
			}
			e.removeStatusEffect(mobEffect);			
		}
		e.addStatusEffect(new StatusEffectInstance(mobEffect, TWO_SECONDS, amplifier, true, MyConfig.isParticlesOn()));
		return;
	}
	
	@SuppressWarnings("deprecation")
	public static int getSpeedAmplifier(LivingEntity le, World level) {
		Block b = level.getBlockState(le.getBlockPos()).getBlock();
		TrailBlockManager.TrailBlockItem t = TrailBlockManager.getTrailBlockInfo(b.getRegistryEntry().getKey().get().getValue().toString());
		
		if (t == null) { // standing on/in block with no configuration entry
			b = le.world.getBlockState(le.getBlockPos().down()).getBlock();
			t = TrailBlockManager.getTrailBlockInfo(b.getRegistryEntry().getKey().get().getValue().toString());  // check tostring after getKey().
			if (t == null) { // lower block also has no configuration entry
				return 0;
			}
		}
		return t.getTrailBlockSpeed();
	}
	
}
