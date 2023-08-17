package com.mactso.happytrails.events;

import java.util.UUID;

import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.managers.TrailBlockManager;

import net.minecraft.resources.ResourceLocation;
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
	
	static UUID UUID_HAPPYMODSPEED = UUID.fromString("793fcced-972d-45cb-b385-84694056001a");
	public static final AttributeModifier HAPPYMODSPEED_ATTR = new AttributeModifier(UUID.fromString("8d2c8d25-7a8c-4fbc-be91-8dba276ebbe0"), "happytrailsspeed", 1.0, Operation.MULTIPLY_BASE);
	
	public static boolean applyMovementSpeedAttribute(LivingEntity le, int amplifier) {


		AttributeModifier am = le.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(UUID_HAPPYMODSPEED);
		if (Math.abs(amplifier)<=10) {
			if (am != null) {
				le.getAttribute(Attributes.MOVEMENT_SPEED).removePermanentModifier(UUID_HAPPYMODSPEED); 
			}
			return false;
		}

		double amp = 0.0d;
		if (amplifier < -10) {
			amplifier += 10;
		} else if (amplifier > 10){
			amplifier -= 10;
		}
		
		if (amplifier == 99) amplifier = 225; // plaid speed
		double modAmp = (double)((amplifier)/2.0d);
		if (amplifier < 0) {
			modAmp = (double)(amplifier*0.01d);
		}
		if (modAmp > 0) modAmp *= 0.12f;
		if (modAmp < 0) modAmp *= 0.25f;
		
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
		if (amplifier == 10) {
			amplifier = 20;  // player "plaid" speed.
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
		e.addEffect(new MobEffectInstance(mobEffect, TWO_SECONDS, amplifier, true, MyConfig.isParticlesOn()  ));
		return;
	}
	
	@SuppressWarnings("deprecation")
	public static int getSpeedAmplifier(LivingEntity le, Level level) {
		Block b = level.getBlockState(le.blockPosition()).getBlock();
        ResourceLocation key = b.builtInRegistryHolder().key().location();
		TrailBlockManager.TrailBlockItem t = TrailBlockManager.getTrailBlockInfo(key.toString());
		
		if (t == null) { // standing on/in block with no configuration entry
			b = le.level.getBlockState(le.blockPosition().below()).getBlock();
	        key = b.builtInRegistryHolder().key().location();
			t = TrailBlockManager.getTrailBlockInfo(key.toString());
			if (t == null) { // lower block also has no configuration entry
				return 0;
			}
		}
		return t.getTrailBlockSpeed();
	}
	
}
