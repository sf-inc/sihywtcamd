package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GhastEntity.class)
public class GhastMixin {
    @Inject(method = "createGhastAttributes", at = @At("HEAD"), cancellable = true)
    private static void increaseHealthG(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        if (ModConfig.get().ghastIncreasedHealth) {
            cir.setReturnValue(MobEntity.createMobAttributes()
                    .add(EntityAttributes.GENERIC_MAX_HEALTH, 42.0D)
                    .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 100.0D));
        }
    }
}
