package com.github.galatynf.sihywtcamd.statusEffect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.sound.SoundCategory;

/*
 * Effect that reduce every sound for a player, except ambient and weather ones that will be increased.
 * Amplifier goes from 0 to 99, in order to set the percentage of effect.
 * 0 means 1% of the maximum effect. At the maximum, sounds will be increased (resp reduced) of 50% of your current setting.
 * */
public class TinnitusStatusEffect extends StatusEffect {
    public TinnitusStatusEffect() {
        super(StatusEffectType.HARMFUL, 0xEBEBEB);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return false;
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity.world.isClient()
                && MinecraftClient.getInstance().player != null
                && MinecraftClient.getInstance().player.equals(entity)) {
            amplifier = Math.min(++amplifier, 100);
            for (SoundCategory soundCategory : SoundCategory.values()) {
                if (!soundCategory.equals(SoundCategory.MASTER)) {
                    float oldValue = MinecraftClient.getInstance().options.getSoundVolume(soundCategory);
                    if (soundCategory.equals(SoundCategory.AMBIENT) ||soundCategory.equals(SoundCategory.WEATHER)) {
                        MinecraftClient.getInstance().getSoundManager().updateSoundVolume(soundCategory,
                                Math.min(1.0F, oldValue * (1.0F + 0.5F * (amplifier / 100.0F))));
                    } else {
                        MinecraftClient.getInstance().getSoundManager().updateSoundVolume(soundCategory,
                                oldValue * (1.0F - 0.5F * (amplifier / 100.0F)));
                    }
                }
            }
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity.world.isClient()
                && MinecraftClient.getInstance().player != null
                && MinecraftClient.getInstance().player.equals(entity)) {
            for (SoundCategory soundCategory : SoundCategory.values()) {
                if (!soundCategory.equals(SoundCategory.MASTER)) {
                    float oldValue = MinecraftClient.getInstance().options.getSoundVolume(soundCategory);
                    MinecraftClient.getInstance().getSoundManager().updateSoundVolume(soundCategory, oldValue);
                }
            }
        }
    }
}
