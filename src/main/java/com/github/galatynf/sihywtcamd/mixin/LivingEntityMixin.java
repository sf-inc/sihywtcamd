package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract EntityGroup getGroup();

    @Inject(method = "computeFallDamage", at = @At("HEAD"), cancellable = true)
    private void cancelArthropodFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
        if (ModConfig.get().overworld.arthropodNoFallDamage && this.getGroup().equals(EntityGroup.ARTHROPOD)) {
            cir.setReturnValue(0);
        }
    }
}
