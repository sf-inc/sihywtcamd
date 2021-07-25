package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndermanEntity.class)
public abstract class EndermanMixin {
    @Shadow abstract boolean isPlayerStaring(PlayerEntity player);

    @Inject(method = "setTarget", at = @At("HEAD"))
    private void addBlindnessE(LivingEntity target, CallbackInfo ci) {
        if (ModConfig.get().end.endermanBlindness
                && target instanceof PlayerEntity
                && isPlayerStaring((PlayerEntity) target)) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 150));
        }
    }
}
