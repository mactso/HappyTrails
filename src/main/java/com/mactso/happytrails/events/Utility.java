package com.mactso.happytrails.events;

import java.util.UUID;

import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.config.TrailBlockManager;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class Utility {
	final static int TWO_SECONDS = 40;
	
	static UUID UUID_HAPPYMODSPEED = UUID.fromString("793fcced-972d-45cb-b385-84694056001a");
	public static final AttributeModifier HAPPYMODSPEED_ATTR = new AttributeModifier(UUID.fromString("8d2c8d25-7a8c-4fbc-be91-8dba276ebbe0"), "happytrailsspeed", 1.0, Operation.MULTIPLY_BASE);
	
	public static boolean applyMovementSpeedAttribute(LivingEntity le, int amplifier) {


		AttributeModifier am = le.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(UUID_HAPPYMODSPEED);
		if (Math.abs(amplifier)<=10) {
			if (am != null) {
				System.out.println("Removed speed mod : " + am.getAmount());
				le.getAttribute(Attributes.MOVEMENT_SPEED).removePermanentModifier(UUID_HAPPYMODSPEED); 
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
			amp = am.getAmount();
		}

		if (modAmp == amp) {
			return true;
		}

		if (modAmp == 0.0) {
			le.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(UUID_HAPPYMODSPEED);
			return true;
		}
		le.getAttribute(Attributes.MOVEMENT_SPEED).removePermanentModifier(UUID_HAPPYMODSPEED); 
		AttributeModifier hms = new AttributeModifier(UUID_HAPPYMODSPEED, "happytrailsspeed", modAmp, Operation.MULTIPLY_BASE);
		le.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(hms);  
		return true;

	}

	public static void applyMovementSlownessAttribute(LivingEntity le, int amplifier) {
		
	}

	public static void updateEffect(LivingEntity e, int amplifier,  MobEffect mobEffect) {
		MobEffectInstance ei = e.getEffect(mobEffect);
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
		if (mobEffect == MobEffects.MOVEMENT_SPEED) System.out.print("Speed - ");
		if (mobEffect == MobEffects.MOVEMENT_SLOWDOWN) System.out.print("Slow - ");
		e.addEffect(new MobEffectInstance(mobEffect, TWO_SECONDS, amplifier, true, MyConfig.aParticlesOn  ));
		return;
	}
	
	public static int getSpeedAmplifier(LivingEntity le, Level level) {
		Block b = level.getBlockState(le.blockPosition()).getBlock();
		TrailBlockManager.TrailBlockItem t = TrailBlockManager.getTrailBlockInfo(b.getRegistryName().toString());
		
		if (t == null) { // standing on/in block with no configuration entry
			b = le.level.getBlockState(le.blockPosition().below()).getBlock();
			t = TrailBlockManager.getTrailBlockInfo(b.getRegistryName().toString());
			if (t == null) { // lower block also has no configuration entry
				return 0;
			}
		}
		return t.getTrailBlockSpeed();
	}
	
}
