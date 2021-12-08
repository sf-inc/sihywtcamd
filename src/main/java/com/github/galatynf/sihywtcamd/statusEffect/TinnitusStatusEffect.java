package com.github.galatynf.sihywtcamd.statusEffect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

/*
 * Effect that reduce every sound for a player, except ambient and weather ones that will be increased.
 * Amplifier goes from 0 to 99, in order to set the percentage of effect.
 * 0 means 1% of the maximum effect. At the maximum, sounds will be increased (resp reduced) of 50% of your current setting.
 * */
public class TinnitusStatusEffect extends StatusEffect {
    public TinnitusStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0xEBEBEB);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return false;
    }

    @Override
    public boolean isInstant() {
        return false;
    }
}
